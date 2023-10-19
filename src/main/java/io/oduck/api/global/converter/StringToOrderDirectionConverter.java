package io.oduck.api.global.converter;

import io.oduck.api.global.common.OrderDirection;
import org.springframework.core.convert.converter.Converter;

public class StringToOrderDirectionConverter implements Converter<String, OrderDirection> {

    @Override
    public OrderDirection convert(String order) {
        return OrderDirection.valueOf(order.toUpperCase());
    }
}