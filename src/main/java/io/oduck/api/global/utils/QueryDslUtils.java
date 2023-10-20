package io.oduck.api.global.utils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class QueryDslUtils {
    // JPAQuery를 이용하여 Slice를 생성하여 반환.
    public static <T> Slice<T> fetchSliceByCursor(Path path, JPAQuery<T> query, Pageable pageable) {
        Sort.Order order = pageable.getSort().iterator().next();

        int pageSize = pageable.getPageSize();

        List<T> content = query
            .orderBy(getOrderSpecifier(order, path))
            .limit(pageSize + 1)
            .fetch();

        return new SliceImpl<>(content, pageable, isHasNext(pageSize, content));
    }

    // sort.order 객체로 OrderSpecifier를 생성하여 반환.
    public static OrderSpecifier<?> getOrderSpecifier(Sort.Order order, Path path) {

        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
        return createOrderSpecifier(direction, path, order.getProperty());
    }

    private static OrderSpecifier<?> createOrderSpecifier(Order order, Path<?> parent, String fieldName) {
        // 일반 컬럼을 기준으로 할때의 OrderSpecifier
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);

        return new OrderSpecifier(order, fieldPath);
    }

    private static <T> boolean isHasNext(int pageSize, List<T> content) {
//        boolean hasNext = pageSize <= content.size();
        boolean hasNext = false;
        if (pageSize < content.size()) {
            hasNext = true;
            content.remove(pageSize);
        }
        return hasNext;
    }
}