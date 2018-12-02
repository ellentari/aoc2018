package com.adventofcode;

import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

import java.util.stream.Stream;

class Day1 {

    int solvePart1(Stream<String> lines) {
        return lines.mapToInt(this::parse).sum();
    }

    int solvePart2(List<String> lines) {
        return doSolvePart2(0, HashSet.of(0), lines);
    }

    private int doSolvePart2(int initial, HashSet<Integer> seen, List<String> lines) {
        Tuple3<Integer, HashSet<Integer>, Integer> result = lines
                .map(this::parse)
                .foldLeft(Tuple.of(initial, seen, null), (Tuple3<Integer, HashSet<Integer>, Integer> acc, Integer el) -> {
                    int sumSoFar = acc._1 + el;
                    if (acc._3 != null) {
                        return acc;
                    } else if (acc._2.contains(sumSoFar)) {
                        return Tuple.of(sumSoFar, acc._2, sumSoFar);
                    } else {
                        return Tuple.of(sumSoFar, acc._2.add(sumSoFar), null);
                    }
                });
        return result._3 != null ? result._3 : doSolvePart2(result._1, result._2, lines);
    }

    private int parse(String string) {
        return Integer.parseInt(string);
    }
}
