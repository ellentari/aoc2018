package com.adventofcode;

import io.vavr.Tuple2;
import io.vavr.collection.Array;
import io.vavr.collection.HashMap;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import java.util.Objects;

import static io.vavr.Function4.lift;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;

class Day16 {

    private static final Map<Integer, Opcode> OPCODES = List.<Opcode>of(
            Opcode::addr,
            Opcode::addi,
            Opcode::multr,
            Opcode::multi,
            Opcode::banr,
            Opcode::bani,
            Opcode::borr,
            Opcode::bori,
            Opcode::setr,
            Opcode::seti,
            Opcode::gtir,
            Opcode::gtri,
            Opcode::gtrr,
            Opcode::eqir,
            Opcode::eqri,
            Opcode::eqrr
    )
            .zipWithIndex()
            .toMap(Tuple2::_2, Tuple2::_1);

    int solvePart1(List<String> input) {
        return parseSamples(input)
                .count(e -> getPossibleOpcodes(e, OPCODES).size() >= 3);
    }

    int solvePart2(List<String> input1, List<String> input2) {
        Set<Sample> entries = parseSamples(input1).toSet();
        List<Instruction> instructions = parseInstructions(input2);

        Map<Integer, Opcode> opcodes = identifyOpcodes(entries, OPCODES);

        return execute(Array.of(0, 0, 0, 0), instructions, opcodes).head();
    }

    private Array<Integer> execute(Array<Integer> initial, List<Instruction> instructions, Map<Integer, Opcode> opcodes) {
        return instructions.foldLeft(initial, (registers, instruction) ->
                opcodes.get(instruction.opcode).get().execute(registers, instruction.a, instruction.b, instruction.c)
        );
    }

    private Map<Integer, Opcode> identifyOpcodes(Set<Sample> samples, Map<Integer, Opcode> opcodes) {
        if (samples.isEmpty() || opcodes.isEmpty()) {
            return HashMap.empty();
        }

        Map<Sample, Integer> foundOpcodes = samples
                .toMap(identity(), sample -> getPossibleOpcodes(sample, opcodes))
                .filterValues(ops -> ops.size() == 1)
                .mapValues(List::head);

        Map<Integer, Opcode> identified = foundOpcodes
                .mapKeys(s -> s.instruction.opcode)
                .mapValues(i -> opcodes.get(i).get());

        return identified.merge(identifyOpcodes(
                samples.removeAll(foundOpcodes.keySet()),
                opcodes.removeAll(foundOpcodes.values())
        ));
    }

    private List<Integer> getPossibleOpcodes(Sample sample, Map<Integer, Opcode> opcodes) {
        return opcodes
                .filterValues(opcode ->
                        trySample(sample, opcode).map(r -> r.equals(sample.after)).getOrElse(false)
                )
                .map(Tuple2::_1)
                .toList();
    }

    private Option<Array<Integer>> trySample(Sample sample, Opcode opcode) {
        return lift(opcode::execute)
                .apply(sample.before, sample.instruction.a, sample.instruction.b, sample.instruction.c);
    }

    private static Iterator<Sample> parseSamples(List<String> input) {
        return input.grouped(4).map(Day16::parseSample);
    }

    private static Sample parseSample(List<String> input) {
        return new Sample(
                parseRegister(input.get(0)),
                parseInstruction(input.get(1)),
                parseRegister(input.get(2))
        );
    }

    private static List<Instruction> parseInstructions(List<String> input) {
        return input.map(Day16::parseInstruction);
    }

    private static Instruction parseInstruction(String input) {
        int[] instruction = stream(input.split("\\s+")).mapToInt(Integer::parseInt).toArray();

        return new Instruction(
                instruction[0],
                instruction[1],
                instruction[2],
                instruction[3]
        );
    }

    private static Array<Integer> parseRegister(String input) {
        return Array.ofAll(stream(getArrayFrom(input).split(",\\s+")).map(Integer::parseInt));
    }

    private static String getArrayFrom(String input) {
        return input.substring(input.indexOf('[') + 1, input.length() - 1);
    }

    private static class Sample {
        final Array<Integer> before;
        final Instruction instruction;
        final Array<Integer> after;

        Sample(Array<Integer> before, Instruction instruction, Array<Integer> after) {
            this.before = before;
            this.instruction = instruction;
            this.after = after;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Sample sample = (Sample) o;
            return Objects.equals(before, sample.before) &&
                    Objects.equals(instruction, sample.instruction) &&
                    Objects.equals(after, sample.after);
        }

        @Override
        public int hashCode() {
            return Objects.hash(before, instruction, after);
        }
    }

    private static class Instruction {
        final int opcode;
        final int a;
        final int b;
        final int c;

         Instruction(int opcode, int a, int b, int c) {
            this.opcode = opcode;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Instruction that = (Instruction) o;
            return opcode == that.opcode &&
                    a == that.a &&
                    b == that.b &&
                    c == that.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(opcode, a, b, c);
        }
    }

    private interface Opcode {

        Array<Integer> execute(Array<Integer> registers, int a, int b, int c);

        static Array<Integer> addr(Array<Integer> registers, int a, int b, int c) {
            return registers.update(c, registers.get(a) + registers.get(b));
        }

        static Array<Integer> addi(Array<Integer> registers, int a, int b, int c) {
            return registers.update(c, registers.get(a) + b);
        }

        static Array<Integer> multr(Array<Integer> registers, int a, int b, int c) {
            return registers.update(c, registers.get(a) * registers.get(b));
        }

        static Array<Integer> multi(Array<Integer> registers, int a, int b, int c) {
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
}