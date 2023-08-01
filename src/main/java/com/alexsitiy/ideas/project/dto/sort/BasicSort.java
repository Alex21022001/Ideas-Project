package com.alexsitiy.ideas.project.dto.sort;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class BasicSort {
    private final Integer page;
    private final Integer size;
    private final List<String> sortList;

    public Pageable getPageable() {
        List<Order> sortOrderList = new ArrayList<>();
        Map<String, Order> allowedOrders = getAllowedOrders();

        for (String sort : sortList) {
            Order maybeOrder = allowedOrders.get(sort);

            if (maybeOrder != null) {
                sortOrderList.add(maybeOrder);
            }
        }

        if (sortOrderList.isEmpty()) {
            addDefaultOrder(sortOrderList);
        }

        return PageRequest.of(this.page, this.size, Sort.by(sortOrderList));
    }

    public static BasicSort of(int page, int size, List<String> sortList, Class<?> clazz) {
        if (clazz == NotificationSort.class) {
            return new NotificationSort(page, size, sortList);
        } else if (clazz == ProjectSort.class) {
            return new ProjectSort(page, size, sortList);
        } else {
            throw new IllegalArgumentException();
        }
    }

    abstract Map<String, Order> getAllowedOrders();

    abstract void addDefaultOrder(List<Order> orders);
}