package com.steiner.vblog.validate;

import com.steiner.vblog.dto.request.IRequest;
import jakarta.annotation.Nonnull;

public interface IValidator<T extends IRequest> {
    @Nonnull
    ValidateResult validate(@Nonnull T request);
}
