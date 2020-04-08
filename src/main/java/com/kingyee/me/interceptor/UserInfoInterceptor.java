package com.kingyee.me.interceptor;

import com.kingyee.me.MyCustomConfig;
import com.kingyee.me.common.security.NhUserUtil;
import com.kingyee.me.entity.NhUser;
import com.kingyee.me.service.INhUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import static com.kingyee.me.common.security.NhUserUtil.COOKIE_TIME;
import static com.kingyee.me.common.security.NhUserUtil.USER_SESSION_ID;

/**
 * 前台用户信息取得拦截器
 *
 * @author peihong
 * @version 2014-4-14 下午2:03:58
 */
@Component
public class UserInfoInterceptor extends HandlerInterceptorAdapter {
    private final static Logger logger = LoggerFactory.getLogger(UserInfoInterceptor.class);
    @Autowired
    private MyCustomConfig customConfig;

    @Autowired
    private INhUserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断用户来源存入cookie
        String source = "";
        if (request.getParameter("source") != null) {
            source = request.getParameter("source");
        }
        if (StringUtils.isNotEmpty(source)) {
            NhUserUtil.setCookie(response, NhUserUtil.COOKIE_SOURCE, source, COOKIE_TIME);
        }
        //判断是否登录
        if (!NhUserUtil.hasLogin()) {
            String userIdStr = NhUserUtil.getCookie(request, NhUserUtil.COOKIE_AUTOLOGIN);
            logger.info("getCookie:" + userIdStr);
            if(StringUtils.isNotEmpty(userIdStr)){
                NhUserUtil.login(Long.parseLong(userIdStr));
            }else {
                response.sendRedirect(NhUserUtil.MEDLIVE_LOGIN_URL+"?service="+customConfig.getDomain()+"/medliveLogin");
                return false;
            }
        }

        NhUser user = userService.getLocalUserByMedliveId(request.getSession()
                .getAttribute(USER_SESSION_ID).toString());
        //医生认证判断，如果本地判断未认证 去接口判断，如果接口判断为 认证通过 则更新本地 否则 跳转到认证页面
        if(user.getNuCertifyFlg()==null|| user.getNuCertifyFlg()==0){
              Boolean b = userService.getCertifyStateByMedliveId(user.getNuMedicineId());
              if(b){
                  user.setNuCertifyFlg(1);
                  userService.updateById(user);
              }else {
                  response.sendRedirect(NhUserUtil.MEDLIVE_CERTIFY_URL);
                  return false;
              }
        }
        return super.preHandle(request, response, handler);
    }
}
