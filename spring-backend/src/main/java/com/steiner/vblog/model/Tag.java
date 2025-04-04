package com.steiner.vblog.model;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Tag {
    public int id;

    @Nonnull
    public String name;

    public int userId;

}
