package com.alexsitiy.ideas.project.dto.sort;

import org.springframework.data.domain.Sort.Order;

import java.util.List;
import java.util.Map;

public class ProjectSort extends BasicSort {

    public ProjectSort(Integer page, Integer size, List<String> sortList) {
        super(page, size, sortList);
    }

    @Override
    Map<String, Order> getAllowedOrders() {
        return Map.of(
                "likes", Order.desc("reaction.likes"),
                "dislikes", Order.desc("reaction.dislikes"),
                "title", Order.asc("title"),
                "new", Order.desc("createdAt")
        );
    }

    @Override
    void addDefaultOrder(List<Order> orders) {
        orders.add(Order.desc("reaction.likes"));
    }
}
