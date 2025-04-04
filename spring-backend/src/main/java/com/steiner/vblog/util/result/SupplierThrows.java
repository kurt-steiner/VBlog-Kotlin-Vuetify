package com.steiner.vblog.util.result;

import jakarta.annotation.Nullable;

@FunctionalInterface
public interface SupplierThrows<T, E extends Throwable> {
    @Nullable
    T supplier() throws E;
}
