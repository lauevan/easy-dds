package cn.lauevan.easy.dds.core;

import cn.lauevan.easy.dds.core.enums.DataSourceType;
import cn.lauevan.easy.dds.core.spi.IDataSourceLookupKey;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

/**
 * <p>简单的 lookup key 实现</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 19, 2020 at 09:29:07 GMT+8
 */
@Data
@AllArgsConstructor
public class DataSourceTypeLookupKey implements IDataSourceLookupKey {

    private String key;
    private DataSourceType type;

    @Override
    public String create() {
        return key + DEFAULT_LOOKUP_KEY_SEPARATOR + type.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataSourceTypeLookupKey)) {
            return false;
        }
        DataSourceTypeLookupKey that = (DataSourceTypeLookupKey) o;
        return Objects.equals(key, that.key) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, type);
    }
}
