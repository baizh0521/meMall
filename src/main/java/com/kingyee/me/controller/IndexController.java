// ======================================
// Project Name:nhblood
// Package Name:com.kingyee.nhblood.controller.web
// File Name:IndexController.java
// Create Date:2020年02月19日  14:20
// ======================================
package com.kingyee.me.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kingyee.me.MyCustomConfig;
import com.kingyee.me.common.security.CommonUtils;
import com.kingyee.me.common.security.NhUserUtil;
import com.kingyee.me.entity.NhUser;
import com.kingyee.me.service.INhUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.kingyee.me.common.security.NhUserUtil.*;

/**
 * @Author BZH
 * @Description 前台
 * @Date 2020/3/16 14:28
 * @Param
 * @return
 **/
@Controller
@RequestMapping(value = "")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private MyCustomConfig customConfig;
    @Autowired
    private INhUserService service;
    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = {"", "/index"})
    public String index() {

        return "redirect:/web/index";
    }

    /**
     *
     * 医脉通登录
     * */
    @RequestMapping("/medliveLogin")
    public String medliveLogin() {
        return "redirect:http://www.medlive.cn/pgtoken/userinfo.php?rtn_url="+customConfig.getDomain()+"/getUserInfo";
    }

    /**
     * 医脉通获取用户信息以及本网站登入
     *
     * @return
     */
    @RequestMapping("/getUserInfo")
    public String getUserinfo(HttpServletRequest request, HttpServletResponse response, String userinfo) {
        try {
            if (userinfo != null && !userinfo.isEmpty()) {
                userinfo = CommonUtils.decryptAES(userinfo, "kEV7TXRS6k8z1uEr");
                JsonElement element = JsonParser.parseString(userinfo);//将String类型转换成JsonElement
                JsonObject sobj = element.getAsJsonObject(); //将获取的数据转为json类型
//                {"user_id":"2844400","nick":"2844400","thumb":"http://webres.medlive.cn/upload/thumb/000/089/490_big","certify_flg":"doctor","reg_time":"2017-05-26 16:51:24","reg_from":"dic_android"}
                String user_id = sobj.get("user_id").getAsString();
                HttpSession session = request.getSession(true);
                try {
                    //保存登入信息
                    NhUser user = new NhUser();
                    QueryWrapper<NhUser> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(!StringUtils.isEmpty(user_id), NhUser::getNuMedicineId, user_id);
                    user = service.getOne(queryWrapper);
                    if(user!=null){
                         user = service.getUserByMedliveId(Long.parseLong(user_id),user.getNuId(),request);
                    }else {
                         user = service.getUserByMedliveId(Long.parseLong(user_id),null,request);
                    }
                     session.setAttribute(USER_SESSION_ID, user_id);
                     NhUserUtil.setCookie(response,COOKIE_AUTOLOGIN,user_id,COOKIE_TIME);
                     if(user.getNuCertifyFlg()==0){
                         return "redirect:" + MEDLIVE_CERTIFY_URL;
                     }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("保存用户信息出错" + e);
                    return "error";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取用户信息时出错。" + e);
            return "error";
        }
        return "redirect:" + "/";
    }

}