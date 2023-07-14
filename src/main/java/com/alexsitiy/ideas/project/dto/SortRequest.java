package com.alexsitiy.ideas.project.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SortRequest {
    private Integer page;
    private Integer size;
    private List<String> sortList;

    public Pageable getPageable() {
        List<Sort.Order> sortOrderList = new ArrayList<>();

        for (String sort : sortList) {
            switch (sort) {
                case "dislikes" -> sortOrderList.add(Sort.Order.desc("reaction.dislikes"));
                case "title" -> sortOrderList.add(Sort.Order.asc("title"));
                case "likes" -> sortOrderList.add(Sort.Order.desc("reaction.likes"));
            }
        }

        return PageRequest.of(this.page, this.size, Sort.by(sortOrderList));
    }
}
