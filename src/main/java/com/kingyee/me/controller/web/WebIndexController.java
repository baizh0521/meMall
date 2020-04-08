// ======================================
// Project Name:nhblood
// Package Name:com.kingyee.nhblood.controller.web
// File Name:IndexController.java
// Create Date:2020年02月03日  15:30
// ======================================
package com.kingyee.me.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.Const;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.spring.mvc.WebUtil;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.me.MyCustomConfig;
import com.kingyee.me.common.security.FileString;
import com.kingyee.me.common.security.NhUserUtil;
import com.kingyee.me.entity.NhClick;
import com.kingyee.me.entity.NhUser;
import com.kingyee.me.entity.NhVideo;
import com.kingyee.me.entity.NhView;
import com.kingyee.me.service.INhClickService;
import com.kingyee.me.service.INhUserService;
import com.kingyee.me.service.INhVideoService;
import com.kingyee.me.service.INhViewService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.kingyee.common.util.TimeUtil.FORMAT_DATES;
import static com.kingyee.me.common.security.NhUserUtil.COOKIE_SOURCE;
import static com.kingyee.me.common.security.NhUserUtil.USER_SESSION_ID;

/**
 * @Author BZH
 * @Description 前台
 * @Date 2020/3/17 10:44
 * @Param
 * @return
 **/
@Controller
@RequestMapping(value = "/web")
public class WebIndexController {

    private static final Logger logger = LoggerFactory.getLogger(WebIndexController.class);

    private final INhUserService userService;

    private final INhVideoService videoService;

    private final INhClickService clickService;

    private final INhViewService  viewService;

    public WebIndexController(INhUserService userService, INhVideoService videoService,INhClickService clickService,INhViewService  viewService) {
        this.userService = userService;
        this.videoService = videoService;
        this.clickService = clickService;
        this.viewService = viewService;
    }

    @Autowired
    private MyCustomConfig customConfig;

     /**
     * 列表页  首页
     *
     * @return
     */
    @RequestMapping(value = {"", "/index"})
    public String index() {
        return "/web/index";
    }


    /**
     * 视频列表
     *
     * @param page
     * @param limit
     * @param keyword
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonNode list(Integer page, Integer limit, String keyword) {
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = Const.ADMIN_ROWSPERPAGE_MORE;
        }
        QueryWrapper<NhVideo> queryWrapper = new QueryWrapper<>();
        IPage<NhVideo> pageInfo = new Page<>(page, limit);
        //查询条件 标题
        queryWrapper.lambda().like(!StringUtils.isEmpty(keyword), NhVideo::getNvVideoTitle, keyword)
                             .eq(NhVideo::getNvState,1);
        queryWrapper.orderByDesc("nv_order");
        pageInfo = videoService.page(pageInfo, queryWrapper);
        return JacksonMapper.newDataInstance(pageInfo);
    }

    /**
     * 浏览视频详情页
     *
     * @param request
     * @param mm
     * @param videoId
     * @return
     */
    @RequestMapping(value = "/videoDetail")
    public String videoDetail(HttpServletRequest request, ModelMap mm, Long videoId) {
        NhVideo video = videoService.getById(videoId);
        mm.addAttribute("video", video);
        String medUserId = request.getSession(true).getAttribute(USER_SESSION_ID).toString();
        NhUser user = userService.getLocalUserByMedliveId(medUserId);
        Long firstTime = viewService.getFirstTimeById(videoId,user.getNuId());
        //更新视频浏览量
        video.setNvViewNum(video.getNvViewNum()==null?1:video.getNvViewNum()+1);
        videoService.updateById(video);
        //查询是否点赞
        Boolean b = false;
        QueryWrapper<NhClick> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(videoId!=null, NhClick::getNcVideoId, videoId).eq(user.getNuId()!=null,NhClick::getNcUserId,user.getNuId());
        NhClick click = clickService.getOne(queryWrapper);
        if(click!=null){
            b=true;
        }
        mm.addAttribute("b",b);
        //新增浏览记录
        NhView view = new NhView();
        view.setNvMedicineId(user.getNuMedicineId());
        view.setNvUserId(user.getNuId());
        view.setNvVideoId(videoId);
        view.setNvSource(NhUserUtil.getCookie(request,COOKIE_SOURCE)!=null?NhUserUtil.getCookie(request,COOKIE_SOURCE):"");
        view.setNvVideoTitle(video.getNvVideoTitle());
        view.setNvFirstTime(firstTime);
        view.setNvCreateTime(System.currentTimeMillis());
        view.setNvPlayTime(0L);
        view.setNvStayTime(0L);
        view.setNvViewTime(0L);
        viewService.save(view);
        //ppt图片展示
        String folderPath = customConfig.getPptFilePath();
        String pptImgFileName = video.getNvPptName();
        String path = WebUtil.getRealPath(folderPath);
        File file = new File(path + File.separator + pptImgFileName);
        String[] fileName = file.list();
        List<FileString> imageName = new ArrayList<FileString>();
        if (fileName != null && fileName.length > 0) {
            for (int i = 0; i < fileName.length; i++) {
                String name = fileName[i];
                imageName.add(new FileString(folderPath + "/" + pptImgFileName + "/" + name));
            }
            Collections.sort(imageName);
        }
        mm.addAttribute("timeStr", TimeUtil.longToString(video.getNvCreateTime(),FORMAT_DATES));
        mm.addAttribute("imageName", imageName);
        mm.addAttribute("view", view);
        return "/web/videoDetail";
    }

    /**
     * 视频 点赞/取消点赞
     *
     * @param videoId
     * @return
     */
    @RequestMapping(value = "/videoClick")
    @ResponseBody
    public JsonNode videoClick(HttpServletRequest request,Long videoId){
        NhVideo video = videoService.getById(videoId);
        String medUserId = request.getSession(true).getAttribute(USER_SESSION_ID).toString();
        NhUser user = userService.getLocalUserByMedliveId(medUserId);
        QueryWrapper<NhClick> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(!StringUtils.isEmpty(medUserId), NhClick::getNcMedicineId, medUserId).eq(videoId!=null, NhClick::getNcVideoId, videoId);
        NhClick nhClick = clickService.getOne(queryWrapper);
        Long firstTime = viewService.getFirstTimeById(videoId,user.getNuId());
        if(nhClick!=null){
            video.setNvClickNum(video.getNvClickNum()==0?0:video.getNvClickNum()-1);
            clickService.removeById(nhClick.getNcId());
        }else {
            video.setNvClickNum(video.getNvClickNum()+1);
            NhClick click = new NhClick();
            click.setNcMedicineId(user.getNuMedicineId());
            click.setNcUserId(user.getNuId());
            click.setNcVideoId(videoId);
            click.setNcVideoTitle(video.getNvVideoTitle());
            click.setNcFirstTime(firstTime);
            click.setNcCreateTime(System.currentTimeMillis());
            clickService.save(click);
        }
        videoService.updateById(video);
        return JacksonMapper.newSuccessInstance();
    }


     /**
     * 记录视频播放时长
     *
     * @param viewId
     * @return
     */
    @RequestMapping(value = "updateVideoPalyTime")
    @ResponseBody
    public JsonNode updateVideoPalyTime(Long viewId, Integer palyTime) {
        if (viewId != null) {
            if(palyTime>0){
                NhView view = viewService.getById(viewId);
                view.setNvPlayTime(palyTime.longValue());
                viewService.updateById(view);
            }
        }
        return JacksonMapper.newSuccessInstance();
    }

    /**
     * 记录页面停留时间
     *
     * @param viewId
     * @return
     */
    @RequestMapping(value = "updateVideoViewTime")
    @ResponseBody
    public JsonNode updateVideoViewTime(Long viewId) {
        if (viewId != null) {
            NhView view = viewService.getById(viewId);
            view.setNvStayTime(view.getNvStayTime()+2);
            viewService.updateById(view);
        }
        return JacksonMapper.newSuccessInstance();
    }


    /**
     * 保利威的视频播放页面
     * @param mm
     * @param vid 保利威的vid
     * @return
     */
    @RequestMapping(value = "video/{vid}")
    public String video(ModelMap mm, @PathVariable String vid){
        mm.addAttribute("vid", vid);
        return "web/video";
    }

    /**
     * 广告页面
     *
     */
    @RequestMapping(value = "adv")
    public String advPage(){
        return "web/advertisement";
    }

}