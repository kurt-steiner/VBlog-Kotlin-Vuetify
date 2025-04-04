package com.steiner.vblog.validate;

import jakarta.annotation.Nonnull;

public class ValidateResult {
    public ValidateResult() {

    }

    public static ValidateResult Valid = new ValidateResult();

    public static class InValid extends ValidateResult {
        @Nonnull
        public final String message;

        public InValid(@Nonnull String message) {
            this.message = message;
        }
    }
}
