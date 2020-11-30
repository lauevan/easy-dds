package cn.lauevan.easy.dds.core.lookup;

import cn.lauevan.easy.dds.core.IEquallyComparable;

/**
 * <p>数据源映射 Key 的定义</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 5, 2020 at 23:49:06 GMT+8
 */
@FunctionalInterface
public interface IDataSourceLookupKey extends IEquallyComparable {

    /**
     * 默认分隔符
     */
    String DEFAULT_LOOKUP_KEY_SEPARATOR = "_";

    String create();
}
