package com.steiner.vblog;

import com.steiner.vblog.dto.request.PostCategoryRequest;
import com.steiner.vblog.mapper.CategoryMapper;
import com.steiner.vblog.table_metadata.CategoriesMetadata;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@SpringBootTest
public class CategoryMapperTest {
    @Resource
    CategoryMapper mapper;
    @Resource
    CategoriesMetadata metadata;

    @Test
    @Transactional
    public void testInsert() {
        PostCategoryRequest request = new PostCategoryRequest("category 1", 1);
        mapper.insertOne(metadata, request);
        Objects.requireNonNull(request.returningId);
        mapper.findOne(metadata, request.returningId).ifPresent(model -> {
            System.out.println("id=%s, name=%s, create-time=%s".formatted(model.id, model.name, model.createTime));
        });
    }
}
