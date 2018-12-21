package com.adventofcode;

import com.adventofcode.comon.Instruction;
import io.vavr.collection.Array;
import io.vavr.collection.Stream;

import java.util.LinkedHashSet;
import java.util.Set;

class Day21 {

    private static final int IP_REG = 2;

    int solvePart1() {
        Array<Instruction> instructions = input().map(Instruction::fromString).toArray();
        Array<Long> registers = Array.of(0L, 0L, 0L, 0L, 0L, 0L);

        int ip = 0;

        while (ip >= 0 && ip < instructions.length()) {
            registers = registers.update(IP_REG, (long) ip);

            if (ip == 28) {
                return registers.get(3).intValue();
            }

            registers = instructions.get(ip).execute(registers);

            ip = registers.get(IP_REG).intValue();
            ip++;
        }

        return -1;
    }

    int solvePart2() {
        Array<Instruction> instructions = input().map(Instruction::fromString).toArray();
        Array<Long> registers = Array.of(0L, 0L, 0L, 0L, 0L, 0L);

        int ip = 0;

        Set<Long> reg3Values = new LinkedHashSet<>();

        while (ip >= 0 && ip < instructions.length()) {
            registers = registers.update(IP_REG, (long) ip);

            if (ip == 28) {
                if (reg3Values.contains(registers.get(3))) {
                    return Stream.ofAll(reg3Values).last().intValue();
                } else {
                    reg3Values.add(registers.get(3));
                }
            }

            registers = instructions.get(ip).execute(registers);

            ip = registers.get(IP_REG).intValue();
            ip++;
        }

        return -1;
    }

    private Array<String> input() {
        return Array.of(
                "seti 123 0 3",
                "bani 3 456 3",
                "eqri 3 72 3",
                "addr 3 2 2",
                "seti 0 0 2",
                "seti 0 4 3",
                "bori 3 65536 4",
                "seti 1107552 3 3",
                "bani 4 255 5",
                "addr 3 5 3",
                "bani 3 16777215 3",
                "muli 3 65899 3",
                "bani 3 16777215 3",
                "gtir 256 4 5",
                "addr 5 2 2",
                "addi 2 1 2",
                "seti 27 0 2",
                "seti 0 2 5",
                "addi 5 1 1",
                "muli 1 256 1",
                "gtrr 1 4 1",
                "addr 1 2 2",
                "addi 2 1 2",
                "seti 25 3 2",
                "addi 5 1 5",
                "seti 17 3 2",
                "setr 5 3 4",
                "seti 7 4 2",
                "eqrr 3 0 5",
                "addr 5 2 2",
                "seti 5 8 2"
        );
    }
}
