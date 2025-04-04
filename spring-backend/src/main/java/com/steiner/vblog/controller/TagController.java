package com.steiner.vblog.controller;

import com.steiner.vblog.dto.request.PostTagRequest;
import com.steiner.vblog.dto.request.PutTagRequest;
import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.User;
import com.steiner.vblog.service.TagService;
import com.steiner.vblog.util.CurrentUser;
import com.steiner.vblog.util.Response;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Resource
    TagService service;

    @PostMapping
    public Response.Ok<Tag> insertOne(@RequestBody PostTagRequest request) {
        Tag result = service.insertOne(request);
        return new Response.Ok<>("insert ok", result);
    }

    @PutMapping
    public Response.Ok<Tag> updateOne(@RequestBody PutTagRequest request) {
        Tag result = service.updateOne(request);
        return new Response.Ok<>("update ok", result);
    }

    @DeleteMapping("/{id}")
    public Response.Ok<Void> deleteOne(@PathVariable("id") int id) {
        service.deleteOne(id);
        return new Response.Ok<>("delete ok");
    }

    @GetMapping
    public Response.Ok<List<Tag>> findAll(@Nonnull HttpServletRequest request) {
        User user = CurrentUser.currentUser(request);
        List<Tag> tags = service.findAllOfArticle(user.id);
        return new Response.Ok<>("all tags", tags);
    }
}
