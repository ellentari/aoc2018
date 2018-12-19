package com.adventofcode;

import com.adventofcode.comon.Coordinate;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

class Day17 {

    int solvePart1(List<String> input) {
        List<Ranges> ranges = input.map(this::map);


        int minY = ranges.map(r -> r.y.from).min().get();
        int maxY = ranges.map(r -> r.y.to).max().get();

        int minX = ranges.map(r -> r.x.from).min().get();
        int maxX = ranges.map(r -> r.x.to).max().get();

        char[][] grid = new char[maxY - minY + 2][maxX - minX + 3];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = '.';
            }
        }

        for (Ranges range : ranges) {
            for (int y = range.y.from; y <= range.y.to; y++) {
                for (int x = range.x.from; x <= range.x.to; x++) {

                    grid[y - minY + 1][x - minX + 1] = '#';

                }
            }
        }

        final int startX = 500 - minX + 1;
        final int startY = 0;

        grid[startY][startX] = '+';

        print(grid);

        return solvePart1(grid, startX, startY);
//        return 0;
    }

    int solvePart2(List<String> input) {
        List<Ranges> ranges = input.map(this::map);


        int minY = ranges.map(r -> r.y.from).min().get();
        int maxY = ranges.map(r -> r.y.to).max().get();

        int minX = ranges.map(r -> r.x.from).min().get();
        int maxX = ranges.map(r -> r.x.to).max().get();

        char[][] grid = new char[maxY - minY + 2][maxX - minX + 3];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = '.';
            }
        }

        for (Ranges range : ranges) {
            for (int y = range.y.from; y <= range.y.to; y++) {
                for (int x = range.x.from; x <= range.x.to; x++) {

                    grid[y - minY + 1][x - minX + 1] = '#';

                }
            }
        }

        final int startX = 500 - minX + 1;
        final int startY = 0;

        grid[startY][startX] = '+';

        doSolve(grid, startX, startY);

        return Stream.of(grid).flatMap(Stream::ofAll).count(ch -> ch == '~');
    }

    int solvePart1(char[][] grid, int startX, int startY) {
        doSolve(grid, startX, startY);

        return Stream.of(grid).flatMap(Stream::ofAll).count(ch -> ch == '|' || ch == '~');
    }

    private void doSolve(char[][] grid, int startX, int startY) {
        int count = 0;
//
        Deque<Coordinate> stack = new LinkedList<>();
        stack.push(new Coordinate(startY + 1, startX));

        Set<Coordinate> seen = new HashSet<>();


        while (!stack.isEmpty()) {
            Coordinate coordinate = stack.pop();

            if (!seen.contains(coordinate)) {
                seen.add(coordinate);

                int i = coordinate.i;
                int j = coordinate.j;


                Character top = get(grid, i - 1, coordinate.j).getOrElse('\0');
                Character bootom = get(grid, i + 1, coordinate.j).getOrElse('\0');
                Character botomLeft = get(grid, i + 1, coordinate.j - 1).getOrElse('\0');
                Character botomRight = get(grid, i + 1, coordinate.j + 1).getOrElse('\0');
                Character left = get(grid, i, coordinate.j - 1).getOrElse('\0');
                Character right = get(grid, i, coordinate.j + 1).getOrElse('\0');

                if (bootom == '#' || bootom == '~') {

                    if (insideWalls(grid, i, j)) {
                        grid[i][j] = '~';

                    getCoordinate(grid, i, coordinate.j + 1).filter(c -> c.at(grid) == '|')
                            .forEach(seen::remove);
                    getCoordinate(grid, i, coordinate.j - 1).filter(c -> c.at(grid) == '|')
                            .forEach(seen::remove);
                    getCoordinate(grid, i - 1, coordinate.j).filter(c -> c.at(grid) == '|' && insideWalls(grid, c.i, c.j))
                            .forEach(c -> {
                                seen.remove(c);
                                stack.push(c);
                            });

                    } else if (List.of(botomLeft, bootom, botomRight).count(ch -> ch == '~' || ch == '#') >= 2) {
                        grid[i][j] = '|';
                    }
                } else if (top == '|' || top == '+') {
                    grid[i][j] = '|';
                } else if ((left == '|' && botomLeft == '#') ||(right == '|' && botomRight== '#')) {
                    grid[i][j] = '|';

                }

                count++;

                if (grid[i][j] == '|' || grid[i][j] == '~') {
                    getCoordinate(grid, i, coordinate.j + 1).filter(c -> c.at(grid) != '#')
                            .forEach(stack::push);
                    getCoordinate(grid, i, coordinate.j - 1).filter(c -> c.at(grid) != '#')
                            .forEach(stack::push);
                    getCoordinate(grid, i + 1, coordinate.j).filter(c -> c.at(grid) != '#')
                            .forEach(stack::push);
                }


            }
        }
    }

    private boolean insideWalls(char[][] grid, int i, int j) {

        int left = j;
        int right = j;

        boolean leftFound = false;
        boolean rightFound = false;

        while (get(grid, i + 1, left).getOrElse('\0') == '~'
                || get(grid, i + 1, left).getOrElse('\0') == '#') {
            left--;
            if (get(grid, i, left).getOrElse('\0') == '#') {
                leftFound = true;
            }
        }

        while (get(grid, i + 1, right).getOrElse('\0') == '~'
                || get(grid, i + 1, right).getOrElse('\0') == '#') {
            right++;
            if (get(grid, i, right).getOrElse('\0') == '#') {
                rightFound = true;
            }
        }

        return leftFound && rightFound;
    }

    private Option<Character> get(char[][] grid, int i, int j) {
        return Try.of(() -> grid[i][j]).toOption();
    }

    private Option<Coordinate> getCoordinate(char[][] grid, int i, int j) {
        return Try.of(() -> grid[i][j]).map(__ -> new Coordinate(i, j)).toOption();
    }

    private void print(char[][] grid) {
        StringBuilder bldr = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            bldr.append(new String(grid[i])).append('\n');
        }

        bldr.append('\n').append('\n');

//        System.out.println(bldr.toString());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/Elentari/ssss/7.txt", true))) {
            IOUtils.write(bldr, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Ranges map(String s) {
        String[] split = s.split(", ");

        String[] first = split[0].split("=");
        String[] second = split[1].split("=");

        Range x = first[0].equals("x") ? parseRange(first[1]) : parseRange(second[1]);
        Range y = second[0].equals("y") ? parseRange(second[1]) : parseRange(first[1]);

        return new Ranges(x, y);
    }

    private Range parseRange(String range) {
        String[] split = range.split("\\.\\.");
        int from = Integer.parseInt(split[0]);
        int to = split.length > 1 ? Integer.parseInt(split[1]) : from;

        return new Range(from, to);
    }

    private static class Range {
        final int from;
        final int to;

        Range(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    private static class Ranges {
        final Range x;
        final Range y;

        Ranges(Range x, Range y) {
            this.x = x;
            this.y = y;
        }
    }
}
