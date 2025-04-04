package com.steiner.vblog.dto.request;

import jakarta.annotation.Nonnull;

public class PutTagRequest implements IRequest {
    @Nonnull
    public Integer id;

    @Nonnull
    public String name;

    public PutTagRequest() {
        this.id = null;
        this.name = null;
    }

    public PutTagRequest(@Nonnull Integer id, @Nonnull String name) {
        this.id = id;
        this.name = name;
    }
}
