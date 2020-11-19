package cn.lauevan.easy.dds.spring.boot.starter;

import cn.lauevan.easy.dds.spring.boot.starter.bean.DynamicDataSourceConfigBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * <p>Easy DDS Spring boot stater 自动配置类</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 3, 2020 at 21:56:14 GMT+8
 */
@Configuration
@EnableConfigurationProperties(DynamicDataSourceConfigBean.class)
@ConditionalOnProperty(prefix = "easy-dds", name = "enabled", havingValue = "true")
public class DynamicDataSourceAutoConfiguration {

    @Bean
    public DynamicDataSourceCreator dynamicDataSourceCreator(DynamicDataSourceConfigBean dynamicDataSourceConfig) {

        final DynamicDataSourceCreator creator = new DynamicDataSourceCreator(
                dynamicDataSourceConfig.getRoutingDataSourceBeanName(),
                dynamicDataSourceConfig.getRoutingDataSourceType(),
                dynamicDataSourceConfig.getDatasource().getLookupStrategy(),
                dynamicDataSourceConfig.getDatasource());

        creator.initialize();

        return creator;
    }

    @Bean
    @Primary
    public DataSource dynamicDataSource(DynamicDataSourceCreator creator) {
        return creator.create();
    }
}
