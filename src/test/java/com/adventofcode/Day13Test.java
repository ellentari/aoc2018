package com.adventofcode;

import io.vavr.collection.Array;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day13Test {

    @Test
    void solvePart1Sample() {
        Array<String> input = Array.of(
                "/->-\\        ",
                "|   |  /----\\",
                "| /-+--+-\\  |",
                "| | |  | v  |",
                "\\-+-/  \\-+--/",
                "  \\------/   "
        );

        String result = new Day13().solvePart1(input);

        assertThat(result).isEqualTo("7,3");
    }

    @Test
    void solvePart2Sample1() {
        Array<String> input = Array.of(
                "/>-<\\  ",
                "|   |  ",
                "| /<+-\\",
                "| | | v",
                "\\>+</ |",
                "  |   ^",
                "  \\<->/"
        );

        String result = new Day13().solvePart2(input);

        assertThat(result).isEqualTo("6,4");
    }

    @Test
    void solvePart2Sample2() {
        Array<String> input = Array.of(
                "/---\\    ",
                "| />+<--\\",
                "| | ^   |",
                "\\-+-/   |",
                "  \\-----/"
        );

        String result = new Day13().solvePart2(input);

        assertThat(result).isEqualTo("4,1");
    }

    @Test
    void solvePart1() {
        Array<String> input = ResourceUtils.readLines("day13.txt").toArray();

        String result = new Day13().solvePart1(input);

        assertThat(result).isEqualTo("113,136");
    }

    @Test
    void solvePart2() {
        Array<String> input = ResourceUtils.readLines("day13.txt").toArray();

        String result = new Day13().solvePart2(input);

        assertThat(result).isEqualTo("114,136");
    }
}