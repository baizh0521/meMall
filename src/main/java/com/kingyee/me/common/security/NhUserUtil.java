package com.kingyee.me.common.security;

import com.kingyee.common.spring.mvc.WebUtil;
import com.kingyee.common.util.CookieUtils;
import com.kingyee.common.util.encrypt.RC4Util;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

/**
 * @Author baizh
 * @Description
 * @Date 2020/3/16
 */
@Component
public class NhUserUtil {
    public static String USER_SESSION_ID = "USER_LOGIN_SESSION";
    public static String COOKIE_AUTOLOGIN = "USER_LOGIN_COOKIE";
    //注册，登录来源
    public static String COOKIE_SOURCE = "USER_SOURCE_COOKIE";
    public static int COOKIE_TIME = 365 * 24 * 60 * 60;
    public static String MEDLIVE_LOGIN_URL = "http://www.medlive.cn/auth/login";

    //是否认证
    public static String MEDLIVE_CERTIFYIF_URL = "http://api.medlive.cn/user/check_userinfo_complete.php";

    //认证地址
    public static String MEDLIVE_CERTIFY_URL ="http://setting.medlive.cn/user/certify";
    //取得医师详细信息接口
    public static final String GET_USER_INFO = "http://api.medlive.cn/user/get_user_info.php?hashid=%s&checkid=%s";


    /** 取得session */
    private static HttpSession getSession() {
        return WebUtil.getOrCreateSession();
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public static boolean hasLogin() {
        return getSession().getAttribute(USER_SESSION_ID) != null;
    }

    /**
     * 做登录时，需要的一些操作
     */
    public static void login(Long userId) throws UnsupportedEncodingException {
        getSession().setAttribute(USER_SESSION_ID, userId);
        setCookie(WebUtil.getResponse(), COOKIE_AUTOLOGIN, userId.toString(), COOKIE_TIME);
    }

    /**
     * 做logout时，需要的一些操作
     */
    public static void logout() throws UnsupportedEncodingException {
        getSession().setAttribute(USER_SESSION_ID, null);
        setCookie(WebUtil.getResponse(), COOKIE_AUTOLOGIN, "", -1);
    }


    /**
     * 取得用户的id
     *
     * @return
     */
    public static Long getUserId() {
        if (hasLogin()) {
            return (Long)getSession().getAttribute(USER_SESSION_ID);
        }
        return null;
    }

    /**
     * 创造cookie.
     * @throws UnsupportedEncodingException
     *
     */
    public static void setCookie(HttpServletResponse response,
                                 String cookieName, String userName, int maxAge) throws UnsupportedEncodingException{
        /* 自动登录 cookie设定 */
        String autoLogin = RC4Util.encode(userName);
        CookieUtils.setCookie(response, cookieName, autoLogin, maxAge);
    }

    /**
     * 获得cookie对应的内容
     *
     * @param request
     * @param key
     * @return Cookie
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie cookie = CookieUtils.getCookie(request, key);
        if(cookie != null){
            String autoLogin = cookie.getValue();
            autoLogin = RC4Util.decode(autoLogin);
            return autoLogin;
        }else{
            return null;
        }
    }
}
