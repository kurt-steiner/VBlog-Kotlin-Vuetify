package com.steiner.vblog.mapper;

import com.steiner.vblog.dto.request.PostImageItemRequest;
import com.steiner.vblog.model.ImageItem;
import com.steiner.vblog.table_metadata.ImageItemsMetadata;
import jakarta.annotation.Nonnull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface ImageItemMapper {
    void createTable(@Nonnull ImageItemsMetadata metadata);
    void dropTable(@Nonnull ImageItemsMetadata metadata);

    int insertOne(@Param("metadata") @Nonnull ImageItemsMetadata metadata,
                  @Param("request") @Nonnull PostImageItemRequest request);

    Optional<ImageItem> findOne(@Param("metadata") @Nonnull ImageItemsMetadata metadata,
                                @Param("id") int id);

}
