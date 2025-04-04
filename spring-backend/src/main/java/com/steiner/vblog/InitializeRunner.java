package com.steiner.vblog;

import com.steiner.vblog.mapper.*;
import com.steiner.vblog.table_metadata.*;
import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitializeRunner implements ApplicationRunner {
    @Resource(name = "initialize")
    boolean shouldInitialize;

    @Resource
    ArticlesMetadata articlesMetadata;

    @Resource
    TagsMetadata tagsMetadata;

    @Resource
    ArticleTagMetadata articleTagMetadata;

    @Resource
    CategoriesMetadata categoriesMetadata;

    @Resource
    UsersMetadata usersMetadata;

    @Resource
    ImageItemsMetadata imageItemsMetadata;

    @Resource
    ArticleMapper articleMapper;

    @Resource
    TagMapper tagMapper;

    @Resource
    ArticleTagMapper articleTagMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    ImageItemMapper imageItemMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!shouldInitialize) {
            return;
        }

        // drop tables
        articleTagMapper.dropTable(articleTagMetadata);
        tagMapper.dropTable(tagsMetadata);
        articleMapper.dropTable(articlesMetadata);
        categoryMapper.dropTable(categoriesMetadata);
        userMapper.dropTable(usersMetadata);
        imageItemMapper.dropTable(imageItemsMetadata);

        // create tables
        imageItemMapper.createTable(imageItemsMetadata);
        userMapper.createTable(usersMetadata);
        categoryMapper.createTable(categoriesMetadata);
        articleMapper.createTable(articlesMetadata);
        tagMapper.createTable(tagsMetadata);
        articleTagMapper.createTable(articleTagMetadata);
    }
}
