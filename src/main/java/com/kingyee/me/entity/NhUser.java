package com.kingyee.me.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-13
 */
public class NhUser implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "nu_id", type = IdType.AUTO)
    private Long nuId;

    /**
     * 用户名
     */
    private String nuLoginName;

    /**
     * 医脉通用户id
     */
    private String nuMedicineId;

    /**
     * 头像
     */
    private String nuHeadImg;

    /**
     * 医院
     */
    private String nuHospital;

    /**
     * 科室
     */
    private String nuDept;

    /**
     * 用户注册来源
     */
    private String nuSource;

    /**
     * 用户注册来源
     */
    private Integer nuCertifyFlg;

    /**
     * 创建时间
     */
    private Long nuCreateTime;

    /**
     * 修改时间
     */
    private Long nuUpdateTime;


    public Long getNuId() {
        return nuId;
    }

    public void setNuId(Long nuId) {
        this.nuId = nuId;
    }

    public String getNuLoginName() {
        return nuLoginName;
    }

    public void setNuLoginName(String nuLoginName) {
        this.nuLoginName = nuLoginName;
    }

    public String getNuMedicineId() {
        return nuMedicineId;
    }

    public void setNuMedicineId(String nuMedicineId) {
        this.nuMedicineId = nuMedicineId;
    }

    public String getNuHeadImg() {
        return nuHeadImg;
    }

    public void setNuHeadImg(String nuHeadImg) {
        this.nuHeadImg = nuHeadImg;
    }

    public String getNuHospital() {
        return nuHospital;
    }

    public void setNuHospital(String nuHospital) {
        this.nuHospital = nuHospital;
    }

    public String getNuDept() {
        return nuDept;
    }

    public void setNuDept(String nuDept) {
        this.nuDept = nuDept;
    }

    public String getNuSource() {
        return nuSource;
    }

    public void setNuSource(String nuSource) {
        this.nuSource = nuSource;
    }

    public Integer getNuCertifyFlg() {
        return nuCertifyFlg;
    }

    public void setNuCertifyFlg(Integer nuCertifyFlg) {
        this.nuCertifyFlg = nuCertifyFlg;
    }

    public Long getNuCreateTime() {
        return nuCreateTime;
    }

    public void setNuCreateTime(Long nuCreateTime) {
        this.nuCreateTime = nuCreateTime;
    }

    public Long getNuUpdateTime() {
        return nuUpdateTime;
    }

    public void setNuUpdateTime(Long nuUpdateTime) {
        this.nuUpdateTime = nuUpdateTime;
    }

    @Override
    public String toString() {
        return "NhUser{" +
        "nuId=" + nuId +
        ", nuLoginName=" + nuLoginName +
        ", nuMedicineId=" + nuMedicineId +
        ", nuHeadImg=" + nuHeadImg +
        ", nuHospital=" + nuHospital +
        ", nuDept=" + nuDept +
        ", nuSource=" + nuSource +
        ", nuCreateTime=" + nuCreateTime +
        ", nuUpdateTime=" + nuUpdateTime +
        "}";
    }
}
