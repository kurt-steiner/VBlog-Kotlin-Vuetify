package com.steiner.vblog.util;

import com.steiner.vblog.Constants;
import com.steiner.vblog.exception.NotLoginException;
import com.steiner.vblog.exception.ServerInternalException;
import com.steiner.vblog.model.User;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class CurrentUser {
    public static User currentUser(@Nonnull HttpServletRequest request) {
        @Nullable Object attribute = request.getAttribute(Constants.CURRENT_USER_KEY);
        if (attribute == null) {
            throw new NotLoginException("no user login");
        }

        if (attribute instanceof User) {
            return (User) attribute;
        } else {
            throw new ServerInternalException("cast to User failed");
        }
    }
}
