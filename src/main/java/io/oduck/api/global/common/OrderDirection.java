package io.oduck.api.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderDirection {
    ASC("asc"),
    DESC("desc");

    private final String order;
}
