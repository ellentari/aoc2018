package com.adventofcode;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day7Test {

    @Test
    void solvePart1Sample() {
        List<String> input = sample();

        String result = new Day7().solvePart1(input);

        assertThat(result).isEqualTo("CABDFE");
    }

    @Test
    void solvePart1() {
        List<String> input = input();

        String result = new Day7().solvePart1(input);

        assertThat(result).isEqualTo("GRTAHKLQVYWXMUBCZPIJFEDNSO");
    }

    @Test
    void solvePart2Sample() {
        List<String> input = sample();

        int result = new Day7().solvePart2(2, 0, input);

        assertThat(result).isEqualTo(15);
    }

    @Test
    void solvePart2() {
        List<String> input = input();

        int result = new Day7().solvePart2(5, 60, input);

        assertThat(result).isEqualTo(1115);
    }

    private List<String> sample() {
        return List.of(
                "Step C must be finished before step A can begin.",
                "Step C must be finished before step F can begin.",
                "Step A must be finished before step B can begin.",
                "Step A must be finished before step D can begin.",
                "Step B must be finished before step E can begin.",
                "Step D must be finished before step E can begin.",
                "Step F must be finished before step E can begin."
        );
    }

    private List<String> input() {
        return ResourceUtils.readLines("day7.txt");
    }
}