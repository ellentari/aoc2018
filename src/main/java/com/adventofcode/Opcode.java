package com.adventofcode;

import io.vavr.collection.Array;

interface Opcode {

    Array<Integer> execute(Array<Integer> registers, int a, int b, int c);

    static Array<Integer> addr(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a) + registers.get(b));
    }

    static Array<Integer> addi(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a) + b);
    }

    static Array<Integer> mulr(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a) * registers.get(b));
    }

    static Array<Integer> muli(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a) * b);
    }

    static Array<Integer> banr(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a) & registers.get(b));
    }

    static Array<Integer> bani(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a) & b);
    }

    static Array<Integer> borr(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a) | registers.get(b));
    }

    static Array<Integer> bori(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a) | b);
    }

    static Array<Integer> setr(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a));
    }

    static Array<Integer> seti(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, a);
    }

    static Array<Integer> gtir(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, a > registers.get(b) ? 1 : 0);
    }

    static Array<Integer> gtri(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a) > b ? 1 : 0);
    }

    static Array<Integer> gtrr(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a) > registers.get(b) ? 1 : 0);
    }

    static Array<Integer> eqir(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, a == registers.get(b) ? 1 : 0);
    }

    static Array<Integer> eqri(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a) == b ? 1 : 0);
    }

    static Array<Integer> eqrr(Array<Integer> registers, int a, int b, int c) {
        return registers.update(c, registers.get(a).equals(registers.get(b)) ? 1 : 0);
    }
}
