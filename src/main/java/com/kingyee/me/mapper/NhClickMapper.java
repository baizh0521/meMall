package com.kingyee.me.mapper;

import com.kingyee.me.domain.ClickData;
import com.kingyee.me.entity.NhClick;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户点赞表 Mapper 接口
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-16
 */
public interface NhClickMapper extends BaseMapper<NhClick> {



    /**
     *
     * 根据条件查询点赞需要导出的数据
     * */
    List<ClickData> selectDataByCondition(@Param("videoId") Long videoId, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime);

}
