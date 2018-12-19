package com.adventofcode;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class Day17Test {

    @Test
    void part1Sample() {
        List<String> input = List.of(
                "x=495, y=2..7",
                "y=7, x=495..501",
                "x=501, y=3..7",
                "x=498, y=2..4",
                "x=506, y=1..2",
                "x=498, y=10..13",
                "x=504, y=10..13",
                "y=13, x=498..504"
        );

        int result = new Day17().solvePart1(input);

        assertThat(result).isEqualTo(57);
    }

    @Test
    void part2Sample() {
        List<String> input = List.of(
                "x=495, y=2..7",
                "y=7, x=495..501",
                "x=501, y=3..7",
                "x=498, y=2..4",
                "x=506, y=1..2",
                "x=498, y=10..13",
                "x=504, y=10..13",
                "y=13, x=498..504"
        );

        int result = new Day17().solvePart2(input);

        assertThat(result).isEqualTo(29);
    }

    @Test
    void part1Sample2() {
        String input =
                ".............+.....\n" +
                ".#................#.\n" +
                ".#................#.\n" +
                ".#....#.#.........#.\n" +
                ".#....#.#.........#.\n" +
                ".#....###.........#.\n" +
                ".#................#.\n" +
                ".#................#.\n" +
                ".#................#.\n" +
                ".#................#.\n" +
                ".##################.";

        String[] split = input.split("\n");

        char[][] grid = Arrays.stream(split).map(String::toCharArray).toArray(char[][]::new);

        int result = new Day17().solvePart1(grid, 13, 0);

        assertThat(result).isEqualTo(137);
    }
    
    @Test
    void part1Sample3() {
        String input =  "...........+.............\n" +
                        ".#.......................\n" +
                        ".#......................#\n" +
                        ".#......................#\n" +
                        ".#......................#\n" +
                        ".#.............#..#.....#\n" +
                        ".#.............#..#.....#\n" +
                        ".#.............####.....#\n" +
                        ".#......................#\n" +
                        ".#......................#\n" +
                        ".#......................#\n" +
                        ".########################";

        String[] split = input.split("\n");

        char[][] grid = Arrays.stream(split).map(String::toCharArray).toArray(char[][]::new);

        int result = new Day17().solvePart1(grid, 11, 0);

        assertThat(result).isEqualTo(213);
    }
    
    @Test
    void part1Sample4() {
        String input =  
                "...................+..\n" +
                "........#..#..........\n" +
                "........#..#..........\n" +
                "........#..#......#...\n" +
                ".#......#..#......#...\n" +
                ".#......#..#......#...\n" +
                ".#......#..#......#...\n" +
                ".#......#..#......#...\n" +
                ".#......#..#......#...\n" +
                ".#......#..#......#...\n" +
                ".#......#..#......#...\n" +
                ".#......#..#......#...\n" +
                ".#......####......#...\n" +
                ".#................#...\n" +
                ".#................#...\n" +
                ".##################...";

        String[] split = input.split("\n");

        char[][] grid = Arrays.stream(split).map(String::toCharArray).toArray(char[][]::new);

        int result = new Day17().solvePart1(grid, 19, 0);

        assertThat(result).isEqualTo(15);
    }

    @Test
    void solvePart1() {
        List<String> input = ResourceUtils.readLines("day17.txt");

        int result = new Day17().solvePart1(input);

        assertThat(result).isEqualTo(27331);
    }

    @Test
    void solvePart2() {
        List<String> input = ResourceUtils.readLines("day17.txt");

        int result = new Day17().solvePart2(input);

        assertThat(result).isEqualTo(22245);


    }


}