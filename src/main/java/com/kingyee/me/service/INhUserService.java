package com.kingyee.me.service;

import com.kingyee.me.entity.NhUser;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-13
 */
public interface INhUserService extends IService<NhUser> {

    /**
     * 查询用户的医脉通信息
     *
     * @param medUserId 用户的医脉通id
     * @param usId 用户id
     * @return NhUser
     */
    NhUser getUserByMedliveId(Long medUserId, Long usId, HttpServletRequest request);

    /**
     * 根据医脉通id查询用户的本地信息
     *
     * @param medUserId 医脉通id
     * @return NhUser
     */
    NhUser getLocalUserByMedliveId(String medUserId);

    /**
     * 根据医脉通id判断用户是否认证
     *
     * @param medUserId 医脉通id
     * @return
     */
    Boolean getCertifyStateByMedliveId(String medUserId);

}
