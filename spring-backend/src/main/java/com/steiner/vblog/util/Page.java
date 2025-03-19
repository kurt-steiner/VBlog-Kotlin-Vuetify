package com.steiner.vblog.util;

import jakarta.annotation.Nonnull;

import java.util.List;

public class Page<T> {
    @Nonnull
    public final List<T> content;

    public final int totalPages;

    public Page(@Nonnull List<T> content, int totalPages) {
        this.content = content;
        this.totalPages = totalPages;
    }
}
