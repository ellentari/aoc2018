package com.adventofcode;

import io.vavr.collection.Array;
import io.vavr.collection.IndexedSeq;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day2Test {

    @Test
    void solvePart1() {
        Stream<String> input = ResourceUtils.read("day2.txt");

        long result = new Day2().solvePart1(input);

        assertThat(result).isEqualTo(6888);
    }

    @Test
    void solvePart1Sample() {
        Stream<String> input = Stream.of(
                "abcdef",
                "bababc",
                "abbcde",
                "abcccd",
                "aabcdd",
                "abcdee",
                "ababab"
        );

        long result = new Day2().solvePart1(input);

        assertThat(result).isEqualTo(12);
    }

    @Test
    void solvePart2Sample() {
        IndexedSeq<String> input = Array.of(
                "abcde",
                "fghij",
                "klmno",
                "pqrst",
                "fguij",
                "axcye",
                "wvxyz"
        );

        String result = new Day2().solvePart2(input);

        assertThat(result).isEqualTo("fgij");
    }

    @Test
    void solvePart2() {
        IndexedSeq<String> input = Array.ofAll(ResourceUtils.read("day2.txt"));

        String result = new Day2().solvePart2(input);

        assertThat(result).isEqualTo("icxjvbrobtunlelzpdmfkahgs");
    }
}