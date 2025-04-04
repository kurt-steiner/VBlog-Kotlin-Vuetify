package com.steiner.vblog.exception;

// 这个错误类，是处理那些被标注为 @Nonnull 的字段，但是获取的时候却是 null 的情况
// 这个错误被用在 `验证` 阶段
public class ConstraintNonnullException extends Exception {
    public ConstraintNonnullException(String message) {
        super(message);
    }

    public ConstraintNonnullException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstraintNonnullException(Throwable cause) {
        super(cause);
    }
}
