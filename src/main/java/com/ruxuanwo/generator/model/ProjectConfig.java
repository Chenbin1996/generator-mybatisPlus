package com.ruxuanwo.generator.model;

/**
 * @author ChenBin
 * 包路径配置类
 */
public class ProjectConfig {
    /**
     * 包名 例如com.uhope.rl.demo
     */
    private String packAge;
    /**
     * Domain所在包
     */
    private String domain;
    /**
     * Mapper所在包
     */
    private String mapper;
    /**
     * Service所在包
     */
    private String service;
    /**
     * Service实现类所在包
     */
    private String serviceImpl;
    /**
     * 数据传输类所在包
     */
    private String dto;
    /**
     * 路由层所在包
     */
    private String controller;
    /**
     * 工具类所在包
     */
    private String util;
    /**
     * 配置类所在包
     */
    private String config;
    /**
     * 全局异常处理所在宝
     */
    private String exception;
    /**
     * 过滤器所在包
     */
    private String filter;
    /**
     * 功能模块所在包
     */
    private String core;
    /**
     * pom文件的groupId
     */
    public String groupId;

    public ProjectConfig(String packAge) {
        this.packAge = packAge;
        this.domain = packAge + ".domain";
        this.mapper = packAge + ".mapper";
        this.service = packAge + ".service";
        this.serviceImpl = service + ".impl";
        this.dto = packAge + ".dto";
        this.controller = packAge + ".controller";
        this.util = packAge + ".utils";
        this.config = packAge + ".config";
        this.exception = packAge + ".exception";
        this.filter = packAge + ".filter";
        this.core = packAge + ".core";
        this.groupId = packAge.substring(0, packAge.lastIndexOf("."));
    }

    public String getPackAge() {
        return packAge;
    }

    public void setPackAge(String packAge) {
        this.packAge = packAge;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceImpl() {
        return serviceImpl;
    }

    public void setServiceImpl(String serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    public String getDto() {
        return dto;
    }

    public void setDto(String dto) {
        this.dto = dto;
    }

    public String getUtil() {
        return util;
    }

    public void setUtil(String util) {
        this.util = util;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
