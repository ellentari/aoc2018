package com.adventofcode;

import io.vavr.collection.Array;
import io.vavr.collection.IndexedSeq;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day2Test {

    @Test
    void solvePart1Sample() {
        IndexedSeq<String> input = sample1();

        long result = new Day2().solvePart1(input);

        assertThat(result).isEqualTo(12);
    }

    @Test
    void solvePart1() {
        IndexedSeq<String> input = input();

        long result = new Day2().solvePart1(input);

        assertThat(result).isEqualTo(6888);
    }

    @Test
    void solvePart2Sample() {
        IndexedSeq<String> input = sample2();

        String result = new Day2().solvePart2(input);

        assertThat(result).isEqualTo("fgij");
    }

    @Test
    void solvePart2() {
        IndexedSeq<String> input = input();

        String result = new Day2().solvePart2(input);

        assertThat(result).isEqualTo("icxjvbrobtunlelzpdmfkahgs");
    }

    private IndexedSeq<String> sample1() {
        return Array.of(
                "abcdef",
                "bababc",
                "abbcde",
                "abcccd",
                "aabcdd",
                "abcdee",
                "ababab"
        );
    }

    private IndexedSeq<String> sample2() {
        return Array.of(
                "abcde",
                "fghij",
                "klmno",
                "pqrst",
                "fguij",
                "axcye",
                "wvxyz"
        );
    }

    private IndexedSeq<String> input() {
        return Array.ofAll(ResourceUtils.readLines("day2.txt"));
    }
}