package com.ruxuanwo.generator.controller;


import com.ruxuanwo.generator.generator.CodeGenerator;
import com.ruxuanwo.generator.model.CodeConfig;
import com.ruxuanwo.generator.model.DbConfig;
import com.ruxuanwo.generator.model.PathConfig;
import com.ruxuanwo.generator.model.ProjectConfig;
import com.ruxuanwo.generator.utils.CodeUtil;
import com.ruxuanwo.generator.utils.ResponseMsgUtil;
import com.ruxuanwo.generator.utils.Result;
import com.ruxuanwo.generator.utils.ZipUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 生成SpringBoot-Controller类
 *
 * @author chenbin on 2018
 */
@RestController
@RequestMapping("/springBoot")
public class SpringController {

    /**
     * 创建文件 可选四种类型项目创建
     *
     * @param packAge 包路径，如：com.uhope.rl.demo
     * @param jsonData json数组
     * @param dbConfig 数据库连接对象
     * @param fileName 项目名称，如：rl-demo
     * @param author 创建人
     * @param type 项目类型，1、core层；2、app层；3、独立项目；4、代码片段
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/create")
    public Result createProject(@RequestParam String packAge,
                                @RequestParam String jsonData,
                                DbConfig dbConfig,
                                @RequestParam String fileName,
                                @RequestParam String author,
                                @RequestParam String type,
                                HttpServletResponse response) throws UnsupportedEncodingException {
        //初始化项目名称以及各个文件的路径
        ProjectConfig projectConfig = PathConfig.create(packAge);
        String newFileName = CodeConfig.FILE_PATH + UUID.randomUUID().toString().replaceAll("-", "") + "/" + fileName;

        //生成代码
        CodeGenerator.getCode(jsonData, dbConfig, newFileName, projectConfig, author, type);
        //设置响应头，控制浏览器下载该文件
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".zip", CodeConfig.ENCODING));
        response.setCharacterEncoding(CodeConfig.ENCODING);

        try {
            File file = new File(newFileName + ".zip");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fous = new FileOutputStream(file);

            //压缩导出
            ZipUtil.toZip(newFileName, fous, true);
            response.setContentType("application/x-msdownload");
            //读取要下载的文件，保存到文件输入流
            FileInputStream in = new FileInputStream(file);
            //创建输出流
            OutputStream out = response.getOutputStream();
            //创建缓冲区
            byte[] buffer = new byte[1024];
            int len;
            //循环将输入流中的内容读取到缓冲区当中
            while ((len = in.read(buffer)) > 0) {
                //输出缓冲区的内容到浏览器，实现文件下载
                out.write(buffer, 0, len);
            }
            //关闭文件输入流
            in.close();
            //关闭输出流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMsgUtil.failure();
        }
        String delFileName = newFileName.substring(0, newFileName.lastIndexOf("/"));
        //删除临时文件夹
        CodeUtil.delFolder(delFileName);
        return ResponseMsgUtil.success(null);
    }


}
