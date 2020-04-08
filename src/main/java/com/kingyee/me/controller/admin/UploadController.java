package com.kingyee.me.controller.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.Const;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.spring.mvc.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传文件
 *
 * @author fanyongqian
 *         2016年10月31日
 */
@Controller
@RequestMapping(value = "/admin/upload/")
public class UploadController {
    private final static Logger logger = LoggerFactory.getLogger(UploadController.class);

    /**
     * 上传缩略图
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"uploadPicPc"}, method = RequestMethod.POST)
    public JsonNode uploadPic(MultipartFile file, String type) {
        String path = null;
        String folderPath = null;
        folderPath = Const.PIC_PATH;
        path = WebUtil.getRealPath(folderPath);
        String fileName = file.getOriginalFilename();
        //重命名
        String ext = this.getSuffix(fileName);
        fileName = new Date().getTime() + "." + ext;
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        //保存
        try {
            file.transferTo(targetFile);
            /*String ext = this.getSuffix(fileName);
            destFileName = new Date().getTime() + "." + ext;
            ImageUtil.resizeFixedWidth(targetFile.getAbsolutePath(), path + File.separator + destFileName, 200, 1F);
            targetFile.delete();*/
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传图片时出错。", e);
            return JacksonMapper.newErrorInstance("上传图片时出错");
        }
        return JacksonMapper.newDataInstance(folderPath + fileName);
    }

    // 获取文件后缀
    private String getSuffix(String filename) {
        String suffix = "";
        int pos = filename.lastIndexOf('.');
        if (pos > 0 && pos < filename.length() - 1) {
            suffix = filename.substring(pos + 1);
        }
        return suffix;
    }

    /**
     * 上传多媒体文件：音频 视频
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"uploadFile"}, method = RequestMethod.POST)
    public JsonNode uploadFile(MultipartFile file, String type) {
        if (file == null) {
            return JacksonMapper.newErrorInstance("请上传文件");
        }
        String fileName = file.getOriginalFilename();
        String oldFileName = fileName.substring(0, fileName.lastIndexOf('.'));
        //重命名
        String ext = this.getSuffix(fileName);

        String folderPath = Const.FILE_PATH;
        if (StringUtils.isNotEmpty(type)) {
            if ("audio".equalsIgnoreCase(type)) {
                folderPath = Const.AUDIO_PATH;
            } else if ("video".equalsIgnoreCase(type)) {
                folderPath = Const.VIDEO_PATH;
                if (!"mp4".equalsIgnoreCase(ext)) {
                    return JacksonMapper.newErrorInstance("请上传MP4格式的视频！");
                }
            }
        }
        String path = WebUtil.getRealPath(folderPath);
        fileName = new Date().getTime() + "";
        File targetFile = new File(path, fileName + "-o." + ext);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        //返回路径
        String returnPath = folderPath + fileName + "-o." + ext;
        //保存
        try {
            file.transferTo(targetFile);
            /*String ext = this.getSuffix(fileName);
            destFileName = new Date().getTime() + "." + ext;
            ImageUtil.resizeFixedWidth(targetFile.getAbsolutePath(), path + File.separator + destFileName, 200, 1F);
            targetFile.delete();*/

            if ("video".equals(type)) {
                // 原始视频文件 TODO
                /*String videoRealPath = path + "/" + fileName + "-o." + ext;
                // 压缩后视频文件
				String compressVideoRealPath = path + "/" + fileName + ".mp4";
				// 压缩转换视频
				String line = Const.FFMPEG_PATH + " -y -i " + videoRealPath
						+ " -ar 44100 -vcodec libx264 " + compressVideoRealPath;
				boolean ret = runCommand(line);
				if (ret) {
					returnPath = Const.VIDEO_PATH + fileName + ".mp4";// 视频
				} else {
					returnPath = Const.VIDEO_PATH + fileName + "-o." + ext;// 视频
				}*/
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传多媒体文件时出错。", e);
            return JacksonMapper.newErrorInstance("上传多媒体文件时出错。");
        }
        Map map = new HashMap();
        map.put("returnPath", returnPath);
        map.put("oldFileName", oldFileName);
        return JacksonMapper.newDataInstance(map);
    }
}