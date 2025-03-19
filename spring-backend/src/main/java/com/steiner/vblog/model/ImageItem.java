package com.steiner.vblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;

public final class ImageItem {
    public int id;

    @Nonnull
    @JsonIgnore
    public String path;

    @Nonnull
    public String name;

    public ImageItem(int id, @Nonnull String path, @Nonnull String name) {
        this.id = id;
        this.path = path;
        this.name = name;
    }
}
