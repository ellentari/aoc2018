package com.adventofcode;

import io.vavr.collection.Array;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day15Test {

    @Test
    void sample1() {
        Array<String> map = Array.of(
                "#######",
                "#.G...#",
                "#...EG#",
                "#.#.#G#",
                "#..G#E#",
                "#.....#",
                "#######"
        );

        int result = new Day15().solvePart1(map);

        assertThat(result).isEqualTo(27730);
    }

    @Test
    void sample1Part2() {
        Array<String> map = Array.of(
                "#######",
                "#.G...#",
                "#...EG#",
                "#.#.#G#",
                "#..G#E#",
                "#.....#",
                "#######"
        );

        int result = new Day15().solvePart2(map);

        assertThat(result).isEqualTo(4988);
    }

    @Test
    void sample2() {
        Array<String> map = Array.of(
                "#######",
                "#G..#E#",
                "#E#E.E#",
                "#G.##.#",
                "#...#E#",
                "#...E.#",
                "#######"
        );

        int result = new Day15().solvePart1(map);

        assertThat(result).isEqualTo(36334);
    }

    @Test
    void sample3() {
        Array<String> map = Array.of(
                "#######",
                "#E..EG#",
                "#.#G.E#",
                "#E.##E#",
                "#G..#.#",
                "#..E#.#",
                "#######"
        );

        int result = new Day15().solvePart1(map);

        assertThat(result).isEqualTo(39514);
    }

    @Test
    void sample3Part2() {
        Array<String> map = Array.of(
                "#######",
                "#E..EG#",
                "#.#G.E#",
                "#E.##E#",
                "#G..#.#",
                "#..E#.#",
                "#######"
        );

        int result = new Day15().solvePart2(map);

        assertThat(result).isEqualTo(31284);
    }

    @Test
    void sample4() {
        Array<String> map = Array.of(
                "#######",
                "#E.G#.#",
                "#.#G..#",
                "#G.#.G#",
                "#G..#.#",
                "#...E.#",
                "#######"
        );

        int result = new Day15().solvePart1(map);

        assertThat(result).isEqualTo(27755);
    }

    @Test
    void sample4Part2() {
        Array<String> map = Array.of(
                "#######",
                "#E.G#.#",
                "#.#G..#",
                "#G.#.G#",
                "#G..#.#",
                "#...E.#",
                "#######"
        );

        int result = new Day15().solvePart2(map);

        assertThat(result).isEqualTo(3478);
    }

    @Test
    void sample5() {
        Array<String> map = Array.of(
                "#######",
                "#.E...#",
                "#.#..G#",
                "#.###.#",
                "#E#G#G#",
                "#...#G#",
                "#######"
        );

        int result = new Day15().solvePart1(map);

        assertThat(result).isEqualTo(28944);
    }

    @Test
    void sample5Part2() {
        Array<String> map = Array.of(
                "#######",
                "#.E...#",
                "#.#..G#",
                "#.###.#",
                "#E#G#G#",
                "#...#G#",
                "#######"
        );

        int result = new Day15().solvePart2(map);

        assertThat(result).isEqualTo(6474);
    }

    @Test
    void sample6() {
        Array<String> map = Array.of(
                "#########",
                "#G......#",
                "#.E.#...#",
                "#..##..G#",
                "#...##..#",
                "#...#...#",
                "#.G...G.#",
                "#.....G.#",
                "#########"
        );

        int result = new Day15().solvePart1(map);

        assertThat(result).isEqualTo(18740);
    }

    @Test
    void sample6SolvePart2() {
        Array<String> map = Array.of(
                "#########",
                "#G......#",
                "#.E.#...#",
                "#..##..G#",
                "#...##..#",
                "#...#...#",
                "#.G...G.#",
                "#.....G.#",
                "#########"
        );

        int result = new Day15().solvePart2(map);

        assertThat(result).isEqualTo(1140);
    }

//    @Test
    void solvePart1() {
        Array<String> map = ResourceUtils.readLines("day15.txt").toArray();

        int result = new Day15().solvePart1(map);

        assertThat(result).isEqualTo(269430);
    }

//    @Test
    void solvePart2() {
        Array<String> map = ResourceUtils.readLines("day15.txt").toArray();

        int attackPoints = new Day15().solvePart2(map);
        assertThat(attackPoints).isEqualTo(19);

        int result = new Day15().solvePart1(attackPoints, map);

        assertThat(result).isEqualTo(55160);
    }
}