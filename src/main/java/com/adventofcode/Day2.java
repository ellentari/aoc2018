package com.adventofcode;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Array;
import io.vavr.collection.IndexedSeq;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

class Day2 {

    long solvePart1(Stream<String> input) {
        Tuple2<Long, Long> result = input
                .map(this::countLetters)
                .map(this::toCounts)
                .reduce(Tuple.of(0L, 0L), (a, b) -> Tuple.of(a._1 + b._1, a._2 + b._2));

        return result._1 * result._2;
    }

    private Map<Character, Long> countLetters(String s) {
        return s.chars().boxed().collect(groupingBy(letter -> (char) letter.intValue(), counting()));
    }

    private Tuple2<Long, Long> toCounts(Map<Character, Long> map) {
        return Tuple.of(
                map.entrySet().stream().anyMatch(e -> e.getValue() == 2) ? 1L : 0,
                map.entrySet().stream().anyMatch(e -> e.getValue() == 3) ? 1L : 0
        );
    }

    String solvePart2(IndexedSeq<String> input) {
        return Array.range(0, input.length())
                .flatMap(i -> Array.range(0, input.length()).filter(j -> !i.equals(j)).map(j -> Tuple.of(i, j)))
                .map(t -> Tuple.of(input.get(t._1), input.get(t._2)))
                .find(this::differBy1)
                .map(this::commonLetters)
                .getOrNull();
    }

    private boolean differBy1(Tuple2<String, String> strings) {
        return zip(strings)
                .count(t -> t._1 != t._2) == 1;
    }

    private String commonLetters(Tuple2<String, String> strings) {
        return zip(strings)
                .filter(t -> t._1 == t._2)
                .foldLeft("", (acc, t) -> acc + t._1);
    }

    private Array<Tuple2<Character, Character>> zip(Tuple2<String, String> strings) {
        return Array.ofAll(strings._1.toCharArray())
                .zip(Array.ofAll(strings._2.toCharArray()));
    }
}
