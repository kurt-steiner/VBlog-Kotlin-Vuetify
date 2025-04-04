package com.steiner.vblog.validate.impl;

import com.steiner.vblog.dto.request.PutArticleRequest;
import com.steiner.vblog.exception.ConstraintNonnullException;
import com.steiner.vblog.table_metadata.ArticlesMetadata;
import com.steiner.vblog.util.CheckNonNull;
import com.steiner.vblog.util.result.Result;
import com.steiner.vblog.validate.IValidator;
import com.steiner.vblog.validate.ValidateResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class PutArticleRequestValidator implements IValidator<PutArticleRequest> {
    @Resource
    ArticlesMetadata metadata;


    @Nonnull
    @Override
    public ValidateResult validate(@Nonnull PutArticleRequest request) {
        Result<ValidateResult, ConstraintNonnullException> checkNonnullResult = CheckNonNull.checkNonNull(PutArticleRequest.class, request)
                .map(value -> ValidateResult.Valid);

        if (checkNonnullResult.isErr()) {
            return new ValidateResult.InValid(checkNonnullResult.getError().getMessage());
        }

        if (!(request.title != null && !request.title.isEmpty() && request.title.length() <= metadata.titleLength)) {
            return new ValidateResult.InValid("title length invalid");
        }

        return ValidateResult.Valid;
    }
}
