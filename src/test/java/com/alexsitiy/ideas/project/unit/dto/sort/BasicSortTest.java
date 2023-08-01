package com.alexsitiy.ideas.project.unit.dto.sort;

import com.alexsitiy.ideas.project.dto.sort.BasicSort;
import com.alexsitiy.ideas.project.dto.sort.ProjectSort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class BasicSortTest {
    private static final Integer PAGE = 0;
    private static final Integer SIZE = 10;

    private BasicSort basicSort;

    @BeforeEach
    void init() {
        basicSort = new ProjectSort(PAGE, SIZE, List.of("new", "likes", "dislikes", "invalid"));
    }

    @Test
    void getPageable() {
        Pageable pageable = basicSort.getPageable();

        assertEquals(PAGE, pageable.getPageNumber());
        assertEquals(SIZE, pageable.getPageSize());
        Assertions.assertThat(pageable.getSort().get())
                .hasSize(3)
                .contains(Order.desc("createdAt"),
                        Order.desc("reaction.likes"),
                        Order.desc("reaction.dislikes"));
    }
}