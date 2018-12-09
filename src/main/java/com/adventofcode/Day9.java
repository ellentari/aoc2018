package com.adventofcode;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;

class Day9 {

    long solvePart1(int playersCount, int marblesCount) {
        List<Integer> circle = new ArrayList<>(marblesCount + 1);

        long[] scores = new long[playersCount];

        circle.add(0);
        circle.add(1);

        int current = 1;

        for (int marble = 2, player = 1; marble <= marblesCount; marble++, player = (player + 1) % playersCount) {
            if (marble % 23 == 0) {
                scores[player] += marble;
                int toRemove = removeIndex(circle, current);
                scores[player] += circle.remove(toRemove);
                current = toRemove;
            } else {
                int toInsert = insertIndex(circle, current);
                circle.add(toInsert, marble);
                current = toInsert;
            }
        }

        return max(scores);
    }

    private long max(long[] scores) {
        return stream(scores).max()
                .orElseThrow(IllegalStateException::new);
    }

    private int insertIndex(List<Integer> circle, int current) {
        return ((current + 1) % (circle.size()) + 1) % (circle.size() + 1);
    }

    private int removeIndex(List<Integer> circle, int current) {
        return removeIndex(circle.size(), current);
    }

    private static int removeIndex(int size, int current) {
        if (current - 7 < 0) {
            return size - (7 - current);
        }
        return current - 7;
    }
}
