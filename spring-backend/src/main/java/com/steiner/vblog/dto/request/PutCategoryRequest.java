package com.steiner.vblog.dto.request;

import jakarta.annotation.Nonnull;

public final class PutCategoryRequest implements IRequest {
    @Nonnull
    public Integer id;

    @Nonnull
    public String name;

    public PutCategoryRequest() {
        this.id = null;
        this.name = null;
    }

    public PutCategoryRequest(@Nonnull Integer id, @Nonnull String name) {
        this.id = id;
        this.name = name;
    }
}
