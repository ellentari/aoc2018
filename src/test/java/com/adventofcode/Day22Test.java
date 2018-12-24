package com.adventofcode;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Day22Test {

    @Test
    void solvePart1Sample() {
        int result = new Day22().solvePart1(510, 10, 10);

        assertThat(result).isEqualTo(114);
    }

    @Test
    void solvePart1() {
        int result = new Day22().solvePart1(11820, 7, 782);

        assertThat(result).isEqualTo(6318);
    }

    @Test
    void solvePart2Sample1() {
        int result = new Day22().solvePart2(510, 10, 10);

        assertThat(result).isEqualTo(45);
    }

    @Test
    void solvePart2Sample2() {
        int result = new Day22().solvePart2(6969, 9, 796);

        assertThat(result).isEqualTo(1087);
    }

    @Test
    void solvePart2() {
        int result = new Day22().solvePart2(11820, 7, 782);

        assertThat(result).isEqualTo(1075);
    }
}