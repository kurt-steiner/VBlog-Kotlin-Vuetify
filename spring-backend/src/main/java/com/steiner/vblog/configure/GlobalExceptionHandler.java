package com.steiner.vblog.configure;

import com.steiner.vblog.util.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public final class GlobalExceptionHandler {

    // validation 层的错误
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response.Err handleException(MethodArgumentNotValidException exception) {

        /*
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            // 获取字段名称
            String fieldName = error.getField();

            // 动态生成消息
            String message = messageSource.getMessage(error.getDefaultMessage(), new Object[]{fieldName}, Locale.getDefault());
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
         */

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();

            String message = Optional.ofNullable(error.getDefaultMessage())
                    .orElse("error");

            errors.put(fieldName, message);
        });

        StringBuilder stringBuilder = new StringBuilder();
        errors.forEach((key, value) -> {
            stringBuilder.append("%s: %s".formatted(key, value));
        });

        return new Response.Err(stringBuilder.toString());
    }
}
