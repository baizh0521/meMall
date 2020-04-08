package com.kingyee.me.service;

import com.kingyee.common.util.excel.ExcelData;
import com.kingyee.me.domain.ViewData;
import com.kingyee.me.entity.NhView;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户浏览表 服务类
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-16
 */
public interface INhViewService extends IService<NhView> {

    /**
     * 创建数据
     *
     * @param videoId 视频id
     * @param beginTime 浏览开始时间
     * @param endTime 浏览结束时间
     * @return 返回ExcelData数据
     */

    List<ViewData> createViewData(Long videoId, Long beginTime, Long endTime);

    /**
     * 创建浏览表导出数据
     *
     * @param fileName 导出表名称
     * @param list 浏览数据集合
     * @return 返回ExcelData数据
     */
    ExcelData createNewReport(String fileName, List<ViewData> list);


    /**
     * 查询用户最早查看某视频页面时间
     *
     * @param videoId 导出表名称
     * @return Long
     */
    Long getFirstTimeById(Long videoId,Long userId);

}
