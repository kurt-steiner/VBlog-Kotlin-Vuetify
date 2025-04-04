package com.steiner.vblog.util.result;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Result<T, E extends Throwable> {

    private enum ResultType {
        Ok,
        Err
    }

    public static <T, E extends Throwable> Result<T, E> Ok(@Nonnull T data) {
        return new Result<>(data, null, ResultType.Ok);
    }

    public static <T, E extends Throwable> Result<T, E> Ok() {
        return new Result<>(null, null, ResultType.Ok);
    }

    public static <T, E extends Throwable> Result<T, E> Err(@Nonnull E error) {
        return new Result<>(null, error, ResultType.Err);
    }

    public static <T, E extends Throwable> Result<T, E> from(@Nonnull SupplierThrows<T, E> supplier) {
        try {
            @Nullable T result = supplier.supplier();
            if (result == null) {
                throw new NullPointerException("calling supplier() returns null");
            }

            return new Result<>(result, null, ResultType.Ok);
        } catch (NullPointerException exception) {
            throw exception;
        } catch (Throwable throwable) {
            @SuppressWarnings("unchecked")
            E exception = (E) throwable;
            return new Result<>(null, exception, ResultType.Err);
        }
    }


    @Nullable
    private final T data;

    @Nullable
    private final E error;

    @Nonnull
    private final ResultType resultType;

    private Result(@Nullable T data, @Nullable E error, @Nonnull ResultType resultType) {
        this.data = data;
        this.error = error;
        this.resultType = resultType;
    }

    public boolean isOk() {
        return resultType == ResultType.Ok;
    }

    public boolean isErr() {
        return resultType == ResultType.Err;
    }

    public void ifOk(@Nonnull Consumer<T> consumer) {
        if (isOk()) {
            consumer.accept(get());
        }
    }

    public void ifErr(@Nonnull Consumer<E> consumer) {
        if (isErr()) {
            consumer.accept(getError());
        }
    }

    @Nonnull
    public <U> Result<U, E> map(@Nonnull Function<T, U> function) {
        if (isOk()) {
            return Result.Ok(function.apply(data)); // do not use get(), the `Void` returns null
        } else {
            return Result.Err(this.getError());
        }
    }

    @Nonnull
    public T get() {
        if (data == null) {
            throw new UnsupportedException("cannot unwrap null value");
        }

        if (isErr()) {
            throw new UnsupportedException("cannot unwrap result err");
        }

        return data;
    }

    @Nonnull
    public T orElse(@Nonnull T value) {
        if (isOk()) {
            return get();
        } else {
            return value;
        }
    }

    @Nonnull
    public T orElseGet(@Nonnull Supplier<? extends T> supplier) {
        if (isOk()) {
            return get();
        } else {
            return supplier.get();
        }
    }

    @Nonnull
    public E getError() {
        if (error == null) {
            throw new UnsupportedException("cannot unwrap null error");
        }

        if (isOk()) {
            throw new UnsupportedException("cannot unwrap result ok");
        }

        return error;
    }
}
