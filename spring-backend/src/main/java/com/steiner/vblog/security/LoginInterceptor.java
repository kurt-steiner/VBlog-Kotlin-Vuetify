package com.steiner.vblog.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.steiner.vblog.Constants;
import com.steiner.vblog.exception.ServerInternalException;
import com.steiner.vblog.model.User;
import com.steiner.vblog.util.JWTUtil;
import com.steiner.vblog.util.Response;
import com.steiner.vblog.util.result.Result;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Optional;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    JWTUtil jwtUtil;
    @Resource(name = "token-prefix")
    String tokenPrefix;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable Object handler) throws Exception {
        Optional<String> ifToken = Optional.ofNullable(request.getHeader(Constants.AuthorizationHeader));
        if (ifToken.isEmpty()) {
            String errorMessage = objectMapper.writeValueAsString(new Response.Err("no token found"));
            response.setStatus(401);
            response.getWriter().write(errorMessage);
            return false;
        }

        String token = ifToken.get().replace(tokenPrefix, "").trim();
        Result<User, JWTVerificationException> userResult = jwtUtil.tryParseToken(token);

        userResult.ifOk(user -> request.setAttribute(Constants.CURRENT_USER_KEY, user));

        userResult.ifErr(exception -> {
            try {
                String message = objectMapper.writeValueAsString(new Response.Err(exception.getMessage()));
                response.setStatus(401);
                response.getWriter().write(message);
            } catch (IOException ioException) {
                throw new ServerInternalException(ioException);
            }
        });

        return userResult.map(value -> true).orElse(false);
    }
}
