package part2;

import java.io.Serializable;

public record Pair<T1 extends Serializable, T2 extends Serializable>(T1 first, T2 second) implements Serializable {}

