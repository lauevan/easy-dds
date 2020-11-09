package cn.lauevan.easy.dds.core.bean;

import lombok.Data;

@Data
public class DefaultDataSourceBean {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
