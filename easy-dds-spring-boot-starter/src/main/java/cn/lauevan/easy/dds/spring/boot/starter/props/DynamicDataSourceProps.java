package cn.lauevan.easy.dds.spring.boot.starter.props;

import cn.lauevan.easy.dds.core.bean.DefaultDataSourceBean;
import cn.lauevan.easy.dds.core.enums.DataSourceLookupStrategy;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "easy-dds")
public class DynamicDataSourceProps {

    private Boolean enabled;
    private String routingDataSourceBeanName;
    private String routingDataSourceType;
    private DataSourceLookupStrategy dataSourceLookupStrategy;
    private DefaultDataSourceBean defaultDataSource;
}
