package cn.lauevan.easy.dds.core.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataSourceConfigBean implements Serializable {

    private static final long serialVersionUID = -941883607549465484L;

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
