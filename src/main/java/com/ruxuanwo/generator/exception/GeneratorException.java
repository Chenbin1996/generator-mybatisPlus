package com.ruxuanwo.generator.exception;

/**
 * @Author: ChenBin
 * @Date: 2018/6/8/0008 16:37
 */
public class GeneratorException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "配置出现问题，检查配置！";
    private String message;

    public GeneratorException() {
        super();
    }

    public GeneratorException(String message) {
        super();
        this.message = message;
    }

    public GeneratorException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return message == null ? DEFAULT_MESSAGE : message;
    }
}
