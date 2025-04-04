package com.steiner.vblog.controller;

import com.steiner.vblog.dto.query.CategoryQuery;
import com.steiner.vblog.dto.request.PostCategoryRequest;
import com.steiner.vblog.dto.request.PutCategoryRequest;
import com.steiner.vblog.model.Category;
import com.steiner.vblog.model.User;
import com.steiner.vblog.service.CategoryService;
import com.steiner.vblog.util.CurrentUser;
import com.steiner.vblog.util.Response;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    CategoryService service;

    @GetMapping
    public Response.Ok<List<Category>> findAll(@Nonnull HttpServletRequest request) {
        User user = CurrentUser.currentUser(request);
        CategoryQuery query = CategoryQuery.builder()
                .userId(user.id)
                .build();

        List<Category> result = service.findAll(query);
        return new Response.Ok<>("all categories", result);
    }

    @PostMapping
    public Response.Ok<Category> insertOne(@RequestBody PostCategoryRequest request) {
        Category result = service.insertOne(request);
        return new Response.Ok<>("insert ok", result);
    }

    @PutMapping
    public Response.Ok<Category> updateOne(@RequestBody PutCategoryRequest request) {
        Category result = service.updateOne(request);
        return new Response.Ok<>("update ok", result);
    }

    @DeleteMapping("/{id}")
    public Response.Ok<Void> deleteOne(@PathVariable("id") int id) {
        service.deleteOne(id);
        return new Response.Ok<>("delete ok");
    }
}
