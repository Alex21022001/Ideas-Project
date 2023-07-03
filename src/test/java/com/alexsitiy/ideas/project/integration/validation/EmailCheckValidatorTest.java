package com.alexsitiy.ideas.project.integration.validation;

import com.alexsitiy.ideas.project.integration.IntegrationTestBase;
import com.alexsitiy.ideas.project.validation.EmailCheckValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.TestConstructor;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class EmailCheckValidatorTest extends IntegrationTestBase {

    private final EmailCheckValidator emailCheckValidator;

    @ParameterizedTest
    @MethodSource("methodData")
    void isValid(String username, boolean expectedResult) {
        boolean actualResult = emailCheckValidator.isValid(username, null);

        assertEquals(expectedResult,actualResult);
    }

    static Stream<Arguments> methodData() {
        return Stream.of(
                Arguments.of("newtest@gmail.com", true),
                Arguments.of("test1@gmail.com", false)
        );
    }
}