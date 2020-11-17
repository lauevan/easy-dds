package cn.lauevan.easy.dds.core;

/**
 * <p>数据源映射 Key 的定义</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 5, 2020 at 23:49:06 GMT+8
 */
@FunctionalInterface
public interface DataSourceLookupKey extends EquallyComparable {

    String createKey();
}
