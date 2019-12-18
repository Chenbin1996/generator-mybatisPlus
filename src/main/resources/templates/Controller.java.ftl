package ${package.controller};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ${package.service}.${domainNameUpperCamel}Service;

/**
 * <p>
 * ${description} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${domainNameUpperCamel}Controller {
    @Autowired
    private ${domainNameUpperCamel}Service ${domainNameLowerCamel}Service;
}

