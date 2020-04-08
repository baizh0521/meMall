// ======================================
// Project Name:ssm
// Package Name:com.kingyee.nhblood.controller
// File Name:AdminController.java
// Create Date:2019年10月24日  13:57
// ======================================
package com.kingyee.me.controller.admin;

import com.kingyee.common.util.EncryptUtil;
import com.kingyee.me.common.security.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kingyee.me.entity.SysUser;
import com.kingyee.me.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;

/**
 * @author fyq
 * @version 2019年10月24日  13:57
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    private final ISysUserService sysUserService;

    public AdminController(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @RequestMapping(value = {"", "/index"})
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginInit() {
        return "/admin/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(ModelMap mm, String name, String password) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            mm.addAttribute("errMsg", "用户名或密码不能为空！");
            return "/admin/login";
        }
        try {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>()
                    .eq("nb_login_name", name)
                    .eq("nb_password", EncryptUtil.getSHA256Value(password));
            SysUser user = sysUserService.getOne(queryWrapper,false);
            if (user == null) {
                mm.addAttribute("errMsg", "用户名或密码输入错误！");
                return "/admin/login";
            }
            UserUtil.login(user);
            return "redirect:/admin/video/list";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            mm.addAttribute("errMsg", "出错了~");
            return "/admin/login";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        UserUtil.logout();
        return "/admin/login";
    }

    public static void main(String[] args) {
        try {
            System.out.println( EncryptUtil.getSHA256Value("kingyee@456"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}