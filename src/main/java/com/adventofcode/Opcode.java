package com.adventofcode;

import io.vavr.collection.Array;

interface Opcode {

    Array<Long> execute(Array<Long> registers, long a, long b, int c);

    static Array<Long> addr(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a) + registers.get((int) b));
    }

    static Array<Long> addi(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a) + b);
    }

    static Array<Long> mulr(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a) * registers.get((int) b));
    }

    static Array<Long> muli(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a) * b);
    }

    static Array<Long> banr(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a) & registers.get((int) b));
    }

    static Array<Long> bani(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a) & b);
    }

    static Array<Long> borr(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a) | registers.get((int) b));
    }

    static Array<Long> bori(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a) | b);
    }

    static Array<Long> setr(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a));
    }

    static Array<Long> seti(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, a);
    }

    static Array<Long> gtir(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, a > registers.get((int) b) ? 1L : 0);
    }

    static Array<Long> gtri(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a) > b ? 1L : 0);
    }

    static Array<Long> gtrr(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a) > registers.get((int) b) ? 1L : 0);
    }

    static Array<Long> eqir(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, a == registers.get((int) b) ? 1L : 0);
    }

    static Array<Long> eqri(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a) == b ? 1L : 0);
    }

    static Array<Long> eqrr(Array<Long> registers, long a, long b, int c) {
        return registers.update(c, registers.get((int) a).equals(registers.get((int) b)) ? 1L : 0);
    }
}
