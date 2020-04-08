package com.kingyee.me.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.Const;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.StrUtils;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.me.entity.NhVideo;
import com.kingyee.me.service.INhVideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author baizhihao
 * @Description 视频管理
 * @Date 2020/3/13 13:25
 * @Param
 * @return
 **/
@Controller
@RequestMapping(value = "/admin/video")
public class AdminVideoController {

    private static final Logger logger = LoggerFactory.getLogger(AdminVideoController.class);
    @Autowired
    private INhVideoService nhVideoService;

    @RequestMapping(value = {"/list"})
    public String list() {
        return "admin/video/list";
    }

    @RequestMapping(value = {"/edit"})
    public String edit(ModelMap mm, Long id) {
        try {
            if (id != null) {
                NhVideo video = nhVideoService.getById(id);
                if (video != null) {
                    mm.addAttribute("video", video);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = "编辑视频信息异常。";
            logger.error(errmsg, e);
        }
        return "admin/video/edit";
    }

    /**
     * 视频列表
     *
     * @param mm
     * @param page
     * @param limit
     * @param keyword
     * @param videoField
     * @param videoField
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listAjax", method = RequestMethod.GET)
    public JsonNode list(ModelMap mm, Integer page, Integer limit, String keyword, String videoField,String field,String order) {
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = Const.ADMIN_ROWSPERPAGE_MORE;
        }
        QueryWrapper<NhVideo> queryWrapper = new QueryWrapper<>();
        IPage<NhVideo> pageInfo = new Page<>(page, limit);
        //查询条件 标题
        queryWrapper.lambda().like(!StringUtils.isEmpty(keyword), NhVideo::getNvVideoTitle, keyword);
        if(StringUtils.isEmpty(order)){
            queryWrapper.orderByDesc("nv_id");
        }else{
            queryWrapper.orderByAsc("asc".equalsIgnoreCase(order), StrUtils.humpToUnderline(field));
            queryWrapper.orderByDesc("desc".equalsIgnoreCase(order), StrUtils.humpToUnderline(field));
        }
        pageInfo = nhVideoService.page(pageInfo, queryWrapper);
        return JacksonMapper.newCountInstance(pageInfo);
    }

    /**
     * 根据id删除数据
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public JsonNode del(ModelMap mm, Long id) {
        try {
            NhVideo video = nhVideoService.getById(id);
            nhVideoService.removeById(video);
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = "删除视频信息异常。";
            logger.error(errmsg, e);
            return JacksonMapper.newErrorInstance(errmsg);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/save")
    public JsonNode save(NhVideo video) {
        try {
            Long now = TimeUtil.dateToLong();
            if (video.getNvId() == null) {
                // 新增
                video.setNvCreateTime(now);
                video.setNvClickNum(0);
                video.setNvViewNum(0);
                nhVideoService.save(video);
            } else {
                // 新增
                nhVideoService.updateById(video);
            }
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = "保存视频信息异常。";
            logger.error(errmsg, e);
            return JacksonMapper.newErrorInstance(errmsg);
        }
    }

}