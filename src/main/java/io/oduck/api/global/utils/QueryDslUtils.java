package io.oduck.api.global.utils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class QueryDslUtils {
    // JPAQuery를 이용하여 Slice를 생성하여 반환.
    public static <T> Slice<T> fetchSliceByCursor(List<Path> paths, JPAQuery<T> query, Pageable pageable) {
        Sort sort = pageable.getSort();

        int pageSize = pageable.getPageSize();

        List<T> content = query
            .orderBy(getAllOrderSpecifiers(sort, paths))
            .limit(pageSize + 1)
            .fetch();

        return new SliceImpl<>(content, pageable, isHasNext(pageSize, content));
    }

    public static <T> Page<T> fetchPage(List<Path> paths, JPAQuery<T> query, long total, Pageable pageable) {
        Sort sort = pageable.getSort();

        List<T> content = query
            .orderBy(getAllOrderSpecifiers(sort, paths))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();
        return new PageImpl<>(content, pageable, total);
    }

    // sort.order 객체로 OrderSpecifier를 생성하여 반환.
    public static OrderSpecifier[] getAllOrderSpecifiers(Sort sort, List<Path> paths) {
        List<OrderSpecifier> orders = convertToDslOrder(sort, paths);
        return orders.toArray(OrderSpecifier[]::new);
    }

    private static List<OrderSpecifier> convertToDslOrder(Sort sort, List<Path> paths) {
        List<OrderSpecifier> orders = new ArrayList<>();
        Iterator<Path> iterator = paths.iterator();
        if (!sort.isEmpty()) {
            for (Sort.Order order : sort) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                Path<?> path = iterator.next();

                OrderSpecifier<?> orderBy = createOrderSpecifier(direction, path, order.getProperty());
                orders.add(orderBy);
            }
        }
        return orders;
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