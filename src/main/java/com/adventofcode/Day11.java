package com.adventofcode;

import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.Tuple4;
import io.vavr.collection.Stream;

class Day11 {

    private static final int GRID_SIZE = 300;

    String solvePart1(int serialNumber) {
        int[][] grid = initGrid(serialNumber);

        return findLargestTotalPower(grid, 3)
                .apply((i, j, largestPower) -> (j + 1) + "," + (i + 1));
    }

    String solvePart2(int serialNumber) {
        int[][] grid = initGrid(serialNumber);

        return Stream.rangeClosed(1, GRID_SIZE)
                .map(size -> findLargestTotalPower(grid, size).apply((i, j, largestPower) ->
                        Tuple.of(i, j, largestPower, size)
                ))
                .maxBy(Tuple4::_3).get()
                .apply((i, j, largestPower, size) -> (j + 1) + "," + (i + 1) + "," + size);
    }

    private int[][] initGrid(int serialNumber) {
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = powerLevel(j + 1, i + 1, serialNumber);
            }
        }

        return grid;
    }

    static int powerLevel(int x, int y, int serialNumber) {
        int rackId = x + 10;

        return hundredsFrom((rackId * y + serialNumber) * rackId) - 5;
    }

    private static int hundredsFrom(int value) {
        return (value % 1000) / 100;
    }

    private Tuple3<Integer, Integer, Long> findLargestTotalPower(int[][] grid, int size) {
        long largestPower = Long.MIN_VALUE;
        Integer maxI = null;
        Integer maxJ = null;

        for (int i = 0; i < grid.length - size; i++) {
            for (int j = 0; j < grid[i].length - size; j++) {
                long totalPower = totalPower(grid, i, j, size);

                if (largestPower < totalPower) {
                    largestPower = totalPower;
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        return Tuple.of(maxI, maxJ, largestPower);
    }

    private long totalPower(int[][] grid, int startI, int startJ, int size) {
        long total = 0;

        for (int i = startI; i < startI + size; i++) {
            for (int j = startJ; j < startJ + size; j++) {
                total += grid[i][j];
            }
        }

        return total;
    }
}
