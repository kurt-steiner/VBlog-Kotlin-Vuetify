package com.steiner.vblog.dto.request;

import jakarta.annotation.Nonnull;

public final class LoginRequest implements IRequest {
    @Nonnull
    public String name;

    @Nonnull
    public String passwordHash;


    public LoginRequest() {
        this.name = null;
        this.passwordHash = null;
    }

    public LoginRequest(@Nonnull String name, @Nonnull String passwordHash) {
        this.name = name;
        this.passwordHash = passwordHash;
    }
}
