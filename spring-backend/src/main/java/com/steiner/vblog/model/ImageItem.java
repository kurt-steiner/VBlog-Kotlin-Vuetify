package com.steiner.vblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ImageItem {
    public int id;

    @Nonnull
    @JsonIgnore
    public String path;

    @Nonnull
    public String name;
}
