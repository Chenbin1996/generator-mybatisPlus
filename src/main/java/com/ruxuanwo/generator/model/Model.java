package com.ruxuanwo.generator.model;

public class Model {
    private String tableName;
    private String domainName;
    private String description;
    private String pkDataType;

    public Model() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPkDataType() {
        return pkDataType;
    }

    public void setPkDataType(String pkDataType) {
        this.pkDataType = pkDataType;
    }
}
