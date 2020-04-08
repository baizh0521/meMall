package com.kingyee.me;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: 自定义配置
 * @author: SHF
 * @create: 2019-10-11 13:31
 **/
@Component
@ConfigurationProperties(prefix = "custom")
public class MyCustomConfig {

    /**
     * 版本
     */
    private String version;

    /**
     * 版本
     */
    private String cacheType;


    /**
     * 上传文件绝对路径
     */
    private String uploadAbsoluteFilePath;

    /**
     * PPT图片文件夹所在路径
     */
    private String pptFilePath;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 域名
     */
    private String domain;

    /**
     * 端口
     */
    private String port;

    @Value("${server.port}")
    private String serverPort;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCacheType() {
        return cacheType;
    }

    public void setCacheType(String cacheType) {
        this.cacheType = cacheType;
    }

    public String getUploadAbsoluteFilePath() {
        return uploadAbsoluteFilePath;
    }

    public void setUploadAbsoluteFilePath(String uploadAbsoluteFilePath) {
        this.uploadAbsoluteFilePath = uploadAbsoluteFilePath;
    }


    public String getPptFilePath() {
        return pptFilePath;
    }

    public void setPptFilePath(String pptFilePath) {
        this.pptFilePath = pptFilePath;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }
}
