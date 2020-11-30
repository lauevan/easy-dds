package cn.lauevan.easy.dds.core;

import cn.lauevan.easy.dds.core.lookup.IDataSourceLookupKey;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

/**
 * <p>数据源路由实现类</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 3, 2020 at 22:06:35 GMT+8
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    public DynamicRoutingDataSource(DataSource defaultDataSource, Map<Object, Object> targetDataSources) {
        setDefaultTargetDataSource(defaultDataSource);
        setTargetDataSources(targetDataSources);
    }

    @Override
    public Object determineCurrentLookupKey() {

        final IDataSourceLookupKey current = DataSourceLookupKeyHolder.current();

        if (Objects.nonNull(current)) {
            return current.create();
        }

        return null;
    }
}
