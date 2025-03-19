package com.steiner.vblog.model.article;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ArticleStatus {
    Draft(0),
    Published(1),
    Dustbin(2);

    public final int code;
    ArticleStatus(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static ArticleStatus fromCode(int code) {
        for (ArticleStatus status : ArticleStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid status code: %s".formatted(code));
    }
}
