package cn.lauevan.easy.dds.core;

import cn.lauevan.easy.dds.core.bean.DataSourceCompositeConfigBean;
import cn.lauevan.easy.dds.core.bean.DataSourceConfigBean;
import cn.lauevan.easy.dds.core.enums.DataSourceLookupStrategy;
import cn.lauevan.easy.dds.core.exception.DDSConfigurationErrorException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static cn.lauevan.easy.dds.core.enums.DataSourceLookupStrategy.LOCAL_CONFIG_READ;
import static cn.lauevan.easy.dds.core.enums.DataSourceLookupStrategy.REMOTE_PULL;

/**
 * <p>动态数据源启动器</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 17, 2020 at 16:57:12 GMT+8
 */
public class DynamicDataSourceStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceStarter.class);

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);
    private static final Class<? extends DataSource> DEFAULT_DATA_SOURCE_TYPE = HikariDataSource.class;
    private static final DataSourceLookupStrategy DEFAULT_LOOKUP_STRATEGY = REMOTE_PULL;

    /**
     * 动态数据源的 bean 名称
     */
    private String beanName;
    private String dataSourceType;
    private DataSourceCompositeConfigBean compositeConfigBean;
    private DataSourceLookupStrategy lookupStrategy;

    public DynamicDataSourceStarter(String routingDataSrouceBeanName,
                                    String routingDataSourceType,
                                    DataSourceLookupStrategy lookupStrategy,
                                    DataSourceCompositeConfigBean compositeConfigBean) {
        this.beanName = routingDataSrouceBeanName;
        this.dataSourceType = routingDataSourceType;
        this.compositeConfigBean = compositeConfigBean;
        this.lookupStrategy = lookupStrategy;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onRefreshed(ContextRefreshedEvent refreshedEvent) {
        if (INITIALIZED.compareAndSet(false, true)) {
            resolveBeanName();
            resolveLookupStrategy();

            check(compositeConfigBean);

            initialize(refreshedEvent.getApplicationContext());
        }
    }

    @EventListener(ContextClosedEvent.class)
    public void onClosed(ContextClosedEvent closedEvent) {

    }

    private void initialize(ApplicationContext ctx) {
        registerRoutingDataSource(
                lookupMainDataSource(),
                lookupMappingDataSources(),
                ctx
        );
    }

    private void registerRoutingDataSource(DataSource mainDataSource,
                                           Map<DataSourceLookupKey, DataSource> mappingDataSources,
                                           ApplicationContext context) {

        // TODO
    }

    private DataSource lookupMainDataSource() {
        Map<DataSourceLookupKey, List<DataSourceConfigBean>> configs = lookupConfigs(true);
        final DataSourceConfigBean config = configs.values()
                .stream()
                .findFirst()
                .orElse(Lists.newArrayList())
                .get(0);
        return createDataSource(config.getUrl(),
                config.getUsername(),
                config.getPassword(),
                config.getDriverClassName());
    }

    private Map<DataSourceLookupKey, List<DataSourceConfigBean>> lookupConfigs(boolean isMain) {

        Map<DataSourceLookupKey, List<DataSourceConfigBean>> configs = Maps.newHashMap();

        if (lookupStrategy == LOCAL_CONFIG_READ) {
            if (isMain) {
                configs.put(null, Lists.newArrayList(compositeConfigBean.getMain()));
            } else {
                Map<String, List<DataSourceConfigBean>> mappings = compositeConfigBean.getMapping();
                Set<Map.Entry<String, List<DataSourceConfigBean>>> entrys = mappings.entrySet();
                entrys.forEach(e -> configs.put(e::getKey, e.getValue()));
            }
        } else if (lookupStrategy == REMOTE_PULL) {
            // TODO SPI load
        }

        return configs;
    }

    private Map<DataSourceLookupKey, DataSource> lookupMappingDataSources() {
        // TODO 根据策略去查找数据源映射表
        return null;
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

    private String lowercaseFirstChar(String text) {

        String firstChar = text.substring(0, 1);
        String otherChars = text.substring(1);

        return firstChar.toLowerCase() + otherChars;
    }

    private void check(DataSourceCompositeConfigBean compositeConfigBean) {

        if (lookupStrategy == LOCAL_CONFIG_READ) {

            if (Objects.isNull(compositeConfigBean)) {
                LOGGER.error("The data source configuration [{}] not found.", "easy-dds.data-source");
                throw new DDSConfigurationErrorException("The data source configuration [easy-dds.data-source] not found.");
            }

            if (Objects.isNull(compositeConfigBean.getMain())) {
                LOGGER.error("The data source configuration [{}] not found.", "easy-dds.data-source.main");
                throw new DDSConfigurationErrorException("The data source configuration [easy-dds.data-source.main] not found.");
            }

            if (Objects.isNull(compositeConfigBean.getMapping())) {
                LOGGER.error("The data source configuration [{}] not found.", "easy-dds.data-source.mapping");
                throw new DDSConfigurationErrorException("The data source configuration [easy-dds.data-source.mapping] not found.");
            }
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
