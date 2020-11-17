package cn.lauevan.easy.dds.core.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class DataSourceCompositeConfigBean implements Serializable {

    private static final long serialVersionUID = -7650566822296131051L;

    private DataSourceConfigBean main;
    private Map<String, List<DataSourceConfigBean>> mapping;
}
