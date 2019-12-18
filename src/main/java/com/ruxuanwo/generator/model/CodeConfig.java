package com.ruxuanwo.generator.model;


import com.ruxuanwo.generator.utils.ConfigUtil;

/**
 * @Author: ChenBin
 * @Date: 2018/6/13/0013 17:17
 */
public class CodeConfig {
    public static String CONFIG_URL;
    public static String ENCODING;
    public static String LOADER_PATH;
    public static String FILE_PATH;

    static {
        CONFIG_URL = ConfigUtil.getConfig().get("code.db.config.url");
        ENCODING = ConfigUtil.getConfig().get("code.encoding");
        LOADER_PATH = ConfigUtil.getConfig().get("code.loader.template.path");
        FILE_PATH = ConfigUtil.getConfig().get("code.file.path");
    }

}
