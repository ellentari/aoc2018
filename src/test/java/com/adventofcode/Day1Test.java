package com.adventofcode;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day1Test {

    @Test
    void solvePart1() {
        Stream<String> input = ResourceUtils.read("day1.txt");

        int solution = new Day1().solvePart1(input);

        assertThat(solution).isEqualTo(427);
    }

    @Test
    void solvePart2() {
        List<String> input = List.ofAll(ResourceUtils.read("day1.txt"));

        int solution = new Day1().solvePart2(input);

        assertThat(solution).isEqualTo(341);
    }
}