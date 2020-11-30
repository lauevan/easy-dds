package cn.lauevan.easy.dds.core.bean;

import cn.lauevan.easy.dds.core.enums.DataSourceLookupStrategy;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class DataSourceCompositeConfigBean implements Serializable {

    private static final long serialVersionUID = -7650566822296131051L;

    private DataSourceLookupStrategy lookupStrategy;
    private DataSourceConfigBean main;
    private Map<String, DataSourceConfigBean> mapping;
}
