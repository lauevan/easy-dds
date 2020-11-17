package cn.lauevan.easy.dds.spring.boot.starter;

import cn.lauevan.easy.dds.core.DynamicDataSourceStarter;
import cn.lauevan.easy.dds.spring.boot.starter.bean.DynamicDataSourceConfigBean;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthContributorAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Easy DDS Spring boot stater 自动配置类</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 3, 2020 at 21:56:14 GMT+8
 */
@Configuration
@AutoConfigureBefore({DataSourceAutoConfiguration.class, DataSourceHealthContributorAutoConfiguration.class})
@EnableConfigurationProperties(DynamicDataSourceConfigBean.class)
@ConditionalOnProperty(prefix = "easy-dds", name = "enabled", havingValue = "true")
public class DynamicDataSourceAutoConfiguration {

    @Bean
    public DynamicDataSourceStarter dynamicDataSourceStarter(DynamicDataSourceConfigBean dynamicDataSourceConfig) {
        return new DynamicDataSourceStarter(dynamicDataSourceConfig.getRoutingDataSourceBeanName(),
                dynamicDataSourceConfig.getRoutingDataSourceType(),
                dynamicDataSourceConfig.getDataSourceLookupStrategy(),
                dynamicDataSourceConfig.getDataSource());
    }
}
