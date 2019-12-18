package ${package.domain};

<#if importPackage?? && (importPackage?size > 0) >
    <#list importPackage as pkg>
import ${pkg};
    </#list>
</#if>
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * <p>
 * ${description}-实体类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("${tableName}")
public class ${domainNameUpperCamel} extends Model<${domainNameUpperCamel}> {

    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list fields as field>

    <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
    </#if>
    <#if field.isKey>
    <#-- 主键 -->
    @TableId(value = "${field.name}", type = IdType.${field.keyType})
    <#else >
    <#-- 普通字段 -->
    @TableField("${field.name}")
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>

    @Override
    protected Serializable pkVal() {
<#list fields as field>
    <#if field.isKey>
        return this.${field.propertyName};
    </#if>
</#list>
    }
}
