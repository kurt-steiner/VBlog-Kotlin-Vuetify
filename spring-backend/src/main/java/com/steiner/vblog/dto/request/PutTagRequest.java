package com.steiner.vblog.dto.request;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class PutTagRequest {
    @Nonnull
    public Integer id;

    @Nonnull
    public String name;

    public PutTagRequest() {
        this.id = null;
        this.name = null;
    }
}
