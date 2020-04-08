package com.kingyee.me.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-13
 */
public class SysUser implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "nb_id", type = IdType.AUTO)
    private Long nbId;

    /**
     * 用户名
     */
    private String nbLoginName;

    /**
     * 密码
     */
    private String nbPassword;

    /**
     * 创建时间
     */
    private Long nbCreateTime;

    /**
     * 修改时间
     */
    private Long nbUpdateTime;


    public Long getNbId() {
        return nbId;
    }

    public void setNbId(Long nbId) {
        this.nbId = nbId;
    }

    public String getNbLoginName() {
        return nbLoginName;
    }

    public void setNbLoginName(String nbLoginName) {
        this.nbLoginName = nbLoginName;
    }

    public String getNbPassword() {
        return nbPassword;
    }

    public void setNbPassword(String nbPassword) {
        this.nbPassword = nbPassword;
    }

    public Long getNbCreateTime() {
        return nbCreateTime;
    }

    public void setNbCreateTime(Long nbCreateTime) {
        this.nbCreateTime = nbCreateTime;
    }

    public Long getNbUpdateTime() {
        return nbUpdateTime;
    }

    public void setNbUpdateTime(Long nbUpdateTime) {
        this.nbUpdateTime = nbUpdateTime;
    }

    @Override
    public String toString() {
        return "SysUser{" +
        "nbId=" + nbId +
        ", nbLoginName=" + nbLoginName +
        ", nbPassword=" + nbPassword +
        ", nbCreateTime=" + nbCreateTime +
        ", nbUpdateTime=" + nbUpdateTime +
        "}";
    }
}
