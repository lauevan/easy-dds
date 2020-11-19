package cn.lauevan.easy.dds.core.util;

public class Tuple<V1, V2> {

    private V1 value1;
    private V2 value2;

    private Tuple() {
    }

    private Tuple(V1 value1, V2  value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public static <V1, V2> Tuple<V1, V2> newTuple(V1 value1, V2 value2) {
        return new Tuple<>(value1, value2);
    }

    public V1 value1() {
        return value1;
    }

    public V2 value2() {
        return value2;
    }
}
