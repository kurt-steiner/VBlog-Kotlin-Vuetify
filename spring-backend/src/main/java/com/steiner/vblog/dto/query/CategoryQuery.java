package com.steiner.vblog.dto.query;

import jakarta.annotation.Nullable;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public final class CategoryQuery {
    @Nullable
    public String name;

    @Nullable
    public Integer userId;
}
