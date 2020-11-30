package cn.lauevan.easy.dds.spring.boot.starter;

import cn.lauevan.easy.dds.core.DynamicRoutingDataSource;
import cn.lauevan.easy.dds.core.bean.DataSourceCompositeConfigBean;
import cn.lauevan.easy.dds.core.bean.DataSourceConfigBean;
import cn.lauevan.easy.dds.core.enums.DataSourceLookupStrategy;
import cn.lauevan.easy.dds.core.exception.DDSConfigurationErrorException;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static cn.lauevan.easy.dds.core.enums.DataSourceLookupStrategy.LOCAL_CONFIG_READ;
import static cn.lauevan.easy.dds.core.enums.DataSourceLookupStrategy.REMOTE_PULL;
import static cn.lauevan.easy.dds.core.util.WordHelper.lowercaseFirstChar;

/**
 * <p>动态数据源创建器</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 17, 2020 at 16:57:12 GMT+8
 */
public class DynamicDataSourceCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceCreator.class);

    private static final Class<? extends DataSource> DEFAULT_DATA_SOURCE_TYPE = HikariDataSource.class;
    private static final String DEFAULT_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final DataSourceLookupStrategy DEFAULT_LOOKUP_STRATEGY = REMOTE_PULL;
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    /**
     * 动态数据源的 bean 名称
     */
    private String beanName;
    private String dataSourceType;
    private DataSourceCompositeConfigBean compositeConfigBean;
    private DataSourceLookupStrategy lookupStrategy;

    private DataSource mainDataSource;
    private Map<Object, Object> mappingDataSources;


    DynamicDataSourceCreator(String routingDataSrouceBeanName,
                             String routingDataSourceType,
                             DataSourceLookupStrategy lookupStrategy,
                             DataSourceCompositeConfigBean compositeConfigBean) {
        this.beanName = routingDataSrouceBeanName;
        this.dataSourceType = routingDataSourceType;
        this.compositeConfigBean = compositeConfigBean;
        this.lookupStrategy = lookupStrategy;
    }

    void initialize() {
        if (INITIALIZED.compareAndSet(false, true)) {
            resolveBeanName();
            resolveLookupStrategy();

            check(compositeConfigBean);

            mainDataSource = lookupDefaultDataSource();
            mappingDataSources = lookupMappingDataSources();
        }
    }

    private String resolveDriverClassName(String driverClassName) {
        if (StringUtils.isBlank(driverClassName)) {
            return DEFAULT_DRIVER_CLASS_NAME;
        }
        return driverClassName;
    }

    DataSource create() {

        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource(mainDataSource, mappingDataSources);
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }

    @EventListener(ContextClosedEvent.class)
    public void onClosed(ContextClosedEvent closedEvent) {

    }

    private DataSource lookupDefaultDataSource() {

        DataSource defaultDataSource = null;

        final Optional<DataSourceConfigBean> configOptional = loadConfigs(true)
                .values()
                .stream()
                .findFirst();

        if (configOptional.isPresent()) {
            final DataSourceConfigBean config = configOptional.get();
            defaultDataSource = createRawDataSource(config.getUrl(),
                    config.getUsername(),
                    config.getPassword(),
                    config.getDriverClassName());
        }

        return defaultDataSource;
    }

    private Map<Object, Object> lookupMappingDataSources() {
        Map<Object, Object> mappings = Maps.newHashMap();
        final Map<String, DataSourceConfigBean> configs = loadConfigs(false);
        Set<Map.Entry<String, DataSourceConfigBean>> entrys = configs.entrySet();
        entrys.forEach(e -> {
            final DataSourceConfigBean config = e.getValue();
            mappings.put(e.getKey(), createRawDataSource(config.getUrl(), config.getUsername(),
                            config.getPassword(), config.getDriverClassName()));
        });
        return mappings;
    }

    private Map<String, DataSourceConfigBean> loadConfigs(boolean isDefault) {

        Map<String, DataSourceConfigBean> configs = Maps.newHashMap();

        if (lookupStrategy == LOCAL_CONFIG_READ) {
            if (isDefault) {
                configs.put(null, compositeConfigBean.getMain());
            } else {
                Map<String, DataSourceConfigBean> mappings = compositeConfigBean.getMapping();
                Set<Map.Entry<String, DataSourceConfigBean>> entrys = mappings.entrySet();
                entrys.forEach(e -> configs.put(e.getKey(), e.getValue()));
            }
        } else if (lookupStrategy == REMOTE_PULL) {
            // TODO SPI load
        }

        return configs;
    }

    private void resolveLookupStrategy() {
        if (Objects.isNull(lookupStrategy)) {
            lookupStrategy = DEFAULT_LOOKUP_STRATEGY;
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends DataSource> resolveDataSourceType(String dataSourceType) {

        Class<? extends DataSource> dataSourceClass;

        if (StringUtils.isBlank(dataSourceType)) {
            dataSourceClass = DEFAULT_DATA_SOURCE_TYPE;
        } else {
            try {
                dataSourceClass = (Class<? extends DataSource>) Class.forName(dataSourceType);
            } catch (ClassNotFoundException e) {
                LOGGER.error("The data source type class [{}] not found.", dataSourceType, e);
                throw new DDSConfigurationErrorException("The data source type class not found.", e);
            }
        }

        return dataSourceClass;
    }

    private void resolveBeanName() {
        if (StringUtils.isBlank(beanName)) {
            beanName = lowercaseFirstChar(DynamicRoutingDataSource.class.getSimpleName());
        }
    }

    private void check(DataSourceCompositeConfigBean compositeConfigBean) {

        if (lookupStrategy == LOCAL_CONFIG_READ) {

            if (Objects.isNull(compositeConfigBean.getMain())) {
                LOGGER.error("The data source configuration [{}] not found.", "easy-dds.data-source.main");
                throw new DDSConfigurationErrorException("The data source configuration [easy-dds.datasource.main] not found.");
            }

            if (Objects.isNull(compositeConfigBean.getMapping())) {
                LOGGER.error("The data source configuration [{}] not found.", "easy-dds.data-source.mapping");
                throw new DDSConfigurationErrorException("The data source configuration [easy-dds.datasource.mapping] not found.");
            }
        }
    }

    private DataSource createRawDataSource(String url, String username, String password, String driverClassName) {
        return DataSourceBuilder
                .create()
                .type(resolveDataSourceType(dataSourceType))
                .driverClassName(resolveDriverClassName(driverClassName))
                .url(url)
                .username(username)
                .password(password)
                .build();
    }
}
