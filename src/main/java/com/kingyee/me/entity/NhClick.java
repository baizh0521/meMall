package com.kingyee.me.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 用户点赞表
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-16
 */
public class NhClick implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "nc_id", type = IdType.AUTO)
    private Long ncId;

    /**
     * 用户id
     */
    private Long ncUserId;

    /**
     * 医脉通用户id
     */
    private String ncMedicineId;

    /**
     * 视频id
     */
    private Long ncVideoId;

    /**
     * 视频标题
     */
    private String ncVideoTitle;

    /**
     * 第一次进入时间
     */
    private Long ncFirstTime;

    /**
     * 创建时间
     */
    private Long ncCreateTime;


    public Long getNcId() {
        return ncId;
    }

    public void setNcId(Long ncId) {
        this.ncId = ncId;
    }

    public Long getNcUserId() {
        return ncUserId;
    }

    public void setNcUserId(Long ncUserId) {
        this.ncUserId = ncUserId;
    }

    public String getNcMedicineId() {
        return ncMedicineId;
    }

    public void setNcMedicineId(String ncMedicineId) {
        this.ncMedicineId = ncMedicineId;
    }

    public Long getNcVideoId() {
        return ncVideoId;
    }

    public void setNcVideoId(Long ncVideoId) {
        this.ncVideoId = ncVideoId;
    }

    public String getNcVideoTitle() {
        return ncVideoTitle;
    }

    public void setNcVideoTitle(String ncVideoTitle) {
        this.ncVideoTitle = ncVideoTitle;
    }

    public Long getNcFirstTime() {
        return ncFirstTime;
    }

    public void setNcFirstTime(Long ncFirstTime) {
        this.ncFirstTime = ncFirstTime;
    }

    public Long getNcCreateTime() {
        return ncCreateTime;
    }

    public void setNcCreateTime(Long ncCreateTime) {
        this.ncCreateTime = ncCreateTime;
    }

    @Override
    public String toString() {
        return "NhClick{" +
        "ncId=" + ncId +
        ", ncUserId=" + ncUserId +
        ", ncMedicineId=" + ncMedicineId +
        ", ncVideoId=" + ncVideoId +
        ", ncVideoTitle=" + ncVideoTitle +
        ", ncFirstTime=" + ncFirstTime +
        ", ncCreateTime=" + ncCreateTime +
        "}";
    }
}
