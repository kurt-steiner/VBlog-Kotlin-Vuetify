package com.steiner.vblog.mapper;

import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.article.Article;
import com.steiner.vblog.dto.query.ArticleQuery;
import com.steiner.vblog.dto.request.PostArticleRequest;
import com.steiner.vblog.dto.request.PutArticleRequest;
import com.steiner.vblog.model.article.ArticleShortcut;
import com.steiner.vblog.table_metadata.ArticlesMetadata;
import jakarta.annotation.Nonnull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mapper
public interface ArticleMapper {
    void createTable(@Nonnull ArticlesMetadata metadata);
    void dropTable(@Nonnull ArticlesMetadata metadata);


    // when calling insertOne, the request is already validated by service
    default int insertOne(@Param("metadata") @Nonnull ArticlesMetadata metadata,
                          @Param("request") @Nonnull PostArticleRequest request) {
        int result = insertOneArticle(metadata, request);
        if (result < 0) {
            return result;
        }

        if (request.tags != null) {
            Objects.requireNonNull(request.returningId);
            insertAssociation(metadata, request);
        }

        return 1;
    }

    int insertOneArticle(@Param("metadata") @Nonnull ArticlesMetadata metadata,
                         @Param("request") @Nonnull PostArticleRequest request);

    void insertAssociation(@Param("metadata") @Nonnull ArticlesMetadata metadata,
                          @Param("request") @Nonnull PostArticleRequest request);

    void deleteOne(@Param("metadata") @Nonnull ArticlesMetadata metadata,
                   @Param("id") int id);
    void deleteAllOfAuthor(@Param("metadata") @Nonnull ArticlesMetadata metadata,
                           @Param("authorId") int authorId);

    int updateOne(@Param("metadata") @Nonnull ArticlesMetadata metadata,
                  @Param("request") @Nonnull PutArticleRequest request);

    Optional<Article> findOne(@Param("metadata") @Nonnull ArticlesMetadata metadata,
                              @Param("id") int id);

    @Nonnull
    List<ArticleShortcut> findAll(@Param("metadata") @Nonnull ArticlesMetadata metadata,
                                  @Param("query") @Nonnull ArticleQuery query);


    int totalPages(@Param("metadata") @Nonnull ArticlesMetadata metadata,
                   @Param("size") int size);

    List<Tag> findAllTags(@Param("metadata") @Nonnull ArticlesMetadata metadata,
                          @Param("id") int id);
}
