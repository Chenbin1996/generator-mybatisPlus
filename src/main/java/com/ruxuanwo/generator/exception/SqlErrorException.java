package com.ruxuanwo.generator.exception;

/**
 * @Author: ChenBin
 * @Date: 2018/5/25/0025 14:22
 */
public class SqlErrorException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "数据库用户名或密码错误！";
    private String message;

    public SqlErrorException() {
        super();
    }

    public SqlErrorException(String message) {
        super();
        this.message = message;
    }

    public SqlErrorException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return message == null ? DEFAULT_MESSAGE : message;
    }
}
