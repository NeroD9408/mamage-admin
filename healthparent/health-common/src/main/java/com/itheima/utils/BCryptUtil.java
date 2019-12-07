package com.itheima.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * <p>
 * BCryptPasswordEncoder加密工具类
 * Spring Security 提供的 BCryptPasswordEncoder 加密算法进行加密
 * </p>
 *
 * @author 张辉
 * @since 2019-01-02
 */
public class BCryptUtil {
    /**
     * 对字符串加密
     *
     * @param str 加密对象字符串
     * @return 已加密结果字符串
     */
    public static String encode(String str) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(str);
    }

    /**
     * 验证密码是否和已加密对象字符串匹配
     *
     * @param passWord       密码字符串
     * @param encodePassWord 已加密对象字符串
     * @return
     */
    public static boolean isMatch(String passWord, String encodePassWord) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(passWord, encodePassWord);
    }

    public static void main(String[] args) {
        String encode = encode("1234");
        System.out.println(encode);
        boolean match = isMatch("1234", "$2a$10$3xW2nBjwBM3rx1LoYprVsemNri5bvxeOd/QfmO7UDFQhW2HRHLi.C");
        System.out.println(match);
    }
}

