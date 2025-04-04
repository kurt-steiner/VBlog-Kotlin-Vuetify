package com.steiner.vblog.validate.impl;

import com.steiner.vblog.dto.request.LoginRequest;
import com.steiner.vblog.exception.ConstraintNonnullException;
import com.steiner.vblog.util.CheckNonNull;
import com.steiner.vblog.util.result.Result;
import com.steiner.vblog.validate.IValidator;
import com.steiner.vblog.validate.ValidateResult;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class LoginRequestValidator implements IValidator<LoginRequest> {
    @Override
    @Nonnull
    public ValidateResult validate(@Nonnull LoginRequest request) {
        Result<ValidateResult, ConstraintNonnullException> checkNonnullResult = CheckNonNull.checkNonNull(LoginRequest.class, request)
                .map(value -> ValidateResult.Valid);


        if (checkNonnullResult.isErr()) {
            return new ValidateResult.InValid(checkNonnullResult.getError().getMessage());
        } else {
            return ValidateResult.Valid;
        }
    }
}
