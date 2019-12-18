package ${package.util};

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 返回数据结果集合
 *
 * @author ${author} on ${date}
 */
public class Result<T> {
    private Integer resCode;
    private String resMsg;
    private T data;

    public Result() {
    }

    public Integer getResCode() {
        return this.resCode;
    }

    public void setResCode(Integer resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return this.resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toJson() {
        return this.data == null ? JSON.toJSONString(this) : this.toJson(SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue);
    }

    public String toJson(SerializerFeature... features) {
        return features == null ? this.toJson() : JSON.toJSONString(this, features);
    }

    public String toString() {
        return "Result{resCode=" + this.resCode + ", resMsg='" + this.resMsg + '\'' + ", data=" + this.data + '}';
    }
}