package com.adventofcode;


import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day16Test {

    @Test
    void solvePart1Sample() {
        List<String> input = List.of(
                "Before: [3, 2, 1, 1]",
                "9 2 1 2",
                "After:  [3, 2, 2, 1]",
                ""
        );

        int result = new Day16().solvePart1(input);

        assertThat(result).isEqualTo(1);
    }

    @Test
    void solvePart1() {
        List<String> input = ResourceUtils.readLines("day16.1.txt");

        int result = new Day16().solvePart1(input);

        assertThat(result).isEqualTo(612);
    }

    @Test
    void solvePart2() {
        List<String> input1 = ResourceUtils.readLines("day16.1.txt");
        List<String> input2 = ResourceUtils.readLines("day16.2.txt");

        long result = new Day16().solvePart2(input1, input2);

        assertThat(result).isEqualTo(485);
    }

}