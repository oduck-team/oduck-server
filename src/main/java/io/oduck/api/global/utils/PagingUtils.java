package io.oduck.api.global.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public class PagingUtils {

    public static Pageable applyPageableForNonOffset(String property, String direction, int size) {
        return PageRequest.of(0, size, direction.equals("desc") ? Direction.DESC : Direction.ASC, property);
    }

    public static Pageable applyPageableForOffset(int page, String property, String direction, int size) {
        return PageRequest.of(page, size, direction.equals("desc") ? Direction.DESC : Direction.ASC, property);
    }
}
