package cn.lauevan.easy.dds.core;

import cn.lauevan.easy.dds.core.bean.DefaultDataSourceBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class DynamicDataSourceStarter {

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    private String routingDataSourceBeanName;
    private DefaultDataSourceBean defaultDataSourceBean;

    public DynamicDataSourceStarter(String beanName, DefaultDataSourceBean defaultDataSource) {
        this.routingDataSourceBeanName = beanName;
        this.defaultDataSourceBean = defaultDataSource;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onRefreshed(ContextRefreshedEvent refreshedEvent) {

        if (Objects.isNull(defaultDataSourceBean)
                || StringUtils.isBlank(routingDataSourceBeanName)) {
            // TODO 改成自定义异常
            throw new RuntimeException("配置错误");
        }

        if (INITIALIZED.compareAndSet(false, true)) {
            ApplicationContext ctx = refreshedEvent.getApplicationContext();

            DataSource defaultDataSource = DataSourceBuilder
                    .create()
                    .type(HikariDataSource.class)
                    .url(defaultDataSourceBean.getUrl())
                    .username(defaultDataSourceBean.getUsername())
                    .password(defaultDataSourceBean.getPassword())
                    .driverClassName(defaultDataSourceBean.getDriverClassName())
                    .build();

            if (ctx instanceof AnnotationConfigApplicationContext) {
                AnnotationConfigApplicationContext configCtx =
                        (AnnotationConfigApplicationContext) ctx;
                if (configCtx.containsBean(routingDataSourceBeanName)) {
                    configCtx.removeBeanDefinition(routingDataSourceBeanName);
                } else {
                    final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
                            DynamicRoutingDataSource.class);
                    configCtx.registerBeanDefinition(routingDataSourceBeanName, builder.getBeanDefinition());
                }
            }
        }
    }

    @EventListener(ContextClosedEvent.class)
    public void onClosed(ContextClosedEvent closedEvent) {

    }
}
