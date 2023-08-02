package com.alexsitiy.ideas.project.dto.sort;

import org.springframework.data.domain.Sort.Order;

import java.util.List;
import java.util.Map;

public class NotificationSort extends BasicSort {

    public NotificationSort(Integer page, Integer size, List<String> sortList) {
        super(page, size, sortList);
    }

    @Override
    Map<String, Order> getAllowedOrders() {
        return Map.of("new", Order.desc("createdAt"));
    }

    @Override
    void addDefaultOrder(List<Order> orders) {
        orders.add(Order.desc("createdAt"));
    }
}
