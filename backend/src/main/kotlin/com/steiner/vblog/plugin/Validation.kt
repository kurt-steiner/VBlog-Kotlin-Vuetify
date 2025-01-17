package com.steiner.vblog.plugin

import com.steiner.vblog.ArticleConfigure
import com.steiner.vblog.CategoryConfigure
import com.steiner.vblog.TagConfigure
import com.steiner.vblog.UserConfigure
import com.steiner.vblog.request.*
import com.steiner.vblog.util.emailPattern
import com.steiner.vblog.util.limitLength
import com.steiner.vblog.util.notBlank
import com.steiner.vblog.util.validationChain
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<LoginRequest> { request ->
            validationChain(
                notBlank(request.name, "username"),
                notBlank(request.passwordHash, "passwordHash")
            )
        }

        validate<PostArticleRequest> { request ->
            validationChain(
                notBlank(request.title, "title"),
                limitLength(request.title, maxLength = ArticleConfigure.TITLE_LENGTH, fieldName = "title"),
                notBlank(request.markdownContent, "markdown content"),
                notBlank(request.htmlContent, "html content")
            )
        }

        validate<PostCategoryRequest> { request ->
            validationChain(
                notBlank(request.name, "name"),
                limitLength(request.name, maxLength = CategoryConfigure.NAME_LENGTH)
            )
        }

        validate<PostTagRequest> { request ->
            validationChain(
                notBlank(request.name, "name"),
                limitLength(request.name, maxLength = TagConfigure.NAME_LENGTH)
            )
        }

        validate<RegisterRequest> { request ->
            validationChain(
                notBlank(request.name, "name"),
                limitLength(request.name, maxLength = UserConfigure.NAME_LENGTH, fieldName = "name"),
                notBlank(request.passwordHash, "password hash"),
                notBlank(request.nickname, allowNull = true, fieldName = "nickname"),
                limitLength(request.nickname, maxLength = UserConfigure.NICKNAME_LENGTH, allowNull = true, fieldName = "nickname"),
                limitLength(request.email, maxLength = UserConfigure.EMAIL_LENGTH, allowNull = true, fieldName = "email"),
                emailPattern(request.email, allowNull = true)
            )
        }

        validate<PutArticleRequest> { request ->
            validationChain(
                notBlank(request.title, allowNull = true, fieldName = "title"),
                limitLength(request.title, maxLength = ArticleConfigure.TITLE_LENGTH, fieldName = "title", allowNull = true),
                notBlank(request.markdownContent, allowNull = true, fieldName = "markdown content")
            )
        }

        validate<PutCategoryRequest> { request ->
            validationChain(
                notBlank(request.name, fieldName = "name"),
                limitLength(request.name, fieldName = "name", maxLength = CategoryConfigure.NAME_LENGTH)
            )
        }

        validate<PutTagRequest> { request ->
            validationChain(
                notBlank(request.name, fieldName = "name"),
                limitLength(request.name, fieldName = "name", maxLength = CategoryConfigure.NAME_LENGTH)
            )
        }

        validate<PutUserRequest> { request ->
            validationChain(
                notBlank(request.name, allowNull = true, fieldName = "name"),
                limitLength(request.name, allowNull = true, fieldName = "name", maxLength = UserConfigure.NAME_LENGTH),

                notBlank(request.nickname, allowNull = true, fieldName = "nickname"),
                limitLength(request.nickname, allowNull = true, fieldName = "nickname", maxLength = UserConfigure.NICKNAME_LENGTH),

                notBlank(request.passwordHash, allowNull = true, fieldName = "password hash"),
                limitLength(request.passwordHash, allowNull = true, fieldName = "password hash", maxLength = UserConfigure.PASSWORD_LENGTH),

                notBlank(request.email, allowNull = true, fieldName = "email"),
                limitLength(request.email, allowNull = true, fieldName = "email", maxLength = UserConfigure.EMAIL_LENGTH),
                emailPattern(request.email, allowNull = true)
            )
        }
    }
}