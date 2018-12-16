package com.adventofcode.comon;

import java.util.Objects;

import static java.util.Comparator.comparingInt;

public class Coordinate implements Comparable<Coordinate> {

    public final int i;
    public final int j;

    public Coordinate(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public <T> T at(T[][] grid) {
        return grid[i][j];
    }

    public Coordinate left() {
        return new Coordinate(i, j - 1);
    }

    public Coordinate right() {
        return new Coordinate(i, j + 1);
    }

    public Coordinate top() {
        return new Coordinate(i - 1, j);
    }

    public Coordinate bottom() {
        return new Coordinate(i + 1, j);
    }

    @Override
    public String toString() {
        return "[" + i + ", " + j + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return i == that.i &&
                j == that.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }

    @Override
    public int compareTo(Coordinate o) {
        return comparingInt(Coordinate::getI)
                .thenComparingInt(Coordinate::getJ)
                .compare(this, o);
    }
}
