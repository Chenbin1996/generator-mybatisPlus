package com.ruxuanwo.generator.generator;

import com.alibaba.fastjson.JSONArray;
import com.ruxuanwo.generator.model.DbConfig;
import com.ruxuanwo.generator.model.Model;
import com.ruxuanwo.generator.model.PathConfig;
import com.ruxuanwo.generator.model.ProjectConfig;
import com.ruxuanwo.generator.utils.CodeUtil;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenBin
 * @Date: 2018/6/6/0006 16:54
 */
public class CodeGenerator {

    private static TemplateEngine templateEngine;

    static {
        templateEngine = new TemplateEngine();
    }

    /**
     * 解析传入的二维JSON数组，转换成相应的值
     *
     * @param jsonData      二维JSON数组
     * @param dbConfig      数据库配置信息
     * @param fileName      文件夹名称
     * @param projectConfig 项目包的文件夹名称
     * @param author        创建人
     */
    public static void getCode(String jsonData, DbConfig dbConfig, String fileName, ProjectConfig projectConfig,
                               String author, String type) {
        List<Model> models = JSONArray.parseArray(jsonData, Model.class);
        CodeUtil.setLinuxFile(fileName);
        for (Model model : models) {
            Map<String, Object> params = CodeUtil.setParam(model, author, projectConfig, dbConfig, fileName);
            writer(params, PathConfig.domain, "Domain.java.ftl", true);
            writer(params, PathConfig.mapper, "Mapper.java.ftl", true);
            writer(params, PathConfig.xml, "Mapper.xml.ftl", true);
            writer(params, PathConfig.dto, "DTO.java.ftl", true);
            writer(params, PathConfig.service, "Service.java.ftl", true);
            writer(params, PathConfig.serviceImpl, "ServiceImpl.java.ftl", true);
            writer(params, PathConfig.controller, "Controller.java.ftl", true);
        }
        if ("1".equals(type)){
            Map<String, Object> params = CodeUtil.setParam(null, author, projectConfig, dbConfig, fileName);
            writer(params, PathConfig.core, "BaseService.java.ftl", false);
            writer(params, PathConfig.core, "BaseServiceImpl.java.ftl", false);
            writer(params, PathConfig.config, "DruidDBConfig.java.ftl", false);
            writer(params, PathConfig.config, "MybatisPlusConfig.java.ftl", false);
            writer(params, PathConfig.config, "WebMvcConfig.java.ftl", false);
            writer(params, PathConfig.exception, "GlobalExceptionHandler.java.ftl", false);
            writer(params, PathConfig.util, "RequestUtil.java.ftl", false);
            writer(params, PathConfig.util, "ResponseMsgUtil.java.ftl", false);
            writer(params, PathConfig.util, "Result.java.ftl", false);
            writer(params, PathConfig.packAges, "Application.java.ftl", false);
            writer(params, PathConfig.RESOURCES_PATH + "/", "application.properties.ftl", false);
            writer(params, PathConfig.RESOURCES_PATH + "/", "application-dev.properties.ftl", false);
            writer(params, "/", "gitignore.gitignore.ftl", false);
            writer(params, "/", "pom.xml.ftl", false);
        }
    }


    private static void writer(Map<String, Object> params, String path, String template, boolean isWriterName){
        String tempFileName = template.substring(0, template.lastIndexOf("."));
        tempFileName = tempFileName.contains("Domain") || tempFileName.contains("gitignore") ? tempFileName.substring(tempFileName.indexOf(".")) : tempFileName;
        String outputFile = params.get("fileName") + path + (isWriterName ? params.get("domainNameUpperCamel") + tempFileName : tempFileName);
        templateEngine.writer(params, template, outputFile);
    }



}
