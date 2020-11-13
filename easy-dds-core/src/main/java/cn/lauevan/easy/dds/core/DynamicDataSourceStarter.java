package cn.lauevan.easy.dds.core;

import cn.lauevan.easy.dds.core.bean.DefaultDataSourceBean;
import cn.lauevan.easy.dds.core.enums.DataSourceLookupStrategy;
import cn.lauevan.easy.dds.core.exception.ConfigurationErrorException;
import com.google.common.base.Throwables;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class DynamicDataSourceStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceStarter.class);

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);
    private static final Class<? extends DataSource> DEFAULT_DATA_SOURCE_TYPE = HikariDataSource.class;
    private static final DataSourceLookupStrategy DEFAULT_LOOKUP_STRATEGY = DataSourceLookupStrategy.REMOTE_PULL;

    private String beanName;
    private String dataSourceType;
    private DefaultDataSourceBean defaultDataSourceBean;
    private DataSourceLookupStrategy lookupStrategy;
    private DataSource defaultDataSource;
    private Map<DataSourceLookupKey, DataSource> mappingDataSources;

    public DynamicDataSourceStarter(String routingDataSrouceBeanName,
                                    String routingDataSourceType,
                                    DataSourceLookupStrategy lookupStrategy,
                                    DefaultDataSourceBean defaultDataSourceBean) {
        this.beanName = routingDataSrouceBeanName;
        this.dataSourceType = routingDataSourceType;
        this.defaultDataSourceBean = defaultDataSourceBean;
        this.lookupStrategy = lookupStrategy;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onRefreshed(ContextRefreshedEvent refreshedEvent) {
        if (INITIALIZED.compareAndSet(false, true)) {
            check(defaultDataSourceBean);
            initialize(refreshedEvent.getApplicationContext());
        }
    }

    @EventListener(ContextClosedEvent.class)
    public void onClosed(ContextClosedEvent closedEvent) {

    }

    private void initialize(ApplicationContext ctx) {

        resolveBeanName();
        resolveLookupStrategy();

        lookupDefaultDataSource();
        lookupMappingDataSources();

        registerRoutingDataSource(
                lookupDefaultDataSource(),
                lookupMappingDataSources(),
                ctx
        );
    }

    private void registerRoutingDataSource(Map<DataSourceLookupKey, DataSource> mappingDataSources,
                                           DataSource defaultDataSource,
                                           ApplicationContext context) {


    }

    private DataSource lookupMappingDataSources() {
        // TODO 根据策略去查找数据源映射表
        return null;
    }

    private Map<DataSourceLookupKey, DataSource> lookupDefaultDataSource() {
        // TODO 根据策略查找默认数据源
        return null;
    }

    private void resolveLookupStrategy() {
        if (Objects.isNull(lookupStrategy)) {
            lookupStrategy = DEFAULT_LOOKUP_STRATEGY;
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends DataSource> resolveDataSourceType(String dataSourceType) {

        Class<? extends DataSource> dataSourceClass = null;

        if (StringUtils.isBlank(dataSourceType)) {
            dataSourceClass = DEFAULT_DATA_SOURCE_TYPE;
        } else {
            try {
                dataSourceClass = (Class<? extends DataSource>) Class.forName(dataSourceType);
            } catch (ClassNotFoundException e) {
                Throwables.propagate(e);
            }
        }

        return dataSourceClass;
    }

    private void resolveBeanName() {
        if (StringUtils.isBlank(beanName)) {
            beanName = lowercaseFirstChar(DynamicRoutingDataSource.class.getSimpleName());
        }
    }

    private String lowercaseFirstChar(String text) {

        String firstChar = text.substring(0, 1);
        String otherChars = text.substring(1);

        return firstChar.toLowerCase() + otherChars;
    }

    private void check(DefaultDataSourceBean defaultDataSourceBean) {
        doCheck(defaultDataSourceBean);
    }

    private void doCheck(DefaultDataSourceBean defaultDataSourceBean) {
        if (Objects.isNull(defaultDataSourceBean)) {
            throw new ConfigurationErrorException("The configuration bean not found.");
        }
    }

    private DataSource createDataSource(String url, String username, String password, String driverClassName) {
        return DataSourceBuilder
                .create()
                .type(resolveDataSourceType(dataSourceType))
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }
}
