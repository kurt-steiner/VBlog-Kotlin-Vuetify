package com.steiner.vblog.controller;

import com.steiner.vblog.dto.request.LoginRequest;
import com.steiner.vblog.dto.request.PostUserRequest;
import com.steiner.vblog.service.UserService;
import com.steiner.vblog.util.Response;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {
    @Resource
    UserService service;

    @PostMapping("/login")
    public Response.Ok<String> login(@RequestBody LoginRequest request) {
        String token = service.login(request);

        return new Response.Ok<>("login ok", token);
    }

    @PostMapping("/register")
    public Response.Ok<Void> register(@RequestBody PostUserRequest request) {
        service.insertOne(request);
        return new Response.Ok<>("register ok");
    }
}
