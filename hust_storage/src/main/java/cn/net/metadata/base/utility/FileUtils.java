/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package cn.net.metadata.base.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * 文件操作工具类
 * 实现文件的创建、删除、复制、压缩、解压以及目录的创建、删除、复制、压缩解压等功能
 *
 * @author ThinkGem
 * @version 2013-06-21
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 创建单个文件
     *
     * @param descFileName 文件名，包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createFile(String descFileName) {
        File file = new File(descFileName);
        if (file.exists()) {
            log.debug("文件 " + descFileName + " 已存在!");
            return false;
        }
        if (descFileName.endsWith(File.separator)) {
            log.debug(descFileName + " 为目录，不能创建目录!");
            return false;
        }
        if (!file.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                log.debug("创建文件所在的目录失败!");
                return false;
            }
        }

        // 创建文件
        try {
            if (file.createNewFile()) {
                log.debug(descFileName + " 文件创建成功!");
                return true;
            } else {
                log.debug(descFileName + " 文件创建失败!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(descFileName + " 文件创建失败!");
            return false;
        }

    }

    /**
     * 将内容写入文件
     *
     * @param content
     * @param filePath
     */
    public static void writeFile(String content, String filePath) {
        try {
            if (FileUtils.createFile(filePath)) {
                FileWriter fileWriter = new FileWriter(filePath, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(content);
                bufferedWriter.close();
                fileWriter.close();
            } else {
                log.info("生成失败，文件已存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
