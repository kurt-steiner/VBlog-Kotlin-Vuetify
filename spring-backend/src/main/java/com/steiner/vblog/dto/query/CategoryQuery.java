package com.steiner.vblog.dto.query;

import jakarta.annotation.Nullable;

@lombok.Builder
public class CategoryQuery {
    @Nullable
    public String name;

    @Nullable
    public Integer userId;
}
