package com.adventofcode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day14Test {


    @ParameterizedTest
    @MethodSource("part1Samples")
    void solvePart1Samples(int input, String expected) {
        String result = new Day14().solvePart1(input);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void solvePart1() {
        String result = new Day14().solvePart1(765071);

        assertThat(result).isEqualTo("3171123923");
    }

    @ParameterizedTest
    @MethodSource("part2Samples")
    void solvePart2Samples(String input, String expected) {
        String result = new Day14().solvePart2(input);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void solvePart2() {
        String result = new Day14().solvePart2("765071");

        assertThat(result).isEqualTo("20353748");
    }

    private static Stream<Arguments> part1Samples() {
        return Stream.of(
                Arguments.of(9, "5158916779"),
                Arguments.of(5, "0124515891"),
                Arguments.of(18, "9251071085"),
                Arguments.of(2018, "5941429882")
        );
    }

    private static Stream<Arguments> part2Samples() {
        return Stream.of(
                Arguments.of("51589", "9"),
                Arguments.of("01245", "5"),
                Arguments.of("92510", "18"),
                Arguments.of("59414", "2018")
        );
    }

}