package com.steiner.vblog.model;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Category {
    public int id;

    @Nonnull
    public String name;

    @Nonnull
    public java.sql.Timestamp createTime;

    public int userId;
}
