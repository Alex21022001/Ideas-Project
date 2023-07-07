package com.alexsitiy.ideas.project.util;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicate {

    private final List<Predicate> predicates = new ArrayList<>();

    public static QPredicate builder() {
        return new QPredicate();
    }

    public <T> QPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            this.predicates.add(function.apply(object));
        }
        return this;
    }

    public Predicate buildAll() {
        return Optional.ofNullable(ExpressionUtils.allOf(this.predicates))
                .orElse(Expressions.asBoolean(true).isTrue());
    }

    public Predicate buildAny() {
        return Optional.ofNullable(ExpressionUtils.anyOf(this.predicates))
                .orElse(Expressions.asBoolean(true).isTrue());
    }

}
