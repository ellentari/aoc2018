package com.adventofcode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day11Test {

    private static final int INPUT = 1309;

    @ParameterizedTest
    @MethodSource("powerLevelSample")
    void powerLevel(int x, int y, int serialNumber, int expected) {
        int result = Day11.powerLevel(x, y, serialNumber);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void solvePart1Sample() {
        String result = new Day11().solvePart1(42);

        assertThat(result).isEqualTo("21,61");
    }

    @Test
    void solvePart1() {
        String result = new Day11().solvePart1(INPUT);

        assertThat(result).isEqualTo("20,43");
    }

    @Test
    void solvePart2() {
        String result = new Day11().solvePart2(INPUT);

        assertThat(result).isEqualTo("233,271,13");
    }

    @Test
    void solvePart2Sample() {
        String result = new Day11().solvePart2(18);

        assertThat(result).isEqualTo("90,269,16");
    }

    private static Stream<Arguments> powerLevelSample() {
        return Stream.of(
                Arguments.of(122, 79, 57, -5),
                Arguments.of(217, 196, 39, 0),
                Arguments.of(101, 153, 71, 4)
        );
    }
}