package com.steiner.vblog.validate.impl;

import com.steiner.vblog.dto.request.PutCategoryRequest;
import com.steiner.vblog.exception.ConstraintNonnullException;
import com.steiner.vblog.table_metadata.CategoriesMetadata;
import com.steiner.vblog.util.CheckNonNull;
import com.steiner.vblog.util.result.Result;
import com.steiner.vblog.validate.IValidator;
import com.steiner.vblog.validate.ValidateResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class PutCategoryRequestValidator implements IValidator<PutCategoryRequest> {
    @Resource
    CategoriesMetadata metadata;

    @Nonnull
    @Override
    public ValidateResult validate(@Nonnull PutCategoryRequest request) {
        Result<ValidateResult, ConstraintNonnullException> checkNonnullResult = CheckNonNull.checkNonNull(PutCategoryRequest.class, request)
                .map(value -> ValidateResult.Valid);

        if (checkNonnullResult.isErr()) {
            return new ValidateResult.InValid(checkNonnullResult.getError().getMessage());
        }

        if (!(!request.name.isEmpty() && request.name.length() <= metadata.nameLength)) {
            return new ValidateResult.InValid("name length invalid");
        }

        return ValidateResult.Valid;
    }
}
