package com.steiner.vblog.validate.impl;

import com.steiner.vblog.dto.request.PutTagRequest;
import com.steiner.vblog.exception.ConstraintNonnullException;
import com.steiner.vblog.table_metadata.TagsMetadata;
import com.steiner.vblog.util.CheckNonNull;
import com.steiner.vblog.util.result.Result;
import com.steiner.vblog.validate.IValidator;
import com.steiner.vblog.validate.ValidateResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class PutTagRequestValidator implements IValidator<PutTagRequest> {
    @Resource
    TagsMetadata metadata;

    @Nonnull
    @Override
    public ValidateResult validate(@Nonnull PutTagRequest request) {
        Result<ValidateResult, ConstraintNonnullException> checkNonnullResult = CheckNonNull.checkNonNull(PutTagRequest.class, request)
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
