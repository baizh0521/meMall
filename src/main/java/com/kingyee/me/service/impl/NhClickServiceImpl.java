package com.kingyee.me.service.impl;

import com.kingyee.common.util.TimeUtil;
import com.kingyee.common.util.excel.ExcelData;
import com.kingyee.me.domain.ClickData;
import com.kingyee.me.entity.NhClick;
import com.kingyee.me.mapper.NhClickMapper;
import com.kingyee.me.service.INhClickService;
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
 * 用户点赞表 服务实现类
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-16
 */
@Service
public class NhClickServiceImpl extends ServiceImpl<NhClickMapper, NhClick> implements INhClickService {


    @Autowired
    private NhClickMapper mapper;

    @Override
    public List<ClickData> createClickData(Long videoId, Long beginTime, Long endTime) {
        return mapper.selectDataByCondition(videoId,beginTime,endTime);
    }

    @Override
    public ExcelData createNewReport(String fileName, List<ClickData> list) {
        ExcelData data = new ExcelData();
        data.setName(fileName);
        List<String> titles = Arrays.asList("医脉通ID","医生姓名","医院","科室", "第一次进入时间","文章ID","文章标题");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList<>();
        for (ClickData log:list) {
            List<Object> row = new ArrayList<>();
            row.add(StringUtils.isNotEmpty(log.getNcMedicineId())?log.getNcMedicineId():"");
            row.add(StringUtils.isNotEmpty(log.getNuLoginName())?log.getNuLoginName():"");
            row.add(StringUtils.isNotEmpty(log.getNuHospital())?log.getNuHospital():"");
            row.add(StringUtils.isNotEmpty(log.getNuDept())?log.getNuDept():"");
            row.add(TimeUtil.longToString(log.getNcFirstTime(),FORMAT_DATETIME));
            row.add(log.getNcVideoId());
            row.add(StringUtils.isNotEmpty(log.getNcVideoTitle())?log.getNcVideoTitle():"");
            rows.add(row);
        }
        data.setRows(rows);
        return data;
    }
}
