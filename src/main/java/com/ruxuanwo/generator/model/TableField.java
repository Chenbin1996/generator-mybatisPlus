package com.ruxuanwo.generator.model;

public class TableField {
    /**
     * 字段名
     */
    private String name;
    /**
     * 对应的Java字段名
     */
    private String propertyName;
    /**
     * 字段类型
     */
    private String type;
    /**
     * 对应的Java字段类型
     */
    private String propertyType;
    /**
     * 字段注释
     */
    private String comment;
    /**
     * 是否主键
     */
    private boolean isKey;
    /**
     * 主键类型（UUID，AUTO自增）
     */
    private String keyType;

    private String importPackage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean getIsKey() {
        return isKey;
    }

    public void setIsKey(boolean key) {
        isKey = key;
    }

    public String getImportPackage() {
        return importPackage;
    }

    public void setImportPackage(String importPackage) {
        this.importPackage = importPackage;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    @Override
    public String toString() {
        return "TableField{" +
                "name='" + name + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", type='" + type + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", comment='" + comment + '\'' +
                ", isKey=" + isKey +
                ", importPackage='" + importPackage + '\'' +
                ", keyType='" + keyType + '\'' +
                '}';
    }
}
