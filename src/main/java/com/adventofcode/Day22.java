package com.adventofcode;

import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static java.util.Comparator.comparingInt;

class Day22 {

    int solvePart1(int depth, int targetX, int targetY) {
        Coordinate target = new Coordinate(targetX, targetY);
        Region[][] regions = generateRegions(depth, target);

        return riskLevel(regions, target);
    }

    int solvePart2(int depth, int targetX, int targetY) {
        Coordinate target = new Coordinate(targetX, targetY);
        Region[][] regions = generateRegions(depth, target);

        PriorityQueue<Movement> movements = new PriorityQueue<>(comparingInt(Movement::getCost));
        Map<Tuple2<Coordinate, Tool>, Integer> distances = new HashMap<>();
        Set<Tuple2<Coordinate, Tool>> seen = new HashSet<>();

        movements.add(new Movement(0, Tool.TORCH, Tool.TORCH, null, new Coordinate(0, 0)));

        while (!movements.isEmpty()) {
            Movement movement = movements.remove();

            if (movement.coordinate.equals(target)
                    && seen.containsAll(getAvailableToolsFor(target.at(regions)).map(t -> Tuple.of(target, t)).toJavaList())) {
                break;
            }

            if (!seen.contains(Tuple.of(movement.coordinate, movement.equippedTool))) {
                if (movement.cost < distances.getOrDefault(Tuple.of(movement.coordinate, movement.equippedTool), Integer.MAX_VALUE)) {
                    distances.put(Tuple.of(movement.coordinate, movement.equippedTool), movement.cost);
                }

                List<Movement> nextMovements = getNextMovements(movement, regions);

                for (Movement nextMovement : nextMovements) {
                    if (nextMovement.cost < distances.getOrDefault(Tuple.of(nextMovement.coordinate, nextMovement.equippedTool), Integer.MAX_VALUE)
                            && !seen.contains(Tuple.of(nextMovement.coordinate, nextMovement.equippedTool))) {
                        distances.put(Tuple.of(nextMovement.coordinate, nextMovement.equippedTool), nextMovement.cost);
                        movements.add(nextMovement);
                    }
                }

                seen.add(Tuple.of(movement.coordinate, movement.equippedTool));
            }
        }

        return distances.entrySet().stream()
                .filter(e -> e.getKey()._1.equals(target))
                .mapToInt(e -> e.getValue() + getChangeCost(e.getKey()._2, Tool.TORCH))
                .min()
                .getAsInt();
    }

    private List<Movement> getNextMovements(Movement parent, Region[][] cavern) {
        Coordinate coordinate = parent.coordinate;

        return List.of(
                new Coordinate(coordinate.x + 1, coordinate.y),
                new Coordinate(coordinate.x, coordinate.y + 1),
                new Coordinate(coordinate.x - 1, coordinate.y),
                new Coordinate(coordinate.x, coordinate.y - 1)
        )
                .flatMap(c -> Try.of(() -> Tuple.of(c, c.at(cavern))).toOption())
                .flatMap(t -> getMovements(parent, t._1, t._2, cavern))
                .sorted(comparingInt(Movement::getCost));
    }

    private List<Movement> getMovements(Movement parent,
                                        Coordinate toCoordinate,
                                        Region toRegion,
                                        Region[][] cavern) {
        return intersection(
                getAvailableToolsFor(parent.coordinate.at(cavern)),
                getAvailableToolsFor(toRegion)
        ).map(tool -> new Movement(
                parent.cost + getChangeCost(parent.equippedTool, tool) + 1,
                parent.equippedTool,
                tool,
                parent.coordinate,
                toCoordinate
        ));
    }

    private List<Tool> getAvailableToolsFor(Region region) {
        return API.Match(region).of(
                Case($(Region.ROCKY), List.of(Tool.CLIMBING_GEAR, Tool.TORCH)),
                Case($(Region.WET), List.of(Tool.CLIMBING_GEAR, Tool.NEITHER)),
                Case($(Region.NARROW), List.of(Tool.TORCH, Tool.NEITHER))
        );
    }

    private <T> List<T> intersection(List<T> list1, List<T> list2) {
        return list1.filter(list2::contains);
    }

    private int getChangeCost(Tool equippedTool, Tool changedTool) {
        return equippedTool == changedTool ? 0 : 7;
    }

    private Region[][] generateRegions(int depth, Coordinate target) {
        int[][] geologicIndex = new int[depth][depth];
        int[][] erosionLevels = new int[depth][depth];
        Region[][] regions = new Region[depth][depth];

        for (int y = 0; y < depth; y++) {
            for (int x = 0; x < depth; x++) {
                geologicIndex[y][x] = geologicIndex(x, y, target, erosionLevels);
                erosionLevels[y][x] = erosionLevel(x, y, geologicIndex, depth);
                regions[y][x] = getType(x, y, erosionLevels);
            }
        }

        return regions;
    }

    private int riskLevel(Region[][] regions, Coordinate target) {
        int riskLevel = 0;

        for (int y = 0; y <= target.x; y++) {
            for (int x = 0; x <= target.y; x++) {
                switch (regions[y][x]) {
                    case ROCKY: riskLevel += 0; break;
                    case WET: riskLevel += 1; break;
                    case NARROW: riskLevel += 2; break;
                }
            }
        }

        return riskLevel;
    }

    private int geologicIndex(int x, int y, Coordinate target, int[][] erosionLevels) {
        if ((x == 0 && y == 0) || (x == target.x && y == target.y)) {
            return 0;
        } else if (y == 0) {
            return x * 16807;
        } else if (x == 0) {
            return y * 48271;
        } else {
            return erosionLevels[y][x - 1] * erosionLevels[y - 1][x];
        }
    }

    private int erosionLevel(int x, int y, int[][] geologicIndex, int depth) {
        return (geologicIndex[y][x] + depth) % 20183;
    }

    private Region getType(int x, int y, int[][] erosionLevels) {
        int erosionLevel = erosionLevels[y][x];

        if (erosionLevel % 3 == 0) return Region.ROCKY;
        if (erosionLevel % 3 == 1) return Region.WET;
        if (erosionLevel % 3 == 2) return Region.NARROW;

        throw new IllegalArgumentException();
    }

    static class Movement {
        final int cost;
        
        final Tool toolBefore;
        final Tool equippedTool;
        
        final Coordinate from;
        final Coordinate coordinate;

        Movement(int cost, Tool toolBefore, Tool equippedTool, Coordinate from, Coordinate coordinate) {
            this.cost = cost;
            this.toolBefore = toolBefore;
            this.equippedTool = equippedTool;
            this.from = from;
            this.coordinate = coordinate;
        }

        int getCost() {
            return cost;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Movement movement = (Movement) o;
            return cost == movement.cost &&
                    toolBefore == movement.toolBefore &&
                    equippedTool == movement.equippedTool &&
                    Objects.equals(from, movement.from) &&
                    Objects.equals(coordinate, movement.coordinate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cost, toolBefore, equippedTool, from, coordinate);
        }

        @Override
        public String toString() {
            return "Movement{" +
                    "cost=" + cost +
                    ", toolBefore=" + toolBefore +
                    ", equippedTool=" + equippedTool +
                    ", from=" + from +
                    ", coordinate=" + coordinate +
                    '}';
        }
    }

    enum Region {
        ROCKY, WET, NARROW
    }

    enum Tool {
        TORCH, CLIMBING_GEAR, NEITHER
    }

    static class Coordinate {
        final int x;
        final int y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        <T> T at(T[][] grid) {
            return grid[y][x];
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return x + ", " + y;
        }
    }
}
