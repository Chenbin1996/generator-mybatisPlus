package com.ruxuanwo.generator.utils;


import com.google.common.base.CaseFormat;
import com.ruxuanwo.generator.model.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: ChenBin
 * @Date: 2018/6/6/0006 16:25
 */
public class CodeUtil {

    private CodeUtil(){}

    /**
     * 创建文件夹
     *
     * @param path 文件路径
     * @return
     */
    public static File getFile(String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static void setLinuxFile(String fileName) {
        File file = new File(fileName + PathConfig.JAVA_PATH);
        file.setWritable(true, false);
        if (!file.exists()) {
            file.mkdirs();
        }

        File file1 = new File(fileName + PathConfig.RESOURCES_PATH);
        file1.setWritable(true, false);
        if (!file1.exists()) {
            file1.mkdirs();
        }
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                // 先删除文件夹里面的文件
                delAllFile(path + "/" + tempList[i]);
                // 再删除空文件夹
                delFolder(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }

    /***
     * 删除文件夹
     *
     * @param folderPath 文件夹完整绝对路径
     */
    public static void delFolder(String folderPath) {
        try {
            // 删除完里面所有内容
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath;
            File myFilePath = new File(filePath);
            // 删除空文件夹
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 配置map
     *
     * @param model
     * @param author
     * @param projectConfig
     * @return
     */
    public static Map<String, Object> setParam(Model model, String author, ProjectConfig projectConfig, DbConfig dbConfig, String fileName) {
        dbConfig.setTableName(model == null ? "" : model.getTableName());
        List<TableField> tableFields = DataBaseUtil.getTableField(dbConfig);
        List<String> importPackage = new ArrayList<>();
        List<TableField> pkgs = tableFields.stream().filter(x -> x.getImportPackage() != null).collect(Collectors.toList());
        if (!pkgs.isEmpty()){
            importPackage = pkgs.stream().map(TableField::getImportPackage).distinct().collect(Collectors.toList());
        }
        String domainNameUpperCamel = "";
        String domainNameLowerCamel = "";
        String description = "";
        if (model != null){
            domainNameUpperCamel = StringUtils.isEmpty(model.getDomainName()) ? tableNameConvertUpperCamel(model.getTableName()) : model.getDomainName();
            domainNameLowerCamel = tableNameConvertLowerCamel(model.getTableName());
            description = StringUtils.isEmpty(model.getDescription()) ? "" : model.getDescription();
        }
        Map<String, Object> data = new HashMap<>(16);
        data.put("description", description);
        data.put("date", PathConfig.DATE);
        data.put("author", author);
        data.put("domainNameUpperCamel", domainNameUpperCamel);
        data.put("domainNameLowerCamel", domainNameLowerCamel);
        data.put("package", projectConfig);
        data.put("baseRequestMapping", StringUtils.isEmpty(domainNameUpperCamel) ? "" : domainNameConvertMappingPath(domainNameUpperCamel));
        data.put("dbConfig", dbConfig);
        data.put("tableName", model == null ? "" : model.getTableName());
        data.put("projectName", fileName.substring(fileName.lastIndexOf("/") + 1));
        data.put("fileName", fileName);
        data.put("serverErrorPath", "${server.error.path:${error.path:/error}}");
        data.put("fields", tableFields);
        data.put("importPackage", importPackage);
        return data;
    }


    public static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

    public static String tableNameConvertLowerCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    public static String tableNameConvertUpperCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());
    }

    public static String tableNameConvertMappingPath(String tableName) {
        //兼容使用大写的表名
        tableName = tableName.toLowerCase();
        return "/" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);
    }

    public static String domainNameConvertMappingPath(String domainName) {
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, domainName);
        return tableNameConvertMappingPath(tableName);
    }

}
