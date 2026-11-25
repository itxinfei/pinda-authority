package com.itheima.pinda.authority.biz.service.auth.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itheima.pinda.auth.server.utils.JwtTokenServerUtils;
import com.itheima.pinda.auth.utils.JwtUserInfo;
import com.itheima.pinda.auth.utils.Token;
import com.itheima.pinda.authority.biz.service.auth.ResourceService;
import com.itheima.pinda.authority.biz.service.auth.UserService;
import com.itheima.pinda.authority.biz.service.common.LoginLogService;
import com.itheima.pinda.authority.config.AuthorityProperties;
import com.itheima.pinda.authority.dto.auth.LoginDTO;
import com.itheima.pinda.authority.dto.auth.ResourceQueryDTO;
import com.itheima.pinda.authority.dto.auth.UserDTO;
import com.itheima.pinda.authority.entity.auth.Resource;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.constant.CacheKey;
import com.itheima.pinda.dozer.DozerUtils;
import com.itheima.pinda.exception.code.ExceptionCode;
import com.itheima.pinda.log.util.AddressUtil;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import com.itheima.pinda.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证管理器
 */
@Service
@Slf4j
public class AuthManager {
    @Autowired
    private AuthorityProperties authorityProperties;
    @Autowired
    private UserService userService;
    @Autowired
    private DozerUtils dozerUtils;
    @Autowired
    private JwtTokenServerUtils jwtTokenServerUtils;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private CacheChannel cacheChannel;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private HttpServletRequest request;

    //登录认证
    public R<LoginDTO> login(String account, String password) {
        //校验账号、密码是否正确
        R<User> userR = check(account, password);
        Boolean isError = userR.getIsError();
        if (isError) {
            //直接透传 check() 返回的具体失败原因（禁用/锁定/凭证错误），避免被笼统覆盖
            return R.fail(userR.getCode(), userR.getMsg());
        }

        //为用户生成jwt令牌
        User user = userR.getData();
        Token token = generateUserToken(user);

        //登录成功后由服务端落库登录日志（账号取自已认证用户，杜绝前端伪造/匿名写入）
        String ua = StrUtil.sub(request.getHeader("user-agent"), 0, 500);
        String ip = ServletUtil.getClientIP(request);
        String location = AddressUtil.getRegion(ip);
        userService.updateLoginTime(user.getAccount());
        loginLogService.save(user.getAccount(), ua, ip, location, "登陆成功");

        //查询当前用户可以访问的资源权限
        List<Resource> userResource = resourceService.findVisibleResource(ResourceQueryDTO.builder().userId(user.getId()).build());
        log.info("当前用户拥有的资源权限为：" + userResource);

        List<String> permissionList = null;
        if (userResource != null && userResource.size() > 0) {
            //用户对应的权限（给前端使用的）
            permissionList = userResource.stream().map(Resource::getCode).collect(Collectors.toList());

            //将用户对应的权限（给后端网关使用的）进行缓存
            List<String> visibleResource = userResource.stream().map((resource -> {
                return resource.getMethod() + resource.getUrl();
            })).collect(Collectors.toList());
            //缓存权限数据
            cacheChannel.set(CacheKey.USER_RESOURCE, user.getId().toString(), visibleResource);
        }

        //封装返回结果
        LoginDTO loginDTO = LoginDTO.builder().
                user(dozerUtils.map(userR.getData(), UserDTO.class)).
                token(token).
                permissionsList(permissionList).
                build();
        return R.success(loginDTO);
        //return null;
    }

    //账号、密码校验
    public R<User> check(String account, String password) {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, account));

        //用户名不存在：统一返回凭证错误，避免用户名枚举
        if (user == null) {
            return R.fail(ExceptionCode.JWT_USER_INVALID);
        }

        //校验账号是否禁用
        if (user.getStatus() == null || !user.getStatus()) {
            return R.fail(ExceptionCode.JWT_USER_ENABLED);
        }

        //校验是否因密码错误次数过多被锁定
        int errorNum = user.getPasswordErrorNum() == null ? 0 : user.getPasswordErrorNum();
        if (errorNum >= authorityProperties.getMaxPasswordErrorNum()) {
            return R.fail(ExceptionCode.JWT_USER_INVALID.getCode(), "密码错误次数过多，账户已锁定，请联系管理员重置");
        }

        //校验密码
        if (!PasswordUtils.matches(password, user.getPassword())) {
            //累加密码错误次数，达到阈值后下次登录将被锁定
            userService.updatePasswordErrorNumById(user.getId());
            return R.fail(ExceptionCode.JWT_USER_INVALID);
        }

        //认证成功：重置密码错误次数（仅当存在历史错误时）
        if (errorNum > 0) {
            userService.resetPassErrorNum(user.getId());
        }
        return R.success(user);
    }

    //为当前登录用户生成对应的jwt令牌
    public Token generateUserToken(User user) {
        JwtUserInfo jwtUserInfo = new JwtUserInfo(user.getId(), user.getAccount(), user.getName(), user.getOrgId(), user.getStationId());
        Token token = jwtTokenServerUtils.generateUserToken(jwtUserInfo, null);
        return token;
    }
}
