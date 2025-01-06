package com.steiner.vblog.plugin

import com.steiner.vblog.ArticleConfigure
import com.steiner.vblog.CategoryConfigure
import com.steiner.vblog.TagConfigure
import com.steiner.vblog.UserConfigure
import com.steiner.vblog.request.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import java.util.regex.Pattern

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<LoginRequest> { request ->
            if (request.name.isEmpty()) {
                ValidationResult.Invalid("username cannot be empty")
            }

            if (request.passwordHash.isEmpty()) {
                ValidationResult.Invalid("password cannot be empty")
            }

            ValidationResult.Valid
        }

        validate<PostArticleRequest> { request ->
            if (request.title.isEmpty()) {
                ValidationResult.Invalid("title cannot be empty")
            }

            if (request.title.length > ArticleConfigure.TITLE_LENGTH) {
                ValidationResult.Invalid("title too long")
            }

            if (request.markdownContent.isEmpty()) {
                ValidationResult.Invalid("markdown content cannot be empty")
            }

            if (request.htmlContent.isEmpty()) {
                ValidationResult.Invalid("html content cannot be empty")
            }

            ValidationResult.Valid
        }

        validate<PostCategoryRequest> { request ->
            if (request.name.isEmpty()) {
                ValidationResult.Invalid("name cannot be empty")
            }

            if (request.name.length > CategoryConfigure.NAME_LENGTH) {
                ValidationResult.Invalid("name too long")
            }

            ValidationResult.Valid
        }

        validate<PostTagRequest> { request ->
            if (request.name.isEmpty()) {
                ValidationResult.Invalid("name cannot be empty")
            }

            if (request.name.length > TagConfigure.NAME_LENGTH) {
                ValidationResult.Invalid("name too long")
            }

            ValidationResult.Valid
        }

        validate<RegisterRequest> { request ->
            if (request.name.isEmpty()) {
                ValidationResult.Invalid("name cannot be empty")
            }

            if (request.name.length > UserConfigure.NAME_LENGTH) {
                ValidationResult.Invalid("name too long")
            }

            if (request.passwordHash.isEmpty()) {
                ValidationResult.Invalid("password hash cannot be null")
            }

            if (request.nickname != null && request.nickname.length > UserConfigure.NICKNAME_LENGTH) {
                ValidationResult.Invalid("nickname too long")
            }

            if (request.email != null && request.email.length > UserConfigure.EMAIL_LENGTH) {
                ValidationResult.Invalid("email too long")
            }

            ValidationResult.Valid
        }

        validate<PutArticleRequest> { request ->
            if (request.title != null && request.title.length > ArticleConfigure.TITLE_LENGTH) {
                ValidationResult.Invalid("title too long")
            }

            if (request.markdownContent != null && request.markdownContent.isEmpty()) {
                ValidationResult.Invalid("markdown cannot be empty")
            }

            if (request.htmlContent != null && request.htmlContent.isEmpty()) {
                ValidationResult.Invalid("html content cannot be empty")
            }

            ValidationResult.Valid
        }

        validate<PutCategoryRequest> { request ->
            if (request.name.length > CategoryConfigure.NAME_LENGTH) {
                ValidationResult.Invalid("name too long")
            }

            ValidationResult.Valid
        }

        validate<PutTagRequest> { request ->
            if (request.name.length > CategoryConfigure.NAME_LENGTH) {
                ValidationResult.Invalid("name too long")
            }

            ValidationResult.Valid
        }

        validate<PutUserRequest> { request ->
            if (request.name != null) {
                if (request.name.isEmpty()) {
                    ValidationResult.Invalid("name cannot be empty")
                }

                if (request.name.length > UserConfigure.NAME_LENGTH) {
                    ValidationResult.Invalid("name too long")
                }
            }

            if (request.nickname != null) {
                if (request.nickname.isEmpty()) {
                    ValidationResult.Invalid("nickname cannot be empty")
                }

                if (request.nickname.length > UserConfigure.NICKNAME_LENGTH) {
                    ValidationResult.Invalid("nickname too long")
                }
            }

            if (request.passwordHash != null) {
                if (request.passwordHash.isEmpty()) {
                    ValidationResult.Invalid("password hash cannot be empty")
                }

                if (request.passwordHash.length > UserConfigure.PASSWORD_LENGTH) {
                    ValidationResult.Invalid("password hash too long")
                }
            }

            if (request.email != null) {
                if (request.email.isEmpty()) {
                    ValidationResult.Invalid("email cannot be empty")
                }

                if (request.email.length > UserConfigure.EMAIL_LENGTH) {
                    ValidationResult.Invalid("email too long")
                }

                val flag = Pattern.compile(
                    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
                ).matcher(request.email).matches()

                if (!flag) {
                    ValidationResult.Invalid("email format invalid")
                }
            }

            ValidationResult.Valid
        }
    }
}