package ${package.core};

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
* 扩展Mybatis-Plus接口
*
* ${author} on ${date}
*/
public interface BaseService<T> extends IService<T> {
    List<T> find(T entity);
    T find(String fieldName, Object value);
    T getOne(T entity);
    boolean remove(T entity);
}