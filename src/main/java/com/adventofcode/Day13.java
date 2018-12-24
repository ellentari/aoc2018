package com.adventofcode;

import com.adventofcode.comon.Coordinate;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.IndexedSeq;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.collection.Stream.range;

class Day13 {

    String solvePart1(IndexedSeq<String> input) {
        List<Tuple2<Cart, Coordinate>> carts = getCarts(input);
        IndexedSeq<String> map = getMap(input);

        while (true) {
            carts = carts.map(t -> t.map((cart, coordinate) -> move(coordinate, cart, map)))
                    .sorted(Comparator.comparing(t -> t._2));

            List<Coordinate> collisions = getCollisions(carts);

            if (!collisions.isEmpty()) {
                return toString(collisions.head());
            }
        }
    }

    private String toString(Coordinate coordinate) {
        return coordinate.j + "," + coordinate.i;
    }

    String solvePart2(IndexedSeq<String> input) {
        IndexedSeq<String> map = getMap(input);
        List<Tuple2<Cart, Coordinate>> carts = getCarts(input);

        while (carts.size() > 1) {
            Map<Coordinate, Cart> cartMap = carts.toMap(Tuple2::_2, Tuple2::_1);
            List<Coordinate> toProcess = carts.map(t -> t._2).sorted();

            java.util.Map<Coordinate, Cart> moved = new HashMap<>();

            Set<Coordinate> processed = new HashSet<>();
            Set<Coordinate> dead = new HashSet<>();

            for (Coordinate current : toProcess) {
                if (dead.contains(current)) {
                    continue;
                }

                Cart cart = cartMap.get(current).get();
                Tuple2<Cart, Coordinate> t = move(current, cart, map);

                if (toProcess.removeAll(processed).contains(t._2)) {
                    dead.add(t._2);
                } else if (moved.containsKey(t._2)) {
                    moved.remove(t._2);
                    dead.add(t._2);
                } else {
                    moved.put(t._2, t._1);
                }

                processed.add(current);
            }

            carts = io.vavr.collection.HashMap.ofAll(moved).toList().map(Tuple2::swap);
        }

        return carts.head().apply((cart, coordinate) ->
                toString(coordinate)
        );
    }

    private List<Coordinate> getCollisions(List<Tuple2<Cart, Coordinate>> carts) {
        Map<Coordinate, List<Tuple2<Cart, Coordinate>>> byCoordinate = carts.groupBy(t -> t._2);

        return byCoordinate.filterValues(list -> list.size() > 1)
                .keySet().toList();
    }

    private Tuple2<Cart, Coordinate> move(Coordinate coordinate, Cart cart, IndexedSeq<String> input) {
        Coordinate nextCoordinate = new Coordinate(coordinate.i + cart.direction.di, cart.direction.dj + coordinate.j);
        Cart movedCart = move(input.get(nextCoordinate.i).charAt(nextCoordinate.j), cart);

        return Tuple.of(movedCart, nextCoordinate);
    }

    private Cart move(char nextChar, Cart cart) {
        if (nextChar == '+') {
            return new Cart(turn(cart.direction, cart.turnsCount), cart.turnsCount + 1);
        }

        return new Cart(nextDirection(nextChar, cart.direction), cart.turnsCount);
    }

    private Direction nextDirection(char nextChar, Direction currentDirection) {
        return Match(Tuple.of(currentDirection, nextChar)).of(
                // clockwise
                Case($(Tuple.of(Direction.RIGHT, '\\')), Direction.DOWN),
                Case($(Tuple.of(Direction.DOWN, '/')), Direction.LEFT),
                Case($(Tuple.of(Direction.LEFT, '\\')), Direction.UP),
                Case($(Tuple.of(Direction.UP, '/')), Direction.RIGHT),

                // counter-clockwise
                Case($(Tuple.of(Direction.RIGHT, '/')), Direction.UP),
                Case($(Tuple.of(Direction.UP, '\\')), Direction.LEFT),
                Case($(Tuple.of(Direction.LEFT, '/')), Direction.DOWN),
                Case($(Tuple.of(Direction.DOWN, '\\')), Direction.RIGHT),

                Case($(), currentDirection)
        );
    }

    private Direction turnLeft(Direction direction) {
        return Match(direction).of(
                Case($(Direction.UP), Direction.LEFT),
                Case($(Direction.DOWN), Direction.RIGHT),
                Case($(Direction.RIGHT), Direction.UP),
                Case($(Direction.LEFT), Direction.DOWN)
        );
    }

    private Direction turnRight(Direction direction) {
        return Match(direction).of(
                Case($(Direction.UP), Direction.RIGHT),
                Case($(Direction.DOWN), Direction.LEFT),
                Case($(Direction.RIGHT), Direction.DOWN),
                Case($(Direction.LEFT), Direction.UP)
        );
    }

    private Direction turn(Direction direction, int turns) {
        if (turns % 3 == 0) return turnLeft(direction);
        if (turns % 3 == 1) return direction;
        if (turns % 3 == 2) return turnRight(direction);

        throw new IllegalArgumentException();
    }

    private List<Tuple2<Cart, Coordinate>> getCarts(IndexedSeq<String> input) {
        return range(0, input.length()).flatMap(i ->
                range(0, input.get(i).length()).map(j -> new Coordinate(i, j)))
                .flatMap(c -> parseCart(input.get(c.i).charAt(c.j)).map(cart -> Tuple.of(cart, c)))
                .sorted(Comparator.comparing(t -> t._2))
                .toList();
    }

    private Option<Cart> parseCart(char ch) {
        return Match(ch).option(
                Case($('^'), new Cart(Direction.UP)),
                Case($('>'), new Cart(Direction.RIGHT)),
                Case($('v'), new Cart(Direction.DOWN)),
                Case($('<'), new Cart(Direction.LEFT))
        );
    }

    private IndexedSeq<String> getMap(IndexedSeq<String> input) {
        return input.map(s -> s.replaceAll("[><]", "-")
                .replaceAll("[\\^v]", "|"));
    }

    private static class Cart {
        final Direction direction;
        final int turnsCount;

        Cart(Direction direction, int turnsCount) {
            this.direction = direction;
            this.turnsCount = turnsCount;
        }

        Cart(Direction direction) {
            this(direction, 0);
        }
    }

    private enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        final int dj;
        final int di;

        Direction(int dj, int di) {
            this.dj = dj;
            this.di = di;
        }
    }
}
