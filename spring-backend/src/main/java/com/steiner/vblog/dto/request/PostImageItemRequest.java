package com.steiner.vblog.dto.request;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class PostImageItemRequest implements IRequest {
    @Nullable
    public Integer returningId;

    @Nonnull
    public String name;

    @Nonnull
    public String path;

    public PostImageItemRequest(@Nonnull String name, @Nonnull String path) {
        this.returningId = null;
        this.name = name;
        this.path = path;
    }
}
