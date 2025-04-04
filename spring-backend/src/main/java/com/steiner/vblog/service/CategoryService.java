package com.steiner.vblog.service;

import com.steiner.vblog.dto.query.CategoryQuery;
import com.steiner.vblog.dto.request.PostCategoryRequest;
import com.steiner.vblog.dto.request.PutCategoryRequest;
import com.steiner.vblog.exception.ServerInternalException;
import com.steiner.vblog.mapper.CategoryMapper;
import com.steiner.vblog.model.Category;
import com.steiner.vblog.table_metadata.CategoriesMetadata;
import com.steiner.vblog.validate.impl.PostCategoryRequestValidator;
import com.steiner.vblog.validate.impl.PutCategoryRequestValidator;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    @Resource
    CategoriesMetadata metadata;

    @Resource
    CategoryMapper mapper;

    @Resource
    PostCategoryRequestValidator postValidator;

    @Resource
    PutCategoryRequestValidator putValidator;

    @Nonnull
    public Category insertOne(@Nonnull PostCategoryRequest request) {
        postValidator.validate(request);

        int id = mapper.insertOne(metadata, request);
        return mapper.findOne(metadata, id)
                .orElseThrow(() -> new ServerInternalException("unwrap optional failed"));
    }

    public void deleteOne(int id) {
        mapper.deleteOne(metadata, id);
    }

    @Nonnull
    public Category updateOne(@Nonnull PutCategoryRequest request) {
        putValidator.validate(request);

        mapper.updateOne(metadata, request);
        return mapper.findOne(metadata, request.id)
                .orElseThrow(() -> new ServerInternalException("unwrap optional failed"));
    }

    public Optional<Category> findOne(int id) {
        return mapper.findOne(metadata, id);
    }

    @Nonnull
    public List<Category> findAll(@Nonnull CategoryQuery query) {
        return mapper.findAll(metadata, query);
    }
}
