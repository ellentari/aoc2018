package com.adventofcode;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

class Day24Test {

    @Test
    void solvePart1Sample() {
        List<String> input = List.of(
                "Immune System:",
                        "17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2",
                        "989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3",
                        "",
                        "Infection:",
                        "801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1",
                        "4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4"
        );

        int result = new Day24().solvePart1(input);

        assertThat(result).isEqualTo(5216);
    }

    @Test
    void solvePart2Sample() {
        List<String> input = List.of(
                "Immune System:",
                        "17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2",
                        "989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3",
                        "",
                        "Infection:",
                        "801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1",
                        "4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4"
        );

        int result = new Day24().solvePart2(1570, input);

        assertThat(result).isEqualTo(51);
    }

    @Test
    void solvePart1() {
        List<String> input = ResourceUtils.readLines("day24.txt");

        int result = new Day24().solvePart1(input);

        assertThat(result).isEqualTo(16747);
    }

    @Test
    void solvePart2() {
        List<String> input = ResourceUtils.readLines("day24.txt");

        int result = new Day24().solvePart2(45, input);

        assertThat(result).isEqualTo(5923);
    }
}