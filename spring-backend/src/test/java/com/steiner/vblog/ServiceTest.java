package com.steiner.vblog;

import com.steiner.vblog.dto.request.PostArticleRequest;
import com.steiner.vblog.dto.request.PostCategoryRequest;
import com.steiner.vblog.dto.request.PostTagRequest;
import com.steiner.vblog.model.article.ArticleStatus;
import com.steiner.vblog.service.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
public class ServiceTest {


    @Resource
    ArticleService articleService;

    @Resource
    CategoryService categoryService;

    @Resource
    ImageItemService imageItemService;

    @Resource
    TagService tagService;

    @Resource
    UserService userService;

    @Test
    public void testFakeDataInject() {
//        categoryService.insertOne(new PostCategoryRequest("category 1", 1));
//        categoryService.insertOne(new PostCategoryRequest("category 2", 1));
//
//        tagService.insertOne(new PostTagRequest("tag 1", 1));
//        tagService.insertOne(new PostTagRequest("tag 2", 1));

        PostArticleRequest postArticleRequest = new PostArticleRequest(
                "hello",
                "# Hello World",
                "<h1> Hello World </h1>",
                null,
                null,
                ArticleStatus.Published,
                1
        );

        articleService.insertOne(postArticleRequest);
    }


}
