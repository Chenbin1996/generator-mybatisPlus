package com.ruxuanwo.generator.generator;

import com.ruxuanwo.generator.utils.CodeUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TemplateEngine {

    private Configuration configuration;

    public TemplateEngine() {
        init();
    }

    private TemplateEngine init(){
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassLoaderForTemplateLoading(TemplateEngine.class.getClassLoader(), "templates/");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return this;
    }

    public void writer(Map<String, Object> params, String templatePath, String outputFile) {
        File file = CodeUtil.getFile(outputFile);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8.name())){
            Template template = configuration.getTemplate(templatePath);
            template.process(params, outputStreamWriter);
            System.out.println("正在生成模板：" + templatePath);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("模板生成失败：" + e.getMessage());
        }
    }


}
