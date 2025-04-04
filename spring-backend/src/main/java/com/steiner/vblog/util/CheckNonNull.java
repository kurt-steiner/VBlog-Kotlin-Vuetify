package com.steiner.vblog.util;

import com.steiner.vblog.exception.ConstraintNonnullException;
import com.steiner.vblog.util.result.Result;
import jakarta.annotation.Nonnull;

import java.lang.reflect.Field;

public class CheckNonNull {
    @Nonnull
    public static <T> Result<Void, ConstraintNonnullException> checkNonNull(Class<T> type, T instance) {
        Field [] fields = type.getDeclaredFields();
        for (Field field: fields) {
            try {
                if (field.isAnnotationPresent(Nonnull.class) && field.get(instance) == null) {
                    return Result.Err(new ConstraintNonnullException("`%s` cannot be null".formatted(field.getName())));
                }
            } catch (IllegalAccessException e) {
                return Result.Err(new ConstraintNonnullException(e));
            }
        }

        return Result.Ok();
    }
}
