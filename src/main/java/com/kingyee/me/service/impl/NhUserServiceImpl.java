package com.kingyee.me.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kingyee.common.util.http.HttpUtil;
import com.kingyee.me.common.security.EmtUtil;
import com.kingyee.me.common.security.NhUserUtil;
import com.kingyee.me.entity.NhUser;
import com.kingyee.me.mapper.NhUserMapper;
import com.kingyee.me.service.INhUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.kingyee.me.common.security.NhUserUtil.COOKIE_SOURCE;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author baizhihao
 * @since 2020-03-13
 */
@Service
public class NhUserServiceImpl extends ServiceImpl<NhUserMapper, NhUser> implements INhUserService {

      /**
     * 根据医脉通id获取医脉通用户信息
     */
    @Override
    public NhUser getUserByMedliveId(Long medUserId, Long usId, HttpServletRequest request) {
        String[] userHashInfo = null;
        String userInfo = null;
        JSONObject json = null;
        // 计算用户加密信息
        userHashInfo = EmtUtil.hashUserInfo(medUserId);
        // fetch userInfo by API
        userInfo = HttpUtil.getMeliveByUserId(String.format(NhUserUtil.GET_USER_INFO, userHashInfo[0], userHashInfo[1]));
        json = JSONObject.parseObject(userInfo);
        boolean success = json.getString("success_msg").equals("success");
        if (success) {
            json = json.getJSONObject("data");
            NhUser user = new NhUser();
            if(usId!=null) {
                user = getById(usId);
                user.setNuUpdateTime(System.currentTimeMillis());
            }else {
                user.setNuCreateTime(System.currentTimeMillis());
            }
            String name = json.getString("name");//真实姓名
            if (StringUtils.isEmpty(name)) {
                name = json.getString("nick");//昵称
            }
            user.setNuLoginName(name);

            // 医院城市、级别编码
            String company = json.getString("company"); // JSON后无序
            if (!StringUtils.isEmpty(company) && !company.equals("[]")) {

                // 正则获取医院信息
                company = userInfo.replaceAll("\"|\\s", "").replaceAll(".*company:\\{([^\\}]*)\\}.*", "$1");
                String[] infos = company.split(",");
                if (infos.length >= 4) {
                    String hos = EmtUtil.unicodeToString(infos[3].split(":")[1]);
                    user.setNuHospital(hos);
                }
            }
            // 科室编码
            String profession = json.getString("profession"); // JSON后无序
            if (!StringUtils.isEmpty(profession) && !profession.equals("[]")) {
                // 正则获取专业信息
                profession = userInfo.replaceAll("\"|\\s", "").replaceAll(".*profession:\\{([^\\}]*)\\}.*", "$1");
                String[] infos = profession.split(",");
                String ks = EmtUtil.unicodeToString(infos[0].split(":")[1]);
                user.setNuDept(ks);
            }
            String nuCertifyFlg = json.getString("certify_flg");
            String isCertifing = json.getString("is_certifing");
            if(StringUtils.isNotEmpty(nuCertifyFlg)||"Y".equals(isCertifing)){
                user.setNuCertifyFlg(1);
            }else {
                user.setNuCertifyFlg(0);
            }
            user.setNuSource(NhUserUtil.getCookie(request,COOKIE_SOURCE)!=null?NhUserUtil.getCookie(request,COOKIE_SOURCE):"");
            user.setNuMedicineId(medUserId.toString());
            user.setNuUpdateTime(System.currentTimeMillis());
            saveOrUpdate(user);
            return user;
        }
        return null;
    }

    @Override
    public NhUser getLocalUserByMedliveId(String medUserId) {
        QueryWrapper<NhUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(!StringUtils.isEmpty(medUserId), NhUser::getNuMedicineId, medUserId);
        return getOne(queryWrapper);
    }

    @Override
    public Boolean getCertifyStateByMedliveId(String medUserId) {
        String getUrl = NhUserUtil.MEDLIVE_CERTIFYIF_URL+"?user_id="+medUserId;
        Boolean b = false;
        String str = HttpUtil.getMeliveByUserId(getUrl);
        JSONObject json = JSONObject.parseObject(str);
        boolean success = json.getString("result_code").equals("00000");
        if(success){
            json = json.getJSONObject("data");
            String isCertify = json.getString("is_certify");
            if(isCertify.equals("Y")){
                b = true;
            }
        }
        return b;
    }
}
