package com.adventofcode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day20Test {

    private static Stream<Arguments> part1Samples() {
        return Stream.of(
                Arguments.of("^WNE$", 3),
                Arguments.of("^ENWWW(NEEE|SSE(EE|N))$", 10),
                Arguments.of("^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$", 18),
                Arguments.of("^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$", 23),
                Arguments.of("^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$", 31)
        );
    }

    @ParameterizedTest
    @MethodSource("part1Samples")
    void solvePart1Samples(String pathRegexp, int farthestDoor) {
        int result = new Day20().solvePart1(pathRegexp);

        assertThat(result).isEqualTo(farthestDoor);
    }

    @Test
    void solvePart1() {
        String input = ResourceUtils.readString("day20.txt");

        int result = new Day20().solvePart1(input);

        assertThat(result).isEqualTo(3788);
    }

    @Test
    void solvePart2() {
        String input = ResourceUtils.readString("day20.txt");

        int result = new Day20().solvePart2(input);

        assertThat(result).isEqualTo(8568);
    }
}