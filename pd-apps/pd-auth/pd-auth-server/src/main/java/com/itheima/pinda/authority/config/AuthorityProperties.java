package com.itheima.pinda.authority.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 权限服务业务配置
 */
@Data
@Component
@ConfigurationProperties(prefix = AuthorityProperties.PREFIX)
public class AuthorityProperties {
    public static final String PREFIX = "pinda.authority";

    /**
     * 密码最大错误次数，超过则锁定账户，需管理员重置
     */
    private int maxPasswordErrorNum = 5;
}
