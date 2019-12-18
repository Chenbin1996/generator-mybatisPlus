package ${package.serviceImpl};

import ${package.domain}.${domainNameUpperCamel};
import ${package.mapper}.${domainNameUpperCamel}Mapper;
import ${package.service}.${domainNameUpperCamel}Service;
import ${package.core}.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * ${description} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${domainNameUpperCamel}ServiceImpl extends BaseServiceImpl<${domainNameUpperCamel}Mapper, ${domainNameUpperCamel}> implements ${domainNameUpperCamel}Service {
    @Autowired
    private ${domainNameUpperCamel}Mapper ${domainNameLowerCamel}Mapper;
}

