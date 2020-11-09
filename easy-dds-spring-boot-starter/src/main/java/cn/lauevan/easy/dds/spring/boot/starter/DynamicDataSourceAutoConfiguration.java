package cn.lauevan.easy.dds.spring.boot.starter;

import cn.lauevan.easy.dds.core.DynamicDataSourceStarter;
import cn.lauevan.easy.dds.spring.boot.starter.props.DynamicDataSourceProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@EnableConfigurationProperties(DynamicDataSourceProps.class)
public class DynamicDataSourceAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceAutoConfiguration.class);

    private final DynamicDataSourceProps dynamicDataSourceProps;

    public DynamicDataSourceAutoConfiguration(DynamicDataSourceProps dynamicDataSourceProps) {
        this.dynamicDataSourceProps = dynamicDataSourceProps;
    }

    @Bean
    public DynamicDataSourceStarter dynamicDataSourceStarter() {
        return new DynamicDataSourceStarter(dynamicDataSourceProps.getDefaultDataSource());
    }
}
