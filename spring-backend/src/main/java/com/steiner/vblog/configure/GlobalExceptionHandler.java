package com.steiner.vblog.configure;

import com.steiner.vblog.exception.BadRequestException;
import com.steiner.vblog.exception.NotFoundException;
import com.steiner.vblog.exception.NotLoginException;
import com.steiner.vblog.exception.ServerInternalException;
import com.steiner.vblog.util.Response;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // validation 层的错误
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response.Err handleException(@Nonnull MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());

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

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response.Err handleException(@Nonnull NotFoundException exception) {
        log.error(exception.getMessage());

        return new Response.Err(exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response.Err handleException(@Nonnull BadRequestException exception) {
        log.error(exception.getMessage());

        return new Response.Err(exception.getMessage());
    }

    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response.Err handleException(@Nonnull NotLoginException exception) {
        log.error(exception.getMessage());

        return new Response.Err(exception.getMessage());
    }

    @ExceptionHandler(ServerInternalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response.Err handleException(@Nonnull ServerInternalException exception) {
        log.error(exception.getMessage());
        return new Response.Err(exception.getMessage());
    }
}
