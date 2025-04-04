package com.steiner.vblog.dto.request;

import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.article.ArticleStatus;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class PostArticleRequest implements IRequest {
    @Nonnull
    @lombok.NonNull
    public String title;

    @Nonnull
    @lombok.NonNull
    public String markdownContent;

    @Nonnull
    @lombok.NonNull
    public String htmlContent;

    @Nullable
    public Integer categoryId;

    @Nullable
    public String summary;

    @Nullable
    public List<Tag> tags;

    @Nonnull
    @lombok.NonNull
    public ArticleStatus status;

    @Nonnull
    @lombok.NonNull
    public Integer authorId;
}
