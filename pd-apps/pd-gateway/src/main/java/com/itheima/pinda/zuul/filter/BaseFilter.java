package com.itheima.pinda.zuul.filter;

import cn.hutool.core.util.StrUtil;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.adapter.IgnoreTokenConfig;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础 网关过滤器
 *
 * @author Administrator
 */
public abstract class BaseFilter extends ZuulFilter {

    @Value("${server.servlet.context-path}")
    protected String zuulPrefix;

    /**
     * 将请求 uri 归一化为「服务路径 + 资源路径」形式（去掉 context-path 与服务前缀）。
     * <p>
     * 例如 {@code /api/pd-auth/user/page} → {@code /user/page}。
     * 网关的「忽略令牌判定」与「权限标识符计算」必须共用同一归一化结果，
     * 故抽取到此共享方法，避免两处逻辑分叉导致判定错位。
     *
     * @param request 当前请求
     * @return 归一化后的 uri
     */
    protected String normalizeUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = StrUtil.subSuf(uri, zuulPrefix.length());
        uri = StrUtil.subSuf(uri, uri.indexOf("/", 1));
        return uri;
    }

    /**
     * 判断当前请求uri是否需要忽略（直接放行）
     *
     * @return
     */
    protected boolean isIgnoreToken() {
        //动态获取当前请求的uri
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String uri = normalizeUri(request);
        boolean ignoreToken = IgnoreTokenConfig.isIgnoreToken(uri);
        return ignoreToken;
    }

    /**
     * 网关抛异常，不再进行路由，而是直接返回到前端
     *
     * @param errMsg
     * @param errCode
     * @param httpStatusCode
     */
    protected void errorResponse(String errMsg, int errCode, int httpStatusCode) {
        RequestContext ctx = RequestContext.getCurrentContext();
        //设置响应状态码
        ctx.setResponseStatusCode(httpStatusCode);
        //设置响应头信息
        ctx.addZuulResponseHeader("Content-Type", "application/json;charset=utf-8");
        if (ctx.getResponseBody() == null) {
            //设置响应体
            ctx.setResponseBody(R.fail(errCode, errMsg).toString());
            //不进行路由，直接返回
            ctx.setSendZuulResponse(false);
        }
    }
}
