package com.adventofcode;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

import static java.util.Arrays.stream;

class Day20 {

    int solvePart1(String pathRegexp) {
        return findRooms(pathRegexp).values().max().get();
    }

    int solvePart2(String pathRegexp) {
        return findRooms(pathRegexp).values().count(v -> v >= 1000);
    }

    private Map<Coordinate, Integer> findRooms(String pathRegexp) {
        java.util.Map<Coordinate, Integer> rooms = new java.util.HashMap<>();

        Coordinate current = new Coordinate(0, 0);
        rooms.put(current, 0);

        Deque<Character> stack = new LinkedList<>();

        for (char ch : pathRegexp.substring(1, pathRegexp.length() - 1).toCharArray()) {
            if (ch == '|' || ch == ')') {
                current = unwind(current, stack, ch);
            } else {
                stack.push(ch);

                if (ch != '(') {
                    current = getNext(current, Direction.from(ch));
                    rooms.merge(current, pathFrom(stack).length(), Math::min);
                }
            }
        }

        return HashMap.ofAll(rooms);
    }

    private Coordinate unwind(Coordinate coordinate, Deque<Character> stack, char currentChar) {
        Coordinate result = coordinate;

        while (stack.getFirst() != '(' && stack.getFirst() != '|') {
            result = getPrevious(result, Direction.from(stack.removeFirst()));
        }

        if (stack.getFirst() == '(' && currentChar == ')') {
            stack.removeFirst();
        }

        return result;
    }

    private Coordinate getNext(Coordinate coordinate, Direction direction) {
        return new Coordinate(coordinate.x + direction.dx, coordinate.y + direction.dy);
    }

    private Coordinate getPrevious(Coordinate coordinate, Direction direction) {
        return new Coordinate(coordinate.x - direction.dx, coordinate.y - direction.dy);
    }

    private CharSequence pathFrom(Deque<Character> stack) {
        StringBuilder builder = new StringBuilder();
        for (Character character : stack) {
            if (character != '(') {
                builder.append(character);
            }
        }
        return builder.reverse();
    }

    private static final class Coordinate {
        final int x;
        final int y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
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
            return  x + ", " + y;
        }
    }

    private enum Direction {
        E(1, 0),
        W(-1, 0),
        N(0, -1),
        S(0, 1);

        final int dx;
        final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        static Direction from(char ch) {
            return stream(values())
                    .filter(n -> n.name().charAt(0) == ch)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}
