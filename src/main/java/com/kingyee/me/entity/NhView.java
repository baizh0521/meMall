package com.kingyee.me.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 用户浏览表
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-16
 */
public class NhView implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "nv_id", type = IdType.AUTO)
    private Long nvId;

    /**
     * 用户id
     */
    private Long nvUserId;

    /**
     * 医脉通用户id
     */
    private String nvMedicineId;

    /**
     * 用户观看来源
     */
    private String nvSource;

    /**
     * 视频id
     */
    private Long nvVideoId;

    /**
     * 视频标题
     */
    private String nvVideoTitle;

    /**
     * 第一次进入时间
     */
    private Long nvFirstTime;

    /**
     * 页面停留时长
     */
    private Long nvStayTime;

    /**
     * 视频观看时长
     */
    private Long nvViewTime;

    /**
     * 视频播放时长
     */
    private Long nvPlayTime;

    /**
     * 创建时间
     */
    private Long nvCreateTime;


    public Long getNvId() {
        return nvId;
    }

    public void setNvId(Long nvId) {
        this.nvId = nvId;
    }

    public Long getNvUserId() {
        return nvUserId;
    }

    public void setNvUserId(Long nvUserId) {
        this.nvUserId = nvUserId;
    }

    public String getNvMedicineId() {
        return nvMedicineId;
    }

    public void setNvMedicineId(String nvMedicineId) {
        this.nvMedicineId = nvMedicineId;
    }

    public String getNvSource() {
        return nvSource;
    }

    public void setNvSource(String nvSource) {
        this.nvSource = nvSource;
    }

    public Long getNvVideoId() {
        return nvVideoId;
    }

    public void setNvVideoId(Long nvVideoId) {
        this.nvVideoId = nvVideoId;
    }

    public String getNvVideoTitle() {
        return nvVideoTitle;
    }

    public void setNvVideoTitle(String nvVideoTitle) {
        this.nvVideoTitle = nvVideoTitle;
    }

    public Long getNvFirstTime() {
        return nvFirstTime;
    }

    public void setNvFirstTime(Long nvFirstTime) {
        this.nvFirstTime = nvFirstTime;
    }

    public Long getNvStayTime() {
        return nvStayTime;
    }

    public void setNvStayTime(Long nvStayTime) {
        this.nvStayTime = nvStayTime;
    }

    public Long getNvViewTime() {
        return nvViewTime;
    }

    public void setNvViewTime(Long nvViewTime) {
        this.nvViewTime = nvViewTime;
    }

    public Long getNvPlayTime() {
        return nvPlayTime;
    }

    public void setNvPlayTime(Long nvPlayTime) {
        this.nvPlayTime = nvPlayTime;
    }

    public Long getNvCreateTime() {
        return nvCreateTime;
    }

    public void setNvCreateTime(Long nvCreateTime) {
        this.nvCreateTime = nvCreateTime;
    }

    @Override
    public String toString() {
        return "NhView{" +
        "nvId=" + nvId +
        ", nvUserId=" + nvUserId +
        ", nvMedicineId=" + nvMedicineId +
        ", nvSource=" + nvSource +
        ", nvVideoId=" + nvVideoId +
        ", nvVideoTitle=" + nvVideoTitle +
        ", nvFirstTime=" + nvFirstTime +
        ", nvStayTime=" + nvStayTime +
        ", nvViewTime=" + nvViewTime +
        ", nvPlayTime=" + nvPlayTime +
        ", nvCreateTime=" + nvCreateTime +
        "}";
    }
}
