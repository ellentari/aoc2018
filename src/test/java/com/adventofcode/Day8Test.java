package com.adventofcode;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day8Test {

    @Test
    void solvePart1Sample() {
        String input = sample();

        int result = new Day8().solvePart1(input);

        assertThat(result).isEqualTo(138);
    }

    @Test
    void solvePart1() {
        String input = input();

        int result = new Day8().solvePart1(input);

        assertThat(result).isEqualTo(37905);
    }

    @Test
    void solvePart2Sample() {
        String input = sample();

        int result = new Day8().solvePart2(input);

        assertThat(result).isEqualTo(66);
    }

    @Test
    void solvePart2() {
        String input = input();

        int result = new Day8().solvePart2(input);

        assertThat(result).isEqualTo(33891);
    }

    private String sample() {
        return "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2";
    }

    private String input() {
        return ResourceUtils.readString("day8.txt");
    }
}