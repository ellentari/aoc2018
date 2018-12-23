package com.adventofcode;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Day23Test {

    @Test
    void solvePart1Sample() {
        List<String> input = List.of(
                "pos=<0,0,0>, r=4",
                "pos=<1,0,0>, r=1",
                "pos=<4,0,0>, r=3",
                "pos=<0,2,0>, r=1",
                "pos=<0,5,0>, r=3",
                "pos=<0,0,3>, r=1",
                "pos=<1,1,1>, r=1",
                "pos=<1,1,2>, r=1",
                "pos=<1,3,1>, r=1"
        );

        int result = new Day23().solvePart1(input);

        assertThat(result).isEqualTo(7);
    }

    @Test
    void solvePart2Sample() {
        List<String> input = List.of(
                "pos=<10,12,12>, r=2",
                "pos=<12,14,12>, r=2",
                "pos=<16,12,12>, r=4",
                "pos=<14,14,14>, r=6",
                "pos=<50,50,50>, r=200",
                "pos=<10,10,10>, r=5"
        );

        int result = new Day23().solvePart2(input);

        assertThat(result).isEqualTo(36);
    }

    @Test
    void solvePart1() {
        List<String> input = ResourceUtils.readLines("day23.txt");

        int result = new Day23().solvePart1(input);

        assertThat(result).isEqualTo(305);
    }

    @Test
    void solvePart2() {
        List<String> input = ResourceUtils.readLines("day23.txt");

        int result = new Day23().solvePart2(input);

        assertThat(result).isEqualTo(78687716);
    }
}