package com.adventofcode;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day7Test {

    @Test
    void solvePart1Sample() {
        Stream<String> input = sample();

        String result = new Day7().solvePart1(input);

        assertThat(result).isEqualTo("CABDFE");
    }

    @Test
    void solvePart1() {
        Stream<String> input = input();

        String result = new Day7().solvePart1(input);

        assertThat(result).isEqualTo("GRTAHKLQVYWXMUBCZPIJFEDNSO");
    }

    @Test
    void solvePart2Sample() {
        Stream<String> input = sample();

        int result = new Day7().solvePart2(2, 0, input);

        assertThat(result).isEqualTo(15);
    }

    @Test
    void solvePart2() {
        Stream<String> input = input();

        int result = new Day7().solvePart2(5, 60, input);

        assertThat(result).isEqualTo(1115);
    }

    private Stream<String> sample() {
        return Stream.of(
                "Step C must be finished before step A can begin.",
                "Step C must be finished before step F can begin.",
                "Step A must be finished before step B can begin.",
                "Step A must be finished before step D can begin.",
                "Step B must be finished before step E can begin.",
                "Step D must be finished before step E can begin.",
                "Step F must be finished before step E can begin."
        );
    }

    private Stream<String> input() {
        return ResourceUtils.read("day7.txt");
    }
}