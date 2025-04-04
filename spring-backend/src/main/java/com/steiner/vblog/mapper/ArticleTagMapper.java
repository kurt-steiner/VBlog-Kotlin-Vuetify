package com.steiner.vblog.mapper;

import com.steiner.vblog.table_metadata.ArticleTagMetadata;
import jakarta.annotation.Nonnull;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleTagMapper {
    void createTable(@Nonnull ArticleTagMetadata metadata);
    void dropTable(@Nonnull ArticleTagMetadata metadata);
}
