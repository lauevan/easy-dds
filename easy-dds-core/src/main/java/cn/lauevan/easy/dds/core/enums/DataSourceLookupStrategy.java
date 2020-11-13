package cn.lauevan.easy.dds.core.enums;

/**
 * <p>数据源查找策略</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 13, 2020 at 16:57:35 GMT+8
 */
public enum DataSourceLookupStrategy {

    /**
     * 远程拉取
     */
    REMOTE_PULL,
    /**
     * 本地配置读取
     */
    LOCAL_CONFIG_READ
}
