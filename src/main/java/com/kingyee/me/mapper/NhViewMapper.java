package com.kingyee.me.mapper;

import com.kingyee.me.domain.ViewData;
import com.kingyee.me.entity.NhView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户浏览表 Mapper 接口
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-16
 */
public interface NhViewMapper extends BaseMapper<NhView> {
    /**
     *
     * 创建用户浏览导出数据
     * */

    List<ViewData> selectDataByCondition(@Param("videoId") Long videoId,@Param("beginTime") Long beginTime,@Param("endTime") Long endTime);

}
