package com.kingyee.me.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 视频表
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-13
 */
public class NhVideo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "nv_id", type = IdType.AUTO)
    private Long nvId;

    /**
     * 视频标题
     */
    private String nvVideoTitle;

    /**
     * 视频封面图片地址
     */
    private String nvVideoCoverPicUrl;

    /**
     * 视频地址
     */
    private String nvVideoUrl;

    /**
     * 保利威视频id
     */
    private String nvVideoVid;

    /**
     * 视频描述
     */
    private String nvVideoRemark;

    /**
     * 专家姓名
     */
    private String nvExpertName;

    /**
     * 专家头像地址
     */
    private String nvExpertImg;

    /**
     * 专家职称
     */
    private String nvExpertProfessionalTitle;

    /**
     * 专家头衔
     */
    private String nvExpertRank;

    /**
     * 专家信息描述
     */
    private String nvExpertInformation;

    /**
     * ppt文件夹名称
     */
    private String nvPptName;

    /**
     * ppt图片总数量
     */
    private Integer nvPptNum;

    /**
     * 点赞数量
     */
    private Integer nvClickNum;

    /**
     * 观看数量
     */
    private Integer nvViewNum;

    /**
     * 排序 越大越靠前
     */
    private Integer nvOrder;

    /**
     * 视频是否显示 0.不显示 1.显示
     */
    private Integer nvState;


    /**
     * 发布日期
     */
    private String nvPublishDate;

    /**
     * 视频创建时间
     */
    private Long nvCreateTime;



    public Long getNvId() {
        return nvId;
    }

    public void setNvId(Long nvId) {
        this.nvId = nvId;
    }

    public String getNvVideoTitle() {
        return nvVideoTitle;
    }

    public void setNvVideoTitle(String nvVideoTitle) {
        this.nvVideoTitle = nvVideoTitle;
    }

    public String getNvVideoCoverPicUrl() {
        return nvVideoCoverPicUrl;
    }

    public void setNvVideoCoverPicUrl(String nvVideoCoverPicUrl) {
        this.nvVideoCoverPicUrl = nvVideoCoverPicUrl;
    }

    public String getNvVideoUrl() {
        return nvVideoUrl;
    }

    public void setNvVideoUrl(String nvVideoUrl) {
        this.nvVideoUrl = nvVideoUrl;
    }

    public String getNvVideoVid() {
        return nvVideoVid;
    }

    public void setNvVideoVid(String nvVideoVid) {
        this.nvVideoVid = nvVideoVid;
    }

    public String getNvVideoRemark() {
        return nvVideoRemark;
    }

    public void setNvVideoRemark(String nvVideoRemark) {
        this.nvVideoRemark = nvVideoRemark;
    }

    public String getNvExpertName() {
        return nvExpertName;
    }

    public void setNvExpertName(String nvExpertName) {
        this.nvExpertName = nvExpertName;
    }

    public String getNvExpertImg() {
        return nvExpertImg;
    }

    public void setNvExpertImg(String nvExpertImg) {
        this.nvExpertImg = nvExpertImg;
    }

    public String getNvExpertInformation() {
        return nvExpertInformation;
    }

    public void setNvExpertInformation(String nvExpertInformation) {
        this.nvExpertInformation = nvExpertInformation;
    }

    public String getNvPptName() {
        return nvPptName;
    }

    public void setNvPptName(String nvPptName) {
        this.nvPptName = nvPptName;
    }

    public Integer getNvPptNum() {
        return nvPptNum;
    }

    public void setNvPptNum(Integer nvPptNum) {
        this.nvPptNum = nvPptNum;
    }

    public Integer getNvClickNum() {
        return nvClickNum;
    }

    public void setNvClickNum(Integer nvClickNum) {
        this.nvClickNum = nvClickNum;
    }

    public Integer getNvOrder() {
        return nvOrder;
    }

    public void setNvOrder(Integer nvOrder) {
        this.nvOrder = nvOrder;
    }

    public Integer getNvState() {
        return nvState;
    }

    public void setNvState(Integer nvState) {
        this.nvState = nvState;
    }

    public String getNvPublishDate() {
        return nvPublishDate;
    }

    public void setNvPublishDate(String nvPublishDate) {
        this.nvPublishDate = nvPublishDate;
    }

    public Long getNvCreateTime() {
        return nvCreateTime;
    }

    public void setNvCreateTime(Long nvCreateTime) {
        this.nvCreateTime = nvCreateTime;
    }

    public String getNvExpertProfessionalTitle() {
        return nvExpertProfessionalTitle;
    }

    public void setNvExpertProfessionalTitle(String nvExpertProfessionalTitle) {
        this.nvExpertProfessionalTitle = nvExpertProfessionalTitle;
    }

    public String getNvExpertRank() {
        return nvExpertRank;
    }

    public void setNvExpertRank(String nvExpertRank) {
        this.nvExpertRank = nvExpertRank;
    }

    public Integer getNvViewNum() {
        return nvViewNum;
    }

    public void setNvViewNum(Integer nvViewNum) {
        this.nvViewNum = nvViewNum;
    }

    @Override
    public String toString() {
        return "NhVideo{" +
        "nvId=" + nvId +
        ", nvVideoTitle=" + nvVideoTitle +
        ", nvVideoCoverPicUrl=" + nvVideoCoverPicUrl +
        ", nvVideoUrl=" + nvVideoUrl +
        ", nvVideoVid=" + nvVideoVid +
        ", nvVideoRemark=" + nvVideoRemark +
        ", nvExpertName=" + nvExpertName +
        ", nvExpertImg=" + nvExpertImg +
        ", nvExpertProfessionalTitle=" + nvExpertProfessionalTitle +
        ", nvExpertRank=" + nvExpertRank +
        ", nvExpertInformation=" + nvExpertInformation +
        ", nvPptName=" + nvPptName +
        ", nvPptNum=" + nvPptNum +
        ", nvClickNum=" + nvClickNum +
        ", nvViewNum=" + nvViewNum +
        ", nvOrder=" + nvOrder +
        ", nvState=" + nvState +
        ", nvPublishDate=" + nvPublishDate +
        ", nvCreateTime=" + nvCreateTime +
        "}";
    }
}
