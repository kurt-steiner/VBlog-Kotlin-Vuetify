package com.steiner.vblog.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public abstract class Response {
    public static class Ok<T> extends Response {
        @Nonnull
        @JsonProperty("message")
        public final String message;

        @Nullable
        @JsonProperty("data")
        public final T data;

        @JsonCreator
        public Ok(@JsonProperty("message") @Nonnull String message,
                  @JsonProperty("data") @Nonnull T data) {
            this.message = message;
            this.data = data;
        }

        public Ok(@JsonProperty("message") @Nonnull String message) {
            this.message = message;
            this.data = null;
        }
    }

    public static class Err extends Response {
        @Nonnull
        @JsonProperty("message")
        public final String message;

        @JsonCreator
        public Err(@JsonProperty("message") @Nonnull String message) {
            this.message = message;
        }
    }
}
