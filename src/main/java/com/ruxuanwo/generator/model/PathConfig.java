package com.ruxuanwo.generator.model;


import com.ruxuanwo.generator.utils.CodeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: ChenBin
 * @Date: 2018/6/6/0006 15:26
 */
public class PathConfig {
    /**
     * java文件路径
     */
    public static final String JAVA_PATH = "/src/main/java";
    /**
     * 资源文件路径
     */
    public static final String RESOURCES_PATH = "/src/main/resources";
    /**
     * 日期
     */
    public static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    /**
     * 项目包路径
     */
    public static String packAges;
    /**
     * 生成的Service存放路径
     */
    public static String service;
    /**
     * 生成的Service实现存放路径
     */
    public static String serviceImpl;
    /**
     * 实体类路径
     */
    public static String domain;
    /**
     * 生成的DTO存放路径
     */
    public static String dto;
    /**
     * 路由层所在包
     */
    public static String controller;
    /**
     * 工具类所在包
     */
    public static String util;
    /**
     * 配置类所在包
     */
    public static String config;
    /**
     * 全局异常处理所在包
     */
    public static String exception;
    /**
     * 过滤器所在包
     */
    public static String filter;
    /**
     * 功能模块所在包
     */
    public static String core;
    /**
     * mapper所在包
     */
    public static String mapper;
    /**
     * mapper xml所在包
     */
    public static String xml;


    public static ProjectConfig create(String packAge) {
        ProjectConfig projectConfig = new ProjectConfig(packAge);
        packAges = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getPackAge());
        service = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getService());
        serviceImpl = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getServiceImpl());
        domain = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getDomain());
        dto = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getDto());
        controller = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getController());
        util = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getUtil());
        config = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getConfig());
        exception = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getException());
        filter = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getFilter());
        core = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getCore());
        mapper = JAVA_PATH + CodeUtil.packageConvertPath(projectConfig.getMapper());
        xml = RESOURCES_PATH + "/mapper/";
        return projectConfig;
    }

    private PathConfig() {
    }
}
