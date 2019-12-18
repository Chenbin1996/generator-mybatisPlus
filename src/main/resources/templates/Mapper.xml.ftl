<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.mapper}.${domainNameUpperCamel}Mapper">

    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.domain}.${domainNameUpperCamel}">
<#list fields as field>
<#if field.isKey><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list fields as field><#--生成公共字段 -->
    <#if !field.isKey>
        <result column="${field.name}" property="${field.propertyName}" />
    </#if>
</#list>
    </resultMap>

</mapper>
