package com.alexsitiy.ideas.project.integration.validation;

import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.alexsitiy.ideas.project.validation.TitleCheckValidator;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Argument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class TitleCheckValidatorTest extends IntegrationTestBase {

    private final TitleCheckValidator titleCheckValidator;

    @ParameterizedTest
    @MethodSource("titleCheckData")
    void isValid(String title, boolean expected) {
        boolean actual = titleCheckValidator.isValid(title, null);

        assertEquals(expected,actual);
    }

    static Stream<Arguments> titleCheckData(){
        return Stream.of(
                Arguments.of("newTitle",true),
                Arguments.of("test1",false)
        );
    }
}