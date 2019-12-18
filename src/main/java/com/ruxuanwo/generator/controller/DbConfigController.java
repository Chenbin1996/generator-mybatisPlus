package com.ruxuanwo.generator.controller;


import com.ruxuanwo.generator.model.DbConfig;
import com.ruxuanwo.generator.utils.DataBaseUtil;
import com.ruxuanwo.generator.utils.ResponseMsgUtil;
import com.ruxuanwo.generator.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库连接-Controller类
 *
 * @author chenbin on 2018
 */
@RestController
@RequestMapping("/dbConfig")
public class DbConfigController {

    @PostMapping("/connection")
    public Result connection(DbConfig dbConfig) {

        List<String> tableNames = DataBaseUtil.getTableNames(dbConfig);
        if (tableNames == null) {
            return ResponseMsgUtil.failure();
        }
        return ResponseMsgUtil.success(tableNames);

    }

    @GetMapping("/detail")
    public Object detail(DbConfig dbConfig) {
        HashMap<String, List<String>> map = new HashMap<>(16);
        List<String> columnNames = DataBaseUtil.getColumnNames(dbConfig);
        List<String> columnTypes = DataBaseUtil.getColumnTypes(dbConfig);
        List<String> columnComments = DataBaseUtil.getColumnComments(dbConfig);
        map.put("columnNames", columnNames);
        map.put("columnTypes", columnTypes);
        map.put("ColumnComments", columnComments);
        return map;
    }

    @GetMapping("/tableComment")
    public Result tableComment(DbConfig dbConfig){
        List<Map<String, Object>> maps = DataBaseUtil.getTableComment(dbConfig);
        if (maps == null) {
            return ResponseMsgUtil.failure();
        }
        return ResponseMsgUtil.success(maps);
    }

}
