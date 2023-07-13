package com.alexsitiy.ideas.project.dto;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(List<T> data, com.alexsitiy.ideas.project.dto.PageResponse.Metadata metadata) {

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(page.getContent(),
                new Metadata(page.getNumber(), page.getTotalPages(), page.getTotalElements()));
    }

    record Metadata(Integer currentPage, Integer totalPages, Long totalElements) {

    }
}
