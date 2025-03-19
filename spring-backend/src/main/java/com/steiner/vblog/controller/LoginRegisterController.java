package com.steiner.vblog.controller;

import com.steiner.vblog.dto.request.LoginRequest;
import com.steiner.vblog.util.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRegisterController {
    @PostMapping("/login")
    public Response.Ok<String> login(@Valid @RequestBody LoginRequest request) {

    }
}
