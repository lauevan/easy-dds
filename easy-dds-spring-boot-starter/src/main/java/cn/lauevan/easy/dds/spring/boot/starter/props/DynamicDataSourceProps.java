package cn.lauevan.easy.dds.spring.boot.starter.props;

import cn.lauevan.easy.dds.core.bean.DefaultDataSourceBean;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "easy-dds")
public class DynamicDataSourceProps {

    private DefaultDataSourceBean defaultDataSource;
    private String routingDataSrouceBeanName;
}
