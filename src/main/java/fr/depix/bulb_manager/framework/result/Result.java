package fr.depix.bulb_manager.framework.result;

import java.util.List;

public sealed interface Result<S, E> {

    static <S, E> Result<S, E> success(List<S> value) {
        return new Success<>(value);
    }

    static <S, E> Result<S, E> failure(List<E> errors) {
        return new Failure<>(errors);
    }

    record Success<S, E>(List<S> value) implements Result<S, E> {

    }

    record Failure<S, E>(List<E> errors) implements Result<S, E> {

    }
}
