// ======================================
// Project Name:ssm
// Package Name:com.kingyee.nhblood.common.security
// File Name:UserUtil.java
// Create Date:2019年10月24日  11:02
// ======================================
package com.kingyee.me.common.security;

import com.kingyee.me.entity.SysUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author fyq
 * @version 2019年10月24日  11:02
 */
public class UserUtil {

    public static final String USER_SESSION_NAME = "CRS_LOGIN_SESSION";

    /**
     * 取得session
     */
    private static HttpSession getSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        return request.getSession(true);
    }

    /**
     * 判断用户是否登录
     */
    public static boolean hasLogin() {
        return getSession().getAttribute(USER_SESSION_NAME) != null;
    }

    /**
     * 登录
     */
    public static void login(SysUser user) {
        HttpSession session = getSession();
        Object o = session.getAttribute(USER_SESSION_NAME);
        if (o != null) {
            session.removeAttribute(USER_SESSION_NAME);
        }
        session.setAttribute(USER_SESSION_NAME, user);
    }

    public static void logout() {
        getSession().removeAttribute(USER_SESSION_NAME);
    }


    /**
     * 取得登录用户信息
     */
    public static SysUser getLoginUser() {
        if (hasLogin()) {
            return (SysUser) getSession().getAttribute(USER_SESSION_NAME);
        } else {
            return null;
        }
    }

    /**
     * 取得登录用户的主键
     */
    public static Long getLoginUserId() {
        return hasLogin() ? Objects.requireNonNull(getLoginUser()).getNbId() : null;
    }

    /**
     * 取得登录用户的姓名
     */
    public static String getLoginUserName() {
        return hasLogin() ? Objects.requireNonNull(getLoginUser()).getNbLoginName() : "";
    }


}