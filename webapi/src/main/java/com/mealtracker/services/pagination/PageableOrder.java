package com.mealtracker.services.pagination;

import org.springframework.data.domain.Sort.Order;

import java.util.function.Function;

public enum PageableOrder {

    ASC(Order::asc),
    DESC(Order::desc),
    ;

    private final Function<String, Order> orderFactory;

    PageableOrder(Function<String, Order> orderFactory) {
        this.orderFactory = orderFactory;
    }

    public Order of(String columnName) {
        return orderFactory.apply(columnName);
    }
}
