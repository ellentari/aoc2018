package com.adventofcode;

import com.adventofcode.comon.Coordinate;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.IndexedSeq;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.SortedMap;
import io.vavr.collection.SortedSet;
import io.vavr.collection.Stream;
import io.vavr.collection.TreeMap;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static java.util.function.Function.identity;

class Day15 {

    private static final int UNIT_HIT_POINTS = 200;
    private static final int GOBLIN_ATTACK_POINTS = 3;

    int solvePart1(IndexedSeq<String> map) {
        return solvePart1(3, map);
    }

    int solvePart1(int attackPoints, IndexedSeq<String> map) {
        Space[][] space = parseSpace(map);
        SortedMap<Coordinate, Unit> initialUnits = parseUnits(attackPoints, map);

        return performCombat(space, initialUnits).apply((rounds, units) ->
                (rounds - 1) * units.values().map(u -> u.hitPoints).sum().intValue()
        );
    }

    int solvePart2(IndexedSeq<String> map) {
        Space[][] space = parseSpace(map);
        int attackPoints = 3;

        while (true) {
            SortedMap<Coordinate, Unit> initialUnits = parseUnits(attackPoints, map);
            Tuple2<Integer, SortedMap<Coordinate, Unit>> result = performCombat(space, initialUnits);

            elfCount(initialUnits.values());

            if (elfCount(initialUnits.values()) == elfCount(result._2.values())) {
                System.out.println("Attack points needed " + attackPoints);
                return (result._1 - 1) * result._2.values().map(u -> u.hitPoints).sum().intValue();
            }

            attackPoints++;
        }
    }

    private int elfCount(Seq<Unit> units) {
        return units.count(u -> u.type == UnitType.ELF);
    }

    private Tuple2<Integer, SortedMap<Coordinate, Unit>> performCombat(Space[][] space,
                                                                       SortedMap<Coordinate, Unit> units) {

        System.out.println("Initially");
        print(space, units);

        int round = 1;

        while (true) {

            java.util.SortedMap<Coordinate, Unit> remaining = units.toJavaMap();

            for (Coordinate coordinate : units.keySet()) {
                if (remaining.containsKey(coordinate)) {

                    Unit unit = remaining.get(coordinate);
                    SortedMap<Coordinate, Unit> targets = getTargets(unit, TreeMap.ofAll(remaining));

                    if (targets.isEmpty()) {
                        System.out.println("Combat ends.");
                        print(space, TreeMap.ofAll(remaining));

                        return Tuple.of(round, TreeMap.ofAll(remaining));
                    }

                    Coordinate moved = makeMove(coordinate, targets, TreeMap.ofAll(remaining), space)
                            .getOrElse(coordinate);

                    remaining.remove(coordinate, unit);
                    remaining.put(moved, unit);

                    attack(unit, moved, targets, space).forEach(attacked -> {
                        Coordinate attackedCoordinate = attacked._1;
                        Unit attackedUnit = attacked._2;

                        if (attackedUnit.isDead()) {
                            remaining.remove(attackedCoordinate);
                        } else {
                            remaining.put(attackedCoordinate, attackedUnit);
                        }
                    });


                }
            }

            units = TreeMap.ofAll(remaining);


            System.out.println("After " + round + " rounds:");
            print(space, units);

            round++;
        }
    }

    private Option<Coordinate> makeMove(Coordinate unitCoordinate,
                                SortedMap<Coordinate, Unit> targets,
                                SortedMap<Coordinate, Unit> units,
                                Space[][] space) {
        if (getAdjacentCoordinates(unitCoordinate, space).exists(targets::containsKey)) {
            return Option.none();
        }

        SortedSet<Coordinate> inRange = getInRange(targets.keySet(), space, units);
        SortedMap<Coordinate, List<Coordinate>> reachable = getReachable(inRange, unitCoordinate, space, units);

        return reachable
                .minBy(t -> t._2.length())
                .map(t -> t.apply((target, path) -> path.head()));
    }

    private Option<Tuple2<Coordinate, Unit>> attack(Unit unit,
                                                    Coordinate unitCoordinate,
                                                    SortedMap<Coordinate, Unit> targets,
                                                    Space[][] space) {

        return getAdjacentUnits(unitCoordinate, targets, space)
                .minBy(t -> t._2.hitPoints)
                .map(t -> t.map2(target -> target.getAttackedBy(unit)));
    }

    private SortedMap<Coordinate, Unit> getAdjacentUnits(Coordinate unitCoordinate,
                                                         SortedMap<Coordinate, Unit> units,
                                                         Space[][] space) {
        return getAdjacentCoordinates(unitCoordinate, space)
                .filter(units::containsKey)
                .toSortedMap(identity(), c -> units.get(c).get());
    }

    private SortedMap<Coordinate, List<Coordinate>> getReachable(SortedSet<Coordinate> inRange,
                                                                 Coordinate coordinate,
                                                                 Space[][] space,
                                                                 SortedMap<Coordinate, Unit> units) {

        java.util.Map<Coordinate, List<Coordinate>> seen = new HashMap<>();
        Deque<Tuple2<Coordinate, Coordinate>> queue = new LinkedList<>();

        seen.put(coordinate, List.empty());

        getAdjacentEmpty(coordinate, space, units).forEach(adjacent -> {
            seen.put(adjacent, List.of(adjacent));
            queue.offer(Tuple.of(coordinate, adjacent));
        });

        while (!queue.isEmpty()) {
            Tuple2<Coordinate, Coordinate> tuple = queue.remove();

            Coordinate current = tuple._2;
            List<Coordinate> pathToCurrent = seen.get(tuple._1).append(current);

            getAdjacentEmpty(current, space, units)
                    .filter(a -> !seen.containsKey(a))
                    .forEach(a -> {
                        seen.put(a, pathToCurrent.append(a));
                        queue.offer(Tuple.of(current, a));
                    });
        }

        return inRange
                .filter(seen::containsKey)
                .toSortedMap(identity(), seen::get);
    }

    private SortedSet<Coordinate> getInRange(SortedSet<Coordinate> targets,
                                             Space[][] space,
                                             SortedMap<Coordinate, Unit> units) {
        return targets.flatMap(c -> getAdjacentEmpty(c, space, units));
    }

    private Stream<Coordinate> getAdjacentEmpty(Coordinate coordinate,
                                                Space[][] space,
                                                SortedMap<Coordinate, Unit> units) {
        return getAdjacentCoordinates(coordinate, space)
                .filter(c -> !units.containsKey(c));
    }

    private Stream<Coordinate> getAdjacentCoordinates(Coordinate coordinate, Space[][] space) {
        return Stream.of(
                tryGet(coordinate.top(), space),
                tryGet(coordinate.left(), space),
                tryGet(coordinate.right(), space),
                tryGet(coordinate.bottom(), space)
        )
                .flatMap(identity())
                .filter(c -> !c.at(space).isWall);
    }

    private Option<Coordinate> tryGet(Coordinate coordinate, Space[][] grid) {
        return Try.of(() -> coordinate.at(grid)).toOption()
                .map(s -> coordinate);
    }

    private SortedMap<Coordinate, Unit> getTargets(Unit unit, SortedMap<Coordinate, Unit> units) {
        return units.filterValues(u -> unit.type != u.type);
    }

    private void print(Space[][] grid, SortedMap<Coordinate, Unit> units) {
        System.out.println(show(grid, units));
    }

    private String show(Space[][] grid, SortedMap<Coordinate, Unit> units) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                Option<Unit> unit = units.get(coordinate);

                result.append(
                        unit.map(u -> u.type.name().charAt(0))
                                .getOrElse(() -> coordinate.at(grid).isWall ? '#' : '.')
                );
            }

            int finalI = i;

            result.append("  ");

            units.filterKeys(c -> c.i == finalI).values()
                    .forEach(result::append);

            result.append('\n');
        }

        return result.toString();
    }

    private Space[][] parseSpace(IndexedSeq<String> map) {
        Space[][] grid = new Space[map.length()][map.get(0).length()];

        coordinatesOf(map)
                .forEach(c ->
                    grid[c.i][c.j] = Match(map.get(c.i).charAt(c.j)).of(
                            Case($('#'), new Space(true)),
                            Case($(), new Space(false))
                    )
                );

        return grid;
    }

    private SortedMap<Coordinate, Unit> parseUnits(int elfAttackPoints, IndexedSeq<String> map) {
        return coordinatesOf(map)
                .flatMap(c -> Match(map.get(c.i).charAt(c.j)).option(
                        Case($('E'), ch -> new Unit(elfAttackPoints, UNIT_HIT_POINTS, UnitType.ELF)),
                        Case($('G'), ch -> new Unit(GOBLIN_ATTACK_POINTS, UNIT_HIT_POINTS, UnitType.GOBLIN))
                ).map(u -> Tuple.of(c, u)))
                .toSortedMap(Tuple2::_1, Tuple2::_2);
    }

    private Stream<Coordinate> coordinatesOf(IndexedSeq<String> map) {
        return Stream.range(0, map.length())
                .flatMap(i -> Stream.range(0, map.get(i).length()).map(j -> new Coordinate(i, j)));
    }

    private static final class Space {
        final boolean isWall;

        Space(boolean isWall) {
            this.isWall = isWall;
        }
    }

    private static class Unit {
        final int attackPoints;
        final int hitPoints;
        final UnitType type;

        Unit(int attackPoints, int hitPoints, UnitType type) {
            this.attackPoints = attackPoints;
            this.hitPoints = hitPoints;
            this.type = type;
        }

        Unit getAttackedBy(Unit unit) {
            return new Unit(this.attackPoints, hitPoints - unit.attackPoints, type);
        }

        boolean isDead() {
            return hitPoints <= 0;
        }

        @Override
        public String toString() {
            return type.name().charAt(0) + "(" + hitPoints + ')';
        }
    }

    private enum UnitType {
        ELF, GOBLIN
    }
}
