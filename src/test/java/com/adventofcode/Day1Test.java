package com.adventofcode;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day1Test {

    @Test
    void solvePart1() {
        List<String> input = input();

        int solution = new Day1().solvePart1(input);

        assertThat(solution).isEqualTo(427);
    }

    @Test
    void solvePart2() {
        List<String> input = input();

        int solution = new Day1().solvePart2(input);

        assertThat(solution).isEqualTo(341);
    }

    private List<String> input() {
        return ResourceUtils.readLines("day1.txt");
    }
}