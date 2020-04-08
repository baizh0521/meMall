package com.kingyee.common.util;


public class PropertyConst {


    /**
     * 系统环境，开发(dev)OR测试(test)OR正式(product)
     */
    public static String ENVIRONMENT = PropertyUtil.getPropertyValue("environment");
    /**
     * 图片上传路径
     */
    public static String PIC_PATH = PropertyUtil.getPropertyValue("pic.path");
    /**
     * 药品缩略图上传路径路径
     */
    public static String DRUGS_PIC_PATH = PropertyUtil.getPropertyValue("drugs.pic.path");
    /***
     * 头像
     */
    public static String PORTRAIT_PIC_PATH = PropertyUtil.getPropertyValue("portrait.pic.path");
    /**
     * 音频上传路径
     */
    public static String AUDIO_PATH = PropertyUtil.getPropertyValue("audio.path");
    /**
     * 视频上传路径
     */
    public static String VIDEO_PATH = PropertyUtil.getPropertyValue("video.path");
    /**
     * PDF上传路径
     */
    public static String PDF_PATH = PropertyUtil.getPropertyValue("pdf.path");
    /**
     * PPT上传路径
     */
    public static String PPT_PATH = PropertyUtil.getPropertyValue("ppt.path");
    /**
     * PPT备份路径
     */
    public static String PPT_BACKUPS_IMAGE_PATH = PropertyUtil.getPropertyValue("ppt.backup.img.path");
    /**
     * PPT转成的图片路径
     */
    public static String PPT_IMAGE_PATH = PropertyUtil.getPropertyValue("ppt.img.path");
    /**
     * 缓存类型
     */
    public static String CACHE_TYPE = PropertyUtil.getPropertyValue("cache_type");
    /**
     * 项目地址
     */
    public static String PROJECT_URL = PropertyUtil.getPropertyValue("project.url");

    /**
     * 系统邮箱设置
     */
    public static String EMAIL_NAME = PropertyUtil.getPropertyValue("email.name");
    public static String EMAIL_PWD = PropertyUtil.getPropertyValue("email.pwd");
    public static String EMAIL_ADDRESS = PropertyUtil.getPropertyValue("email.address");
    public static String EMAIL_SMTP = PropertyUtil.getPropertyValue("email.smtp");


}
