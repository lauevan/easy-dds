package cn.lauevan.easy.dds.spring.boot.starter.bean;

import cn.lauevan.easy.dds.core.bean.DataSourceCompositeConfigBean;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ConfigurationProperties(prefix = "easy-dds")
public class DynamicDataSourceConfigBean implements Serializable {

    private static final long serialVersionUID = -9086377150590469193L;

    private Boolean enabled;
    private String routingDataSourceBeanName;
    private String routingDataSourceType;
    private DataSourceCompositeConfigBean datasource = new DataSourceCompositeConfigBean();
}
