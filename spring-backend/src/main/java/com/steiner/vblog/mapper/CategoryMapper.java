package com.steiner.vblog.mapper;

import com.steiner.vblog.dto.query.CategoryQuery;
import com.steiner.vblog.model.Category;
import com.steiner.vblog.dto.request.PostCategoryRequest;
import com.steiner.vblog.dto.request.PutCategoryRequest;
import com.steiner.vblog.table_metadata.CategoriesMetadata;
import jakarta.annotation.Nonnull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CategoryMapper {
    void createTable(@Nonnull CategoriesMetadata metadata);
    void dropTable(@Nonnull CategoriesMetadata metadata);

    int insertOne(@Param("metadata") @Nonnull CategoriesMetadata metadata,
                  @Param("request") @Nonnull PostCategoryRequest request);

    void deleteOne(@Param("metadata") @Nonnull CategoriesMetadata metadata,
                   @Param("id") int id);

    int updateOne(@Param("metadata") @Nonnull CategoriesMetadata metadata,
                  @Param("request") @Nonnull PutCategoryRequest request);

    Optional<Category> findOne(@Param("metadata") @Nonnull CategoriesMetadata metadata,
                               @Param("id") int id);


    List<Category> findAll(@Param("metadata") @Nonnull CategoriesMetadata metadata,
                           @Param("query") @Nonnull CategoryQuery query);
}
