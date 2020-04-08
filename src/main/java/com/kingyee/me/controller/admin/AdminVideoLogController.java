package com.kingyee.me.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.Const;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.common.util.excel.BigExcelUtils;
import com.kingyee.common.util.excel.ExcelData;
import com.kingyee.me.domain.ClickData;
import com.kingyee.me.domain.ViewData;
import com.kingyee.me.entity.NhClick;
import com.kingyee.me.service.INhClickService;
import com.kingyee.me.service.INhVideoService;
import com.kingyee.me.service.INhViewService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.kingyee.common.util.TimeUtil.FORMAT_DATETIME_FULL;

/**
 * @Author BZH
 * @Description 视频浏览/点赞记录管理
 * @Date 2020/3/18 10:23
 * @Param
 * @return
 **/
@Controller
@RequestMapping(value = "/admin/videoLog")
public class AdminVideoLogController {

    private static final Logger logger = LoggerFactory.getLogger(AdminVideoLogController.class);
    @Autowired
    private INhClickService clickService;

    @Autowired
    private INhViewService viewService;

    @Autowired
    private INhVideoService videoService;

    @RequestMapping(value = {"/list"})
    public String list() {
        return "admin/videoLog/logList";
    }

    /**
     * 导出视频浏览，点赞记录
     *
     * @param mm
     * @param type 1.浏览 2.点赞
     * @param videoId
     * @param beginTime yyyy-MM-dd
     * @param endTime   yyyy-MM-dd
     * @return
     */

    @RequestMapping(value = "/exportLog", method = RequestMethod.GET)
    public String exportLog(ModelMap mm,Integer type,Long videoId,String beginTime, String endTime,HttpServletResponse response) {
      try{
          String filename = "";
          String endTimes = endTime+" 23:59:59";
          ExcelData excelData = new ExcelData();
          if(type==1) {
              List<ViewData> vList = viewService.createViewData(videoId,TimeUtil.stringToLong(beginTime),TimeUtil.stringToLong(endTimes,FORMAT_DATETIME_FULL));
              //明细
              filename = "视频浏览记录导出";
              excelData = viewService.createNewReport(filename, vList);
          }else if(type==2){
              List<ClickData> cList = clickService.createClickData(videoId,TimeUtil.stringToLong(beginTime),TimeUtil.stringToLong(endTimes,FORMAT_DATETIME_FULL));
              //明细
              filename = "视频点赞记录导出";
              excelData = clickService.createNewReport(filename, cList);
          }
        if (StringUtils.isNotEmpty(beginTime)) {
            filename += "-" + beginTime;
            if (StringUtils.isNotEmpty(endTime)) {
                filename += "-" + endTime;
            } else {
                filename += "_至" + TimeUtil.longToString(System.currentTimeMillis(), TimeUtil.FORMAT_DATE);
            }
        } else {
            if (StringUtils.isNotEmpty(endTime)) {
                filename += "-" + endTime;
            } else {
                filename += "_至" + TimeUtil.longToString(System.currentTimeMillis(), TimeUtil.FORMAT_DATE);
            }
        }
          filename += ".xlsx";
          BigExcelUtils.exportExcel(response, filename, excelData);
          mm.addAttribute("videoId",videoId);
          mm.addAttribute("beginTime",beginTime);
          mm.addAttribute("endTime",endTime);
    } catch (Exception e) {
        e.printStackTrace();
        logger.error("导出出错", e);
        }
        return "admin/videoLog/logList";
    }


    @ResponseBody
    @RequestMapping(value = "/listAjax", method = RequestMethod.GET)
    public JsonNode list(Integer page, Integer limit) {
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = Const.ADMIN_ROWSPERPAGE_MORE;
        }
        IPage<NhClick> pageInfo = new Page<>(page, limit);
        return JacksonMapper.newCountInstance(pageInfo);
    }

}