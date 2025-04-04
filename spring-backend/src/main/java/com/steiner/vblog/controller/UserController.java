package com.steiner.vblog.controller;

import com.steiner.vblog.dto.request.PutUserRequest;
import com.steiner.vblog.model.User;
import com.steiner.vblog.service.UserService;
import com.steiner.vblog.util.CurrentUser;
import com.steiner.vblog.util.Response;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService service;

    @GetMapping
    public Response.Ok<User> findSelf(@Nonnull HttpServletRequest request) {
        User user = CurrentUser.currentUser(request);
        return new Response.Ok<>("this user", user);
    }

    @PutMapping
    public Response.Ok<Void> updateOne(@RequestBody PutUserRequest request) {
        service.updateOne(request);
        return new Response.Ok<>("update ok");
    }

    @DeleteMapping("/{id}")
    public Response.Ok<Void> deleteOne(@PathVariable("id") int id) {
        service.deleteOne(id);

        return new Response.Ok<>("delete ok");
    }
}
