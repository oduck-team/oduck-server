package io.oduck.api.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SingleResponse<T> {
    private T item;

    public static <T> SingleResponse<T> of(T item) {
        return new SingleResponse<>(item);
    }
}