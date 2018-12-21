package com.adventofcode;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day21Test {

    @Test
    void solvePart1() {
        int result = new Day21().solvePart1();

        assertThat(result).isEqualTo(16134795);
    }

    @Test
    void solvePart2() {
        int result = new Day21().solvePart2();

        assertThat(result).isEqualTo(14254292);
    }
}