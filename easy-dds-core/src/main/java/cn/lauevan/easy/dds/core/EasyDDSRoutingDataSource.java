package cn.lauevan.easy.dds.core;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * <p>数据源路由实现类</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 3, 2020 at 22:06:35 GMT+8
 */
public class EasyDDSRoutingDataSource extends AbstractRoutingDataSource {

    public EasyDDSRoutingDataSource(DataSource defaultDataSource, Map<Object, Object> targetDataSources) {
        setDefaultTargetDataSource(defaultDataSource);
        setTargetDataSources(targetDataSources);
    }

    @Override
    public Object determineCurrentLookupKey() {
        return null;
    }
}
