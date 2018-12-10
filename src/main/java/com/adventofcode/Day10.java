package com.adventofcode;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

class Day10 {

    private static final Pattern INPUT_PATTERN =
            Pattern.compile("position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>");

    private Option<String> solve(int secondsLimit, List<String> input) {
        return findPossibleSolution(secondsLimit, input.flatMap(this::parse))
                .map(this::makeString);
    }

    private Option<List<Point>> findPossibleSolution(int secondsLimit, List<Tuple2<Point, Velocity>> points) {
        return Stream.range(1, secondsLimit)
                .map(sec -> points.map(t -> move(sec, t._1, t._2)))
                .minBy(this::pointsDiff);
    }

    private Point move(int second, Point point, Velocity velocity) {
        return new Point(point.x + velocity.dx * second, point.y + velocity.dy * second);
    }

    private int pointsDiff(List<Point> points) {
        Stats stats = calculateStats(points);

        return stats.maxX - stats.minX + stats.maxY - stats.minY;
    }

    private String makeString(List<Point> points) {
        return makeString(buildGrid(points));
    }

    private char[][] buildGrid(List<Point> points) {
        Stats stats = calculateStats(points);

        char[][] grid = initGrid(stats.maxY - stats.minY + 1, stats.maxX - stats.minX + 1);

        points.forEach(p -> {
            int x = p.x - stats.minX;
            int y = p.y - stats.minY;

            grid[y][x] = '#';
        });

        return grid;
    }

    private char[][] initGrid(int i, int j) {
        char[][] grid = new char[i][j];
        stream(grid).forEach(line -> Arrays.fill(line, ' '));
        return grid;
    }

    private String makeString(char[][] grid) {
        return stream(grid)
                .map(String::new)
                .collect(joining("\n"));
    }

    private Stats calculateStats(List<Point> points) {
        int minX = points.minBy(p -> p.x).get().x;
        int maxX = points.maxBy(p -> p.x).get().x;

        int minY = points.minBy(p -> p.y).get().y;
        int maxY = points.maxBy(p -> p.y).get().y;

        return new Stats(minX, maxX, minY, maxY);
    }

    private Option<Tuple2<Point, Velocity>> parse(String s) {
        Matcher matcher = INPUT_PATTERN.matcher(s);

        if (matcher.find()) {
            String x = matcher.group(1);
            String y = matcher.group(2);
            String dx = matcher.group(3);
            String dy = matcher.group(4);

            return Option.of(Tuple.of(
                    new Point(Integer.parseInt(x), Integer.parseInt(y)),
                    new Velocity(Integer.parseInt(dx), Integer.parseInt(dy))
            ));
        }

        return Option.none();
    }

    private static class Stats {
        final int minX;
        final int maxX;

        final int minY;
        final int maxY;

        Stats(int minX, int maxX, int minY, int maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }
    }

    private static class Point {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Velocity {
        final int dx;
        final int dy;

        Velocity(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    public static void main(String[] args) {
        new Day10().solve(5, sample())
                .forEach(System.out::println);

        new Day10().solve(10_400, input())
                .forEach(System.out::println);
    }

    private static List<String> sample() {
        return List.of(
                "position=< 9,  1> velocity=< 0,  2>",
                "position=< 7,  0> velocity=<-1,  0>",
                "position=< 3, -2> velocity=<-1,  1>",
                "position=< 6, 10> velocity=<-2, -1>",
                "position=< 2, -4> velocity=< 2,  2>",
                "position=<-6, 10> velocity=< 2, -2>",
                "position=< 1,  8> velocity=< 1, -1>",
                "position=< 1,  7> velocity=< 1,  0>",
                "position=<-3, 11> velocity=< 1, -2>",
                "position=< 7,  6> velocity=<-1, -1>",
                "position=<-2,  3> velocity=< 1,  0>",
                "position=<-4,  3> velocity=< 2,  0>",
                "position=<10, -3> velocity=<-1,  1>",
                "position=< 5, 11> velocity=< 1, -2>",
                "position=< 4,  7> velocity=< 0, -1>",
                "position=< 8, -2> velocity=< 0,  1>",
                "position=<15,  0> velocity=<-2,  0>",
                "position=< 1,  6> velocity=< 1,  0>",
                "position=< 8,  9> velocity=< 0, -1>",
                "position=< 3,  3> velocity=<-1,  1>",
                "position=< 0,  5> velocity=< 0, -1>",
                "position=<-2,  2> velocity=< 2,  0>",
                "position=< 5, -2> velocity=< 1,  2>",
                "position=< 1,  4> velocity=< 2,  1>",
                "position=<-2,  7> velocity=< 2, -2>",
                "position=< 3,  6> velocity=<-1, -1>",
                "position=< 5,  0> velocity=< 1,  0>",
                "position=<-6,  0> velocity=< 2,  0>",
                "position=< 5,  9> velocity=< 1, -2>",
                "position=<14,  7> velocity=<-2,  0>",
                "position=<-3,  6> velocity=< 2, -1>"
        );
    }

    private static List<String> input() {
        return ResourceUtils.readLines("day10.txt");
    }
}
