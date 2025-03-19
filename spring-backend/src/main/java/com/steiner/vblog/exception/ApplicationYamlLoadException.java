package com.steiner.vblog.exception;

// 读取 `application.yaml` 配置产生的错误
public class ApplicationYamlLoadException extends RuntimeException {
    public ApplicationYamlLoadException(String message) {
        super(message);
    }

    public ApplicationYamlLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationYamlLoadException(Throwable cause) {
        super(cause);
    }
}
