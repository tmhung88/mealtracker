package com.mealtracker.services.pagination;

import lombok.Value;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Value
public class SortableProperty {
    static final int HIGHEST_ORDER = 0;
    static Comparator<SortableProperty> BY_PRIORITY = Comparator.comparing(SortableProperty::getPriority);
    private final int priority;
    private final String name;
    private final PageableOrder order;

    public static List<SortableProperty> singlePropertyOrder(String propertyName, PageableOrder order) {
        return Collections.singletonList(new SortableProperty(HIGHEST_ORDER, propertyName, order));
    }

    public Sort.Order toSpringOrder() {
        return order.of(name);
    }
}
