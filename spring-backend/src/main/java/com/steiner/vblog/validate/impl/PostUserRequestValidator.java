package com.steiner.vblog.validate.impl;

import com.steiner.vblog.dto.request.PostUserRequest;
import com.steiner.vblog.exception.ConstraintNonnullException;
import com.steiner.vblog.table_metadata.UsersMetadata;
import com.steiner.vblog.util.CheckNonNull;
import com.steiner.vblog.util.result.Result;
import com.steiner.vblog.validate.IValidator;
import com.steiner.vblog.validate.ValidateResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class PostUserRequestValidator implements IValidator<PostUserRequest> {
    @Resource
    UsersMetadata metadata;

    @Nonnull
    @Override
    public ValidateResult validate(@Nonnull PostUserRequest request) {
        Result<ValidateResult, ConstraintNonnullException> checkNonnullResult = CheckNonNull.checkNonNull(PostUserRequest.class, request)
                .map(value -> ValidateResult.Valid);

        if (checkNonnullResult.isErr()) {
            return new ValidateResult.InValid(checkNonnullResult.getError().getMessage());
        }

        if (!(!request.name.isEmpty() && request.name.length() <= metadata.nameLength)) {
            return new ValidateResult.InValid("name length invalid");
        }

        if (!(!request.passwordHash.isEmpty() && request.passwordHash.length() <= metadata.passwordHashLength)) {
            return new ValidateResult.InValid("password hash length invalid");
        }

        if (!(request.nickname != null && !request.nickname.isEmpty() && request.nickname.length() <= metadata.nicknameLength)) {
            return new ValidateResult.InValid("nickname length invalid");
        }

        if (!(request.email != null && !request.email.isEmpty() && request.email.length() <= metadata.emailLength)) {
            return new ValidateResult.InValid("email length invalid");
        }

        if (!(request.avatarId != null && request.avatarId > 0)) {
            return new ValidateResult.InValid("avatar id invalid");
        }

        return ValidateResult.Valid;
    }
}
