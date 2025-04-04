package com.steiner.vblog.service;

import com.steiner.vblog.dto.query.UserQuery;
import com.steiner.vblog.dto.request.LoginRequest;
import com.steiner.vblog.dto.request.PostUserRequest;
import com.steiner.vblog.dto.request.PutUserRequest;
import com.steiner.vblog.exception.BadRequestException;
import com.steiner.vblog.exception.ServerInternalException;
import com.steiner.vblog.mapper.UserMapper;
import com.steiner.vblog.model.User;
import com.steiner.vblog.table_metadata.UsersMetadata;
import com.steiner.vblog.util.JWTUtil;
import com.steiner.vblog.validate.impl.LoginRequestValidator;
import com.steiner.vblog.validate.impl.PostUserRequestValidator;
import com.steiner.vblog.validate.impl.PutUserRequestValidator;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    JWTUtil jwtUtil;

    @Resource
    UsersMetadata metadata;

    @Resource
    UserMapper mapper;

    @Resource
    PostUserRequestValidator postValidator;

    @Resource
    PutUserRequestValidator putValidator;

    @Resource
    LoginRequestValidator loginValidator;

    @Resource
    @Lazy
    public void setJwtUtil(@Nonnull JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Nonnull
    public String login(@Nonnull LoginRequest request) {
        loginValidator.validate(request);
        UserQuery query = UserQuery.builder()
                .name(request.name)
                .passwordHash(request.passwordHash)
                .build();

        Optional<User> ifUser = findAll(query).stream().findFirst();

        return ifUser.map(user -> jwtUtil.generateToken(user))
                .orElseThrow(() -> new BadRequestException("no such user"));
    }

    public void insertOne(@Nonnull PostUserRequest request) {
        postValidator.validate(request);
        mapper.insertOne(metadata, request);
    }

    public void deleteOne(int id) {
        mapper.deleteOne(metadata, id);
    }

    @Nonnull
    public User updateOne(@Nonnull PutUserRequest request) {
        putValidator.validate(request);

        mapper.updateOne(metadata, request);
        return mapper.findOne(metadata, request.id)
                .orElseThrow(() -> new ServerInternalException("unwrap optional failed"));
    }

    public Optional<User> findOne(int id) {
        return mapper.findOne(metadata, id);
    }

    @Nonnull
    public List<User> findAll(@Nonnull UserQuery query) {
        return mapper.findAll(metadata, query);
    }
}
