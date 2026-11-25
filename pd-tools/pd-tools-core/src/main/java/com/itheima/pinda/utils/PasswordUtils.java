package com.itheima.pinda.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密工具类
 * <p>
 * 使用 BCrypt 算法对密码进行哈希和校验，
 * 替代原有的无盐 MD5 方式（彩虹表可在秒级破解）。
 * </p>
 */
public final class PasswordUtils {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private PasswordUtils() {
    }

    /**
     * 对原始密码进行 BCrypt 加密
     *
     * @param rawPassword 原始密码（明文）
     * @return BCrypt 哈希值
     */
    public static String encode(CharSequence rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    /**
     * 校验原始密码与存储的哈希值是否匹配
     *
     * @param rawPassword     原始密码（明文）
     * @param encodedPassword 存储的 BCrypt 哈希值
     * @return true 表示匹配
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }
}
