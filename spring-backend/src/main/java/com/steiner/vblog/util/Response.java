package com.steiner.vblog.util;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public abstract class Response {
    public static class Ok<T> extends Response {
        @Nonnull
        public final String message;

        @Nullable
        public final T data;

        public Ok(@Nonnull String message, @Nonnull T data) {
            this.message = message;
            this.data = data;
        }

        public Ok(@Nonnull String message) {
            this.message = message;
            this.data = null;
        }
    }

    public static class Err extends Response {
        @Nonnull
        public final String message;

        public Err(@Nonnull String message) {
            this.message = message;
        }
    }
}
