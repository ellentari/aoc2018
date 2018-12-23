package com.adventofcode;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Optimize;
import io.vavr.collection.List;

import java.util.HashMap;
import java.util.Objects;

import static java.util.Comparator.comparingInt;

class Day23 {

    int solvePart1(List<String> input) {
        List<Nanobot> nanobots = input.map(this::parse);
        Nanobot strongest = nanobots.maxBy(comparingInt(Nanobot::getSignal)).get();

        return nanobots.count(n -> isInRange(n.position, strongest));
    }

    int solvePart2(List<String> input) {
        List<Nanobot> nanobots = input.map(this::parse);

        try (Context ctx = new Context(new HashMap<>())) {
            Optimize opt = ctx.mkOptimize();

            IntExpr x = ctx.mkIntConst("x");
            IntExpr y = ctx.mkIntConst("y");
            IntExpr z = ctx.mkIntConst("z");

            opt.MkMaximize(nanobotsInRangeNumber(nanobots, ctx, x, y, z));
            opt.MkMinimize(distanceFromStart(ctx, x, y, z));

            opt.Check();

            Model model = opt.getModel();

            return distance(
                    new Position(0, 0, 0),
                    new Position(getInt(model, x), getInt(model, y), getInt(model, z))
            );
        }
    }

    private ArithExpr nanobotsInRangeNumber(List<Nanobot> nanobots, Context ctx, IntExpr x, IntExpr y, IntExpr z) {
        IntExpr[] orig = xyz(x, y, z);

        return ctx.mkAdd(nanobots
                .map(n -> ctx.mkITE(z3InRange(ctx, orig, n), ctx.mkInt(1), ctx.mkInt(0)))
                .map(e -> (ArithExpr) e).toJavaArray(ArithExpr.class)
        );
    }

    private ArithExpr distanceFromStart(Context ctx, IntExpr x, IntExpr y, IntExpr z) {
        return z3Distance(ctx, xyz(ctx, 0, 0, 0), xyz(x, y, z));
    }

    private IntExpr[] xyz(IntExpr x, IntExpr y, IntExpr z) {
        return new IntExpr[] {x, y, z};
    }

    private IntExpr[] xyz(Context ctx, int x, int y, int z) {
        return xyz(ctx.mkInt(x), ctx.mkInt(y), ctx.mkInt(z));
    }

    private IntExpr[] xyz(Context ctx, Position position) {
        return xyz(ctx, position.x, position.y, position.z);
    }

    private BoolExpr z3InRange(Context ctx, IntExpr[] position, Nanobot nanobot) {
        return ctx.mkLe(z3Distance(ctx, position, xyz(ctx, nanobot.position)), ctx.mkInt(nanobot.signal));
    }

    private ArithExpr z3Distance(Context ctx, IntExpr[] p1, IntExpr[] p2) {
        ArithExpr xAbs = z3Abs(ctx, ctx.mkSub(p1[0], p2[0]));
        ArithExpr yAbs = z3Abs(ctx, ctx.mkSub(p1[1], p2[1]));
        ArithExpr zAbs = z3Abs(ctx, ctx.mkSub(p1[2], p2[2]));

        return ctx.mkAdd(xAbs, yAbs, zAbs);
    }

    private ArithExpr z3Abs(Context ctx, ArithExpr x) {
        return (ArithExpr) ctx.mkITE(ctx.mkLt(x, ctx.mkInt(0)), ctx.mkUnaryMinus(x), x);
    }

    private int getInt(Model model, IntExpr intExpr) {
        return Integer.parseInt(model.eval(intExpr, true).toString());
    }

    private int distance(Position p1, Position p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) + Math.abs(p1.z - p2.z);
    }

    private boolean isInRange(Position position, Nanobot nanobot) {
        return distance(position, nanobot.position) <= nanobot.signal;
    }

    private Nanobot parse(String s) {
        String[] split = s.split(", r=");
        String[] split1 = split[0].split("[(<=),>]");

        int signal = Integer.parseInt(split[1]);
        int x = Integer.parseInt(split1[2]);
        int y = Integer.parseInt(split1[3]);
        int z = Integer.parseInt(split1[4]);

        return new Nanobot(new Position(x, y, z), signal);
    }

    private static class Position {
        final int x;
        final int y;
        final int z;

        Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x &&
                    y == position.y &&
                    z == position.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    private static class Nanobot {
        final Position position;
        final int signal;

        Nanobot(Position position, int signal) {
            this.position = position;
            this.signal = signal;
        }

        int getSignal() {
            return signal;
        }
    }
}
