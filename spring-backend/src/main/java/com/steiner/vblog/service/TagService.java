package com.steiner.vblog.service;

import com.steiner.vblog.dto.query.TagQuery;
import com.steiner.vblog.dto.request.PostTagRequest;
import com.steiner.vblog.dto.request.PutTagRequest;
import com.steiner.vblog.exception.ServerInternalException;
import com.steiner.vblog.mapper.TagMapper;
import com.steiner.vblog.model.Tag;
import com.steiner.vblog.table_metadata.TagsMetadata;
import com.steiner.vblog.validate.impl.PostTagRequestValidator;
import com.steiner.vblog.validate.impl.PutTagRequestValidator;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagService {
    @Resource
    TagsMetadata metadata;

    @Resource
    TagMapper mapper;

    @Resource
    PostTagRequestValidator postValidator;

    @Resource
    PutTagRequestValidator putValidator;

    @Nonnull
    public Tag insertOne(@Nonnull PostTagRequest request) {
        postValidator.validate(request);

        int id = mapper.insertOne(metadata, request);
        return mapper.findOne(metadata, id)
                .orElseThrow(() -> new ServerInternalException("unwrap optional failed"));
    }

    public void deleteOne(int id) {
        mapper.deleteOne(metadata, id);
    }

    @Nonnull
    public Tag updateOne(@Nonnull PutTagRequest request) {
        putValidator.validate(request);

        mapper.updateOne(metadata, request);
        return mapper.findOne(metadata, request.id)
                .orElseThrow(() -> new ServerInternalException("unwrap optional failed"));
    }

    public Optional<Tag> findOne(int id) {
        return mapper.findOne(metadata, id);
    }

    @Nonnull
    public List<Tag> findAll(@Nonnull TagQuery query) {
        return mapper.findAll(metadata, query);
    }

    @Nonnull
    public List<Tag> findAllOfArticle(int articleId) {
        return mapper.findAllOfArticle(metadata, articleId);
    }

}
