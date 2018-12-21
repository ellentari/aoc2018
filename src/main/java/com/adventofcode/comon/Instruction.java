package com.adventofcode.comon;

import io.vavr.collection.Array;

public interface Instruction {

    Array<Long> execute(Array<Long> registers);

    static Instruction fromString(String s) {
        String[] split = s.split(" ");

        long a = Integer.parseInt(split[1]);
        long b = Integer.parseInt(split[2]);
        int c = Integer.parseInt(split[3]);

        switch (split[0]) {
            case "addr": return registers -> Opcode.addr(registers, a, b, c);
            case "addi": return registers -> Opcode.addi(registers, a, b, c);
            case "mulr": return registers -> Opcode.mulr(registers, a, b, c);
            case "muli": return registers -> Opcode.muli(registers, a, b, c);
            case "banr": return registers -> Opcode.banr(registers, a, b, c);
            case "bani": return registers -> Opcode.bani(registers, a, b, c);
            case "borr": return registers -> Opcode.borr(registers, a, b, c);
            case "bori": return registers -> Opcode.bori(registers, a, b, c);
            case "setr": return registers -> Opcode.setr(registers, a, b, c);
            case "seti": return registers -> Opcode.seti(registers, a, b, c);
            case "gtir": return registers -> Opcode.gtir(registers, a, b, c);
            case "gtri": return registers -> Opcode.gtri(registers, a, b, c);
            case "gtrr": return registers -> Opcode.gtrr(registers, a, b, c);
            case "eqir": return registers -> Opcode.eqir(registers, a, b, c);
            case "eqri": return registers -> Opcode.eqri(registers, a, b, c);
            case "eqrr": return registers -> Opcode.eqrr(registers, a, b, c);

            default: throw new IllegalArgumentException(s);
        }
    }
}
