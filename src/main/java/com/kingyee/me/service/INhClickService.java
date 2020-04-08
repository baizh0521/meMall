package com.kingyee.me.service;

import com.kingyee.common.util.excel.ExcelData;
import com.kingyee.me.domain.ClickData;
import com.kingyee.me.entity.NhClick;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户点赞表 服务类
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-16
 */
public interface INhClickService extends IService<NhClick> {


    /**
     * 创建数据
     *
     * @param videoId 视频id
     * @param beginTime 浏览开始时间
     * @param endTime 浏览结束时间
     * @return 返回ExcelData数据
     */

    List<ClickData> createClickData(Long videoId, Long beginTime, Long endTime);

    /**
     * 创建点赞表导出数据
     *
     * @param fileName 导出表名称
     * @param list 点赞数据集合
     * @return 返回ExcelData数据
     */

    ExcelData createNewReport(String fileName, List<ClickData> list);

}
