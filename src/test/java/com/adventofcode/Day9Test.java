package com.adventofcode;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day9Test {

    @ParameterizedTest
    @MethodSource("part1Samples")
    void solvePart1(int players, int marbles, int expectedResult) {
        long result = new Day9().solvePart1(players, marbles);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void solvePart1() {
        int players = 458;
        int marbles = 72_019;

        long result = new Day9().solvePart1(players, marbles);

        assertThat(result).isEqualTo(404502L);
    }


    @Test
    @Disabled("takes forever to complete")
    void solvePart2() {
        int players = 458;
        int marbles = 7_201_900;

        long result = new Day9().solvePart1(players, marbles);

        assertThat(result).isEqualTo(3243916887L);
    }

    private static Stream<Arguments> part1Samples() {
        return Stream.of(
                Arguments.of(5, 25, 32),
                Arguments.of(10, 1618, 8317),
                Arguments.of(13, 7999, 146373),
                Arguments.of(17, 1104, 2764),
                Arguments.of(21, 6111, 54718),
                Arguments.of(30, 5807, 37305)
        );
    }
}