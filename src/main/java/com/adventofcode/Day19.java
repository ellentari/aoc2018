package com.adventofcode;

import io.vavr.collection.Array;
import io.vavr.collection.IndexedSeq;

class Day19 {

    int solvePart1(int reg0, int ipReg, IndexedSeq<String> input) {
        Array<Instruction> instructions = input.map(Instruction::fromString).toArray();

        Array<Long> registers = Array.of((long) reg0, 0L, 0L, 0L, 0L, 0L);

        int ip = 0;

        while (ip >= 0 && ip < instructions.length()) {
            registers = registers.update(ipReg, (long) ip);

            registers = instructions.get(ip).execute(registers);

            ip = registers.get(ipReg).intValue();
            ip++;
        }

        return registers.get(0).intValue();
    }

}
