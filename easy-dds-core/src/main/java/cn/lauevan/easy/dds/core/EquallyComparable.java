package cn.lauevan.easy.dds.core;

public interface EquallyComparable {

    @Override
    int hashCode();

    @Override
    boolean equals(Object obj);
}
