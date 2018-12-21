package com.adventofcode;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Day19Test {

    @Test
    void part1Sample() {
        List<String> input = List.of(
                "seti 5 0 1",
                "seti 6 0 2",
                "addi 0 1 0",
                "addr 1 2 3",
                "setr 1 0 0",
                "seti 8 0 4",
                "seti 9 0 5"
        );

        int result = new Day19().solvePart1(0, 0, input);

        assertThat(result).isEqualTo(6);
    }

    @Test
    void part1() {
        int result = new Day19().solvePart1(0, 3, input());

        assertThat(result).isEqualTo(968);

    }

    private List<String> input() {
        return List.of(
                "addi 3 16 3", // 0 jump to 17
                "seti 1 8 1",  // 1
                "seti 1 3 4",  // 2
                "mulr 1 4 2",  // 3
                "eqrr 2 5 2",  // 4
                "addr 2 3 3",  // 5
                "addi 3 1 3",  // 6
                "addr 1 0 0",  // 7
                "addi 4 1 4",  // 8
                "gtrr 4 5 2",  // 9
                "addr 3 2 3",  // 10
                "seti 2 6 3",  // 11
                "addi 1 1 1",  // 12
                "gtrr 1 5 2",  // 13
                "addr 2 3 3",  // 14
                "seti 1 5 3",  // 15
                "mulr 3 3 3",  // 16
                "addi 5 2 5",  // 17
                "mulr 5 5 5",  // 18
                "mulr 3 5 5",  // 19
                "muli 5 11 5",  // 20
                "addi 2 5 2",  // 21
                "mulr 2 3 2",  // 22
                "addi 2 21 2",  // 23
                "addr 5 2 5",  // 24
                "addr 3 0 3",  // 25
                "seti 0 4 3",  // 26
                "setr 3 1 2",  // 27
                "mulr 2 3 2",  // 28
                "addr 3 2 2",  // 29
                "mulr 3 2 2",  // 30
                "muli 2 14 2",  // 31
                "mulr 2 3 2",  // 32
                "addr 5 2 5",  // 33
                "seti 0 3 0",  // 34
                "seti 0 6 3"  // 35
        );
    }
}