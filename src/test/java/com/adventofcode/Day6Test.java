package com.adventofcode;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day6Test {

    @Test
    void solvePart1Sample() {
        List<String> input = sample();

        int result = new Day6().solvePart1(input);

        assertThat(result).isEqualTo(17);
    }

    @Test
    void solvePart1() {
        List<String> input = input();

        int result = new Day6().solvePart1(input);

        assertThat(result).isEqualTo(4976);
    }

    @Test
    void solvePart2Sample() {
        List<String> input = sample();

        int result = new Day6().solvePart2(32, input);

        assertThat(result).isEqualTo(16);
    }

    @Test
    void solvePart2() {
        List<String> input = input();

        int result = new Day6().solvePart2(10000, input);

        assertThat(result).isEqualTo(46462);
    }

    private List<String> sample() {
        return List.of(
                "1, 1",
                "1, 6",
                "8, 3",
                "3, 4",
                "5, 5",
                "8, 9"
        );
    }

    private List<String> input() {
        return List.of(
                "292, 73",
                "204, 176",
                "106, 197",
                "155, 265",
                "195, 59",
                "185, 136",
                "54, 82",
                "209, 149",
                "298, 209",
                "274, 157",
                "349, 196",
                "168, 353",
                "193, 129",
                "94, 137",
                "177, 143",
                "196, 357",
                "272, 312",
                "351, 340",
                "253, 115",
                "109, 183",
                "252, 232",
                "193, 258",
                "242, 151",
                "220, 345",
                "336, 348",
                "196, 203",
                "122, 245",
                "265, 189",
                "124, 57",
                "276, 204",
                "309, 125",
                "46, 324",
                "345, 228",
                "251, 134",
                "231, 117",
                "88, 112",
                "256, 229",
                "49, 201",
                "142, 108",
                "150, 337",
                "134, 109",
                "288, 67",
                "297, 231",
                "310, 131",
                "208, 255",
                "246, 132",
                "232, 45",
                "356, 93",
                "356, 207",
                "83, 97"
        );
    }
}