package com.steiner.vblog.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class User {
    public int id;

    @Nonnull
    public String name;

    @Nullable
    public String nickname;

    @Nullable
    public String email;

    @Nonnull
    public java.sql.Timestamp registerTime;

    @Nullable
    public ImageItem avatar;
}
