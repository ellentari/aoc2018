package com.adventofcode;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Day14 {

    String solvePart1(int input) {
        List<Integer> board = new ArrayList<>();

        board.add(3);
        board.add(7);

        int firstElf = 0;
        int secondElf = 1;

        while (board.size() < input + 10) {
            int firstElfValue = board.get(firstElf);
            int secondElfValue = board.get(secondElf);

            int combined = firstElfValue + secondElfValue;

            List<Integer> toAdd = digits(combined);

            board.addAll(toAdd);

            firstElf = move(firstElf + firstElfValue + 1, board);
            secondElf = move(secondElf + secondElfValue + 1, board);
        }

        return score(board.subList(input, input + 10));
    }

    String solvePart2(String input) {
        StringBuilder board = new StringBuilder("37");

        int firstElf = 0;
        int secondElf = 1;

        while (board.length() < (input.length() + 1) || !last(board, input.length() + 1).contains(input)) {
            int firstElfValue = Character.digit(board.charAt(firstElf), 10);
            int secondElfValue = Character.digit(board.charAt(secondElf), 10);

            int combined = firstElfValue + secondElfValue;

            board.append(combined);

            firstElf = (firstElf + firstElfValue + 1) % board.length();
            secondElf = (secondElf + secondElfValue + 1) % board.length();
        }

        return String.valueOf(board.indexOf(input));
    }

    private String last(StringBuilder builder, int n) {
        return builder.length() > n ? builder.substring(builder.length() - n, builder.length()) : builder.toString();
    }

    private int move(int i, List<Integer> board) {
        return i % board.size();
    }

    private String score(List<Integer> integers) {
        return integers.stream().map(String::valueOf).collect(joining());
    }

    private List<Integer> digits(int combined) {
        return String.valueOf(combined).chars()
                .map(ch -> Character.digit((char) ch, 10))
                .boxed()
                .collect(toList());
    }
}
