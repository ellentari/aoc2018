package com.adventofcode;

import com.adventofcode.comon.Coordinate;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.control.Option;

import java.util.Objects;
import java.util.function.Function;

import static io.vavr.collection.HashMap.empty;
import static io.vavr.collection.Stream.range;
import static io.vavr.collection.Stream.rangeClosed;
import static java.util.Comparator.comparing;

class Day6 {

    int solvePart1(List<String> input) {
        Map<ID, Coordinate> coordinates = parseCoordinates(input);

        ID[][] grid = initGrid(coordinates);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Coordinate to = new Coordinate(i, j);
                findClosest(to, coordinates).forEach(closest -> grid[to.i][to.j] = closest);
            }
        }

        Map<ID, Integer> areas = calculateAreas(grid, infiniteAreas(grid));

        return areas.maxBy(t -> t._2).get()._2;
    }

    int solvePart2(int limit, List<String> input) {
        Map<ID, Coordinate> coordinates = parseCoordinates(input);

        ID[][] grid = initGrid(coordinates);

        ID target = new ID(-1);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {

                int total = findTotalDistance(new Coordinate(i, j), coordinates.values());
                if (total < limit) {
                    grid[i][j] = target;
                }
            }
        }

        return calculateAreas(grid).get(target).get();
    }

    private Map<ID, Coordinate> parseCoordinates(List<String> input) {
        return rangeClosed(1, input.length())
                .zip(input)
                .toMap(t -> new ID(t._1), t -> parseCoordinate(t._2));
    }

    private Coordinate parseCoordinate(String coordinate) {
        String[] split = coordinate.split(", ");

        return new Coordinate(
                Integer.parseInt(split[1]),
                Integer.parseInt(split[0])
        );
    }

    private ID[][] initGrid(Map<ID, Coordinate> coordinates) {
        int i = max(coordinates.values(), Coordinate::getI) + 1;
        int j = max(coordinates.values(), Coordinate::getJ) + 1;

        ID[][] grid = new ID[i][j];

        coordinates.forEach((id, coordinate) ->
                grid[coordinate.i][coordinate.j] = id
        );

        return grid;
    }

    private Set<ID> infiniteAreas(ID[][] grid) {
        return Stream.concat(
                range(0, grid.length).map(i -> grid[i][0]), // left border
                range(0, grid.length).map(i -> grid[i][grid[i].length - 1]), // right border

                range(0, grid[0].length).map(i -> grid[0][i]), // top border
                range(0, grid[0].length).map(i -> grid[grid.length - 1][i]) // bottom border
        ).toSet();
    }

    private Map<ID, Integer> calculateAreas(ID[][] grid) {
        return calculateAreas(grid, HashSet.empty());
    }

    private Map<ID, Integer> calculateAreas(ID[][] grid, Set<ID> exclude) {
        java.util.Map<ID, Integer> areas = new java.util.HashMap<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!areas.containsKey(grid[i][j]) && !exclude.contains(grid[i][j])) {
                    areas.put(grid[i][j], calculateArea(grid, i, j));
                }
            }
        }

        return HashMap.ofAll(areas);
    }

    private int calculateArea(ID[][] grid, int i, int j) {
        java.util.Queue<Coordinate> queue = new java.util.LinkedList<>();
        java.util.Set<Coordinate> visited = new java.util.HashSet<>();

        queue.offer(new Coordinate(i, j));

        int areaSize = 0;

        while (!queue.isEmpty()) {
            Coordinate coordinate = queue.remove();

            if (visited.contains(coordinate)) {
                continue;
            }

            areaSize++;
            visited.add(coordinate);

            adjacent(coordinate, grid)
                    .filter(a -> grid[i][j] == grid[a.i][a.j])
                    .forEach(queue::offer);
        }

        return areaSize;
    }

    private <T> Stream<Coordinate> adjacent(Coordinate coordinate, T[][] grid) {
        return Stream.of(
                new Coordinate(coordinate.i - 1, coordinate.j),
                new Coordinate(coordinate.i + 1, coordinate.j),
                new Coordinate(coordinate.i, coordinate.j + 1),
                new Coordinate(coordinate.i, coordinate.j - 1),
                new Coordinate(coordinate.i + 1, coordinate.j + 1),
                new Coordinate(coordinate.i - 1, coordinate.j - 1),
                new Coordinate(coordinate.i + 1, coordinate.j - 1),
                new Coordinate(coordinate.i - 1, coordinate.j + 1)
        )
                .filter(c -> isSafe(c, grid));
    }

    private Option<ID> findClosest(Coordinate to, Map<ID, Coordinate> coordinates) {
        Map<ID, Integer> distances = coordinates.mapValues(c -> manhattanDistance(to, c));
        Option<Integer> minDistance = distances.minBy(Tuple2::_2).map(Tuple2::_2);
        Map<ID, Integer> closest = minDistance.map(md -> distances.filter(t -> t._2.equals(md))).getOrElse(empty());

        return closest.size() == 1 ? Option.some(closest.head()._1) : Option.none();
    }

    private int findTotalDistance(Coordinate coordinate, Seq<Coordinate> coordinates) {
        return coordinates.map(c -> manhattanDistance(coordinate, c)).sum().intValue();
    }

    private static int manhattanDistance(Coordinate c1, Coordinate c2) {
        return Math.abs(c1.i - c2.i) + Math.abs(c1.j - c2.j);
    }

    private static  <T> boolean isSafe(Coordinate coordinate, T[][] grid) {
        return coordinate.i >= 0 && coordinate.i < grid.length
                && coordinate.j >= 0 && coordinate.j < grid[coordinate.i].length;
    }

    private static int max(Seq<Coordinate> coordinates, Function<Coordinate, Integer> f) {
        return coordinates.maxBy(comparing(f)).map(f).getOrElse(0);
    }

    private static class ID {
        final Integer value;

        ID(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ID id = (ID) o;
            return Objects.equals(value, id.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

}
