package ${package.exception};

import ${package.util}.ResponseMsgUtil;
import ${package.util}.Result;
import ${package.util}.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Map;

/**
 * 全局异常处理
 * 一般情况下，方法都有异常处理机制，但不能排除有个别异常没有处理，导致返回到前台，因此在这里做一个异常拦截，统一处理那些未被处理过的异常
 *
 * @author ${author} on ${date}
 */
@ControllerAdvice
@Controller
@RequestMapping
public class GlobalExceptionHandler extends AbstractErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Value("${serverErrorPath}}")
    private static String errorPath = "/error";


    /**
     * sql异常
     *
     * @param req
     * @param rsp
     * @param ex
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ExceptionHandler(SQLException.class)
    public Result<String> sqlException(HttpServletRequest req, HttpServletResponse rsp, Exception ex) {
        LOGGER.error("!!! request uri:{} from {} server exception:{}", req.getRequestURI(), RequestUtil.getIpAddress(req), ex == null ? null : ex);
        return ResponseMsgUtil.builderResponse(1002, ex == null ? null : ex.getMessage(), null);
    }


    /**
     * 500错误.
     *
     * @param req
     * @param rsp
     * @param ex
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<String> serverError(HttpServletRequest req, HttpServletResponse rsp, Exception ex) throws Exception {
        LOGGER.error("!!! request uri:{} from {} server exception:{}", req.getRequestURI(), RequestUtil.getIpAddress(req), ex == null ? null : ex);
        return ResponseMsgUtil.builderResponse(1002, ex == null ? null : ex.getMessage(), null);
    }


    /**
     * 404的拦截.
     *
     * @param request
     * @param response
     * @param ex
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<String> notFound(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
        LOGGER.error("!!! request uri:{} from {} not found exception:{}", request.getRequestURI(), RequestUtil.getIpAddress(request), ex);
        return ResponseMsgUtil.builderResponse(404, ex == null ? null : ex.getMessage(), null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Result<String> paramException(MissingServletRequestParameterException ex) {
        LOGGER.error("缺少请求参数:{}", ex.getMessage());
        return ResponseMsgUtil.builderResponse(99999, "缺少参数:" + ex.getParameterName(), null);
    }

    //参数类型不匹配
    //getPropertyName()获取数据类型不匹配参数名称
    //getRequiredType()实际要求客户端传递的数据类型
    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public Result<String> requestTypeMismatch(TypeMismatchException ex) {
        LOGGER.error("参数类型有误:{}", ex.getMessage());
        return ResponseMsgUtil.builderResponse(99999, "参数类型不匹配,参数" + ex.getPropertyName() + "类型应该为" + ex.getRequiredType(), null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result<String> requestMethod(HttpRequestMethodNotSupportedException ex) {
        LOGGER.error("请求方式有误：{}", ex.getMethod());
        return ResponseMsgUtil.builderResponse(99999, "请求方式有误:" + ex.getMethod(), null);
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public Result<String> fileSizeLimit(MultipartException m){
        LOGGER.error("超过文件上传大小限制");
        return ResponseMsgUtil.builderResponse(99999,"超过文件大小限制,最大10MB",null);
    }


    /**
     * 重写/error请求, ${server.error.path:${error.path:/error}} IDEA报红无需处理，作用是获取spring底层错误拦截
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "${serverErrorPath}}")
    public Result<String> handleErrors(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NOT_FOUND) {
            throw new NoHandlerFoundException(request.getMethod(),request.getRequestURL().toString(),new HttpHeaders()) ;
        }
        Map<String, Object> body = getErrorAttributes(request, true);
        return ResponseMsgUtil.builderResponse(Integer.parseInt(body.get("status").toString()), body.get("message").toString(), null);
    }


    @Override
    public String getErrorPath() {
        return errorPath;
    }
}