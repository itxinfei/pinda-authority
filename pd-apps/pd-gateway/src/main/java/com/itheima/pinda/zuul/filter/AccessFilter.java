package com.itheima.pinda.zuul.filter;

import cn.hutool.core.util.StrUtil;
import com.itheima.pinda.authority.dto.auth.ResourceQueryDTO;
import com.itheima.pinda.authority.entity.auth.Resource;
import com.itheima.pinda.common.constant.CacheKey;
import com.itheima.pinda.context.BaseContextConstants;
import com.itheima.pinda.exception.code.ExceptionCode;
import com.itheima.pinda.zuul.api.ResourceApi;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 鉴权处理过滤器
 *
 * @author Administrator
 */
@Slf4j
@Component
public class AccessFilter extends BaseFilter {

    @javax.annotation.Resource(description = "com.itheima.pinda.zuul.api.ResourceApi")
    private ResourceApi resourceApi;

    @javax.annotation.Resource
    private CacheChannel cacheChannel;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 鉴权处理逻辑
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //第1步：判断当前请求uri是否需要忽略
        if (isIgnoreToken()) {
            //当前请求需要忽略，直接放行
            return null;
        }

        //第2步：获取当前请求的请求方式和uri，拼接成GET/user/page这种形式，称为权限标识符
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String method = request.getMethod();//GET POST PUT
        String uri = normalizeUri(request);
        String permission = method + uri;//GET/user/page

        //第3步：从缓存中获取所有需要进行鉴权的资源
        CacheObject cacheObject = cacheChannel.get(CacheKey.RESOURCE, CacheKey.RESOURCE_NEED_TO_CHECK);
        List<String> list = (List<String>) cacheObject.getValue();
        if (list == null) {
            //缓存中没有相应数据
            R<List> apiResult = resourceApi.list();
            if (apiResult == null || apiResult.getIsError() || apiResult.getData() == null) {
                errorResponse("获取资源列表失败", ExceptionCode.UNAUTHORIZED.getCode(), 200);
                return null;
            }
            list = apiResult.getData();
            //放入缓存中
            if (list != null && list.size() > 0) {
                cacheChannel.set(CacheKey.RESOURCE, CacheKey.RESOURCE_NEED_TO_CHECK, list);
            }
        }

        //第4步：判断这些资源是否包含当前请求的权限标识符，如果不包含当前请求的权限标识符，则返回未经授权错误提示
        long count = list.stream().filter(resource -> match(permission, resource)).count();

        if (count == 0) {
            //当前请求是一个未知请求，直接返回未授权异常信息
            errorResponse(ExceptionCode.UNAUTHORIZED.getMsg(), ExceptionCode.UNAUTHORIZED.getCode(), 200);
            return null;
        }

        //第5步：如果包含当前的权限标识符，则从zuul header中取出用户id，根据用户id取出缓存中的用户拥有的权限
        String userId = RequestContext.getCurrentContext().getZuulRequestHeaders().get(BaseContextConstants.JWT_KEY_USER_ID);
        if (StrUtil.isBlank(userId)) {
            //用户标识缺失（未携带/解析失败 token），拒绝访问
            errorResponse(ExceptionCode.UNAUTHORIZED.getMsg(), ExceptionCode.UNAUTHORIZED.getCode(), 200);
            return null;
        }
        List<String> visibleResource = (List<String>) cacheChannel.get(CacheKey.USER_RESOURCE, userId).getValue();
        if (visibleResource == null) {
            //缓存中不存在，需要通过接口远程调用权限服务来获取
            ResourceQueryDTO resourceQueryDTO = ResourceQueryDTO.builder().userId(new Long(userId)).build();
            R<List<Resource>> apiResult = resourceApi.visible(resourceQueryDTO);
            if (apiResult != null && !apiResult.getIsError() && apiResult.getData() != null) {
                List<Resource> resourceList = apiResult.getData();
                visibleResource = resourceList.stream().map((resource -> {
                    return resource.getMethod() + resource.getUrl();
                })).collect(Collectors.toList());
                //将当前用户拥有的权限载入缓存
                cacheChannel.set(CacheKey.USER_RESOURCE, userId, visibleResource);
            }
        }

        if (visibleResource == null || visibleResource.isEmpty()) {
            // 用户无任何权限，拒绝访问
            errorResponse(ExceptionCode.UNAUTHORIZED.getMsg(), ExceptionCode.UNAUTHORIZED.getCode(), 200);
            return null;
        }

        //第6步：如果用户拥有的权限包含当前请求的权限标识符则说明当前用户拥有权限，直接放行
        count = visibleResource.stream().filter(resource -> match(permission, resource)).count();

        if (count > 0) {
            //当前用户拥有访问权限，直接放行
            return null;
        } else {
            //第7步：如果用户拥有的权限不包含当前请求的权限标识符则说明当前用户没有权限，返回未经授权错误提示
            errorResponse(ExceptionCode.UNAUTHORIZED.getMsg(), ExceptionCode.UNAUTHORIZED.getCode(), 200);
            return null;
        }
    }

    /**
     * 权限标识符匹配（基于路径段边界的精确匹配）
     * <p>
     * 标识符格式为 方法+uri，如 {@code GET/user/5}、{@code POST/role/authority/*}。
     * 匹配规则：
     * 1. 方法与每一个路径段必须完全一致，杜绝 {@code GET/role} 误匹配 {@code GET/roleAuthority} 这类字符串前缀越权；
     * 2. 资源段为 {@code *} 时匹配任意单一路径段（兼容资源表中 {@code /role/authority/*} 写法）；
     * 3. 资源路径允许作为请求路径的段边界前缀（如 {@code /user} 授权其下 {@code /user/5}、{@code /user/page} 等子接口），
     *    从而在不破坏现有资源注册模型的前提下修复越权绕过。
     *
     * @param permission 当前请求的权限标识符（方法+归一化uri）
     * @param resource   已注册的资源标识符（方法+url，url 可含 * 通配段）
     * @return 是否匹配
     */
    private boolean match(String permission, String resource) {
        String[] pSeg = permission.split("/");
        String[] rSeg = resource.split("/");
        // 资源段数多于请求段数，必然无法匹配
        if (rSeg.length > pSeg.length) {
            return false;
        }
        for (int i = 0; i < rSeg.length; i++) {
            if ("*".equals(rSeg[i])) {
                continue;
            }
            if (!rSeg[i].equals(pSeg[i])) {
                return false;
            }
        }
        return true;
    }
}
