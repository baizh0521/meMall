package com.kingyee.common.baidu.ueditor.upload;

import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Map;

public final class Base64Uploader {

    public static com.kingyee.common.baidu.ueditor.define.State save(String content, Map<String, Object> conf) {

        byte[] data = decode(content);

        long maxSize = ((Long) conf.get("maxSize")).longValue();

        if (!validSize(data, maxSize)) {
            return new com.kingyee.common.baidu.ueditor.define.BaseState(false, com.kingyee.common.baidu.ueditor.define.AppInfo.MAX_SIZE);
        }

        String suffix = com.kingyee.common.baidu.ueditor.define.FileType.getSuffix("JPG");

        String savePath = com.kingyee.common.baidu.ueditor.PathFormat.parse((String) conf.get("savePath"),
                (String) conf.get("filename"));

        savePath = savePath + suffix;
        String physicalPath = (String) conf.get("rootPath") + savePath;

        com.kingyee.common.baidu.ueditor.define.State storageState = com.kingyee.common.baidu.ueditor.upload.StorageManager.saveBinaryFile(data, physicalPath);

        if (storageState.isSuccess()) {
            storageState.putInfo("url", com.kingyee.common.baidu.ueditor.PathFormat.format(savePath));
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", "");
        }

        return storageState;
    }

    private static byte[] decode(String content) {
        return Base64.decodeBase64(content);
    }

    private static boolean validSize(byte[] data, long length) {
        return data.length <= length;
    }

}