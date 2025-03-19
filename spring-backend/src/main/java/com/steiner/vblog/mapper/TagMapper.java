package com.steiner.vblog.mapper;

import com.steiner.vblog.dto.query.TagQuery;
import com.steiner.vblog.dto.request.PostTagRequest;
import com.steiner.vblog.dto.request.PutTagRequest;
import com.steiner.vblog.model.Tag;
import com.steiner.vblog.table_metadata.TagsMetadata;
import jakarta.annotation.Nonnull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TagMapper {
    void createTable(@Nonnull TagsMetadata metadata);
    void dropTable(@Nonnull TagsMetadata metadata);

    int insertOne(@Param("metadata") @Nonnull TagsMetadata metadata,
                  @Param("request") @Nonnull PostTagRequest request);

    void deleteOne(@Param("metadata") @Nonnull TagsMetadata metadata,
                   @Param("id") int id);

    int updateOne(@Param("metadata") @Nonnull TagsMetadata metadata,
                  @Param("request") @Nonnull PutTagRequest request);

    Optional<Tag> findOne(@Param("metadata") @Nonnull TagsMetadata metadata,
                          @Param("id") int id);

    List<Tag> findAll(@Param("metadata") @Nonnull TagsMetadata metadata,
                      @Param("query") @Nonnull TagQuery query);

    List<Tag> findAllOfArticle(@Param("metadata") @Nonnull TagsMetadata metadata,
                               @Param("articleId") int articleId);
}
