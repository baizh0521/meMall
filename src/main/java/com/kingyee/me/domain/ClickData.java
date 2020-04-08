package com.kingyee.me.domain;

import com.kingyee.me.entity.NhClick;

/**
 * @Author baizh
 * @Description
 * @Date 2020/4/2
 */
public class ClickData extends NhClick {

    /**
     * 用户名
     */
    private String nuLoginName;

    /**
     * 医院
     */
    private String nuHospital;

    /**
     * 科室
     */
    private String nuDept;

    public String getNuLoginName() {
        return nuLoginName;
    }

    public void setNuLoginName(String nuLoginName) {
        this.nuLoginName = nuLoginName;
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
}
