package com.steiner.vblog.controller;

import com.steiner.vblog.dto.query.ArticleQuery;
import com.steiner.vblog.dto.request.PostArticleRequest;
import com.steiner.vblog.dto.request.PutArticleRequest;
import com.steiner.vblog.exception.BadRequestException;
import com.steiner.vblog.exception.NotFoundException;
import com.steiner.vblog.model.User;
import com.steiner.vblog.model.article.Article;
import com.steiner.vblog.model.article.ArticleShortcut;
import com.steiner.vblog.model.article.ArticleSortBy;
import com.steiner.vblog.model.article.ArticleStatus;
import com.steiner.vblog.service.ArticleService;
import com.steiner.vblog.service.UserService;
import com.steiner.vblog.table_metadata.ArticlesMetadata;
import com.steiner.vblog.util.CurrentUser;
import com.steiner.vblog.util.Page;
import com.steiner.vblog.util.Response;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    ArticleService articleService;

    @Resource
    UserService userService;

    @Resource
    ArticlesMetadata metadata;

    @PostMapping
    public Response.Ok<ArticleShortcut> insertOne(@RequestBody PostArticleRequest request) {
        ArticleShortcut result = ArticleShortcut.fromArticle(articleService.insertOne(request));
        return new Response.Ok<>("insert article ok", result);
    }

    @DeleteMapping("/{id}")
    public Response.Ok<Void> deleteOne(@PathVariable("id") int id) {
        articleService.deleteOne(id);
        return new Response.Ok<>("delete ok");
    }

    @PutMapping("/{id}")
    public Response.Ok<Article> updateOne(@RequestBody PutArticleRequest request) {
        Article result = articleService.updateOne(request);
        return new Response.Ok<>("update ok", result);
    }

    @GetMapping("/{id}")
    public Response.Ok<Article> findOne(@PathVariable("id") int id) {
        Optional<Article> article = articleService.findOne(id);
        return article.map(value -> new Response.Ok<>("this artcile", value))
                .orElseThrow(() -> new NotFoundException("no such article"));
    }

    @GetMapping
    public Response.Ok<Page<ArticleShortcut>> findAll(@RequestParam Map<String, String> queryParameters, @Nonnull HttpServletRequest request) {
        User currentUser = CurrentUser.currentUser(request);
        ArticleQuery articleQuery = generateQueryFromQueryParams(queryParameters, currentUser);

        Page<ArticleShortcut> articles = articleService.findAll(articleQuery);
        return new Response.Ok<>("all articles", articles);
    }

    @Nonnull
    private ArticleQuery generateQueryFromQueryParams(@Nonnull Map<String, String> queryParameters, @Nonnull User currentUser) {
        @Nullable ArticleStatus status = null;
        try {
            @Nullable String string = queryParameters.getOrDefault("status", null);
            if (string != null && !(List.of("0", "1", "2", "3").contains(string))) {
                int code = Integer.parseInt(string);
                status = ArticleStatus.fromCode(code);
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("cannot parse status");
        }

        @Nullable Integer tagId = null;

        try {
            @Nullable String string = queryParameters.getOrDefault("tag-id", null);
            if (string != null) {
                tagId = Integer.parseInt(string);
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("cannot parse tag id");
        }

        @Nullable Integer categoryId = null;

        try {
            @Nullable String string = queryParameters.getOrDefault("category-id", null);
            if (string != null) {
                categoryId = Integer.parseInt(string);
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("cannot parse category id");
        }

        @Nullable String title = queryParameters.getOrDefault("title", null);
        int page = metadata.defaultQueryPage;

        try {
            @Nullable String string = queryParameters.getOrDefault("page", null);

            if (string != null) {
                page = Integer.parseInt(string);
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("cannot parse page");
        }

        int size = metadata.defaultQuerySize;
        try {
            @Nullable String string = queryParameters.getOrDefault("size", null);

            if (string != null) {
                size = Integer.parseInt(string);
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("cannot parse size");
        }

        boolean reverse = "reverse".equals(queryParameters.getOrDefault("reverse", null));
        ArticleSortBy.SortBy sortBy = ArticleSortBy.SortBy.ByEditTime;
        switch (queryParameters.getOrDefault("sort-by", null)) {
            case "title": {
                sortBy = ArticleSortBy.SortBy.ByTitle;
                break;
            }

            case "edit-time": {
                sortBy = ArticleSortBy.SortBy.ByEditTime;
                break;
            }

            case "publish-date": {
                sortBy = ArticleSortBy.SortBy.ByPublishDate;
                break;
            }

            default: {

            }
        }

        return ArticleQuery.builder()
                .authorId(currentUser.id)
                .status(status)
                .tagId(tagId)
                .categoryId(categoryId)
                .title(title)
                .sortBy(new ArticleSortBy(sortBy, reverse))
                .page(page)
                .size(size)
                .build();
    }

}
