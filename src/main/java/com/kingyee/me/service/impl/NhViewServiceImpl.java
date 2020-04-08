package com.kingyee.me.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.common.util.excel.ExcelData;
import com.kingyee.me.domain.ViewData;
import com.kingyee.me.entity.NhView;
import com.kingyee.me.mapper.NhViewMapper;
import com.kingyee.me.service.INhViewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kingyee.common.util.TimeUtil.FORMAT_DATETIME;


/**
 * <p>
 * 用户浏览表 服务实现类
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-16
 */
@Service
public class NhViewServiceImpl extends ServiceImpl<NhViewMapper, NhView> implements INhViewService {

    @Autowired
    private NhViewMapper mapper;

    @Override
    public List<ViewData> createViewData(Long videoId, Long beginTime, Long endTime) {
        return mapper.selectDataByCondition(videoId,beginTime,endTime);
    }

    @Override
    public ExcelData createNewReport(String fileName, List<ViewData> list) {
        ExcelData data = new ExcelData();
        data.setName(fileName);
        List<String> titles = Arrays.asList("医脉通ID","医生姓名","医院","科室", "第一次进入时间","页面停留时长（秒）","文章ID","文章标题","视频播放时长（秒）","用户来源");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList<>();
        for (ViewData log:list) {
            List<Object> row = new ArrayList<>();
            row.add(log.getNvMedicineId());
            row.add(StringUtils.isNotEmpty(log.getNuLoginName())?log.getNuLoginName():"");
            row.add(StringUtils.isNotEmpty(log.getNuHospital())?log.getNuHospital():"");
            row.add(StringUtils.isNotEmpty(log.getNuDept())?log.getNuDept():"");
            row.add(TimeUtil.longToString(log.getNvFirstTime(),FORMAT_DATETIME));
            row.add(log.getNvStayTime()!=null?log.getNvStayTime():0L);
            row.add(log.getNvVideoId());
            row.add(StringUtils.isNotEmpty(log.getNvVideoTitle())?log.getNvVideoTitle():"");
            row.add(log.getNvPlayTime()!=null?log.getNvPlayTime():0L);
            row.add(StringUtils.isNotEmpty(log.getNvSource())?log.getNvSource():"");
            rows.add(row);
        }
        data.setRows(rows);
        return data;
    }

    @Override
    public Long getFirstTimeById(Long videoId,Long userId) {
        QueryWrapper<NhView> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(videoId!=null, NhView::getNvVideoId, videoId).eq(userId!=null,NhView::getNvUserId,userId);
        queryWrapper.orderByAsc("nv_create_time");
        NhView nhView = getOne(queryWrapper,false);
        if(nhView!=null){
            return nhView.getNvFirstTime();
        }else {
            return System.currentTimeMillis();
        }
    }
}
