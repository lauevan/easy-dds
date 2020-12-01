package cn.lauevan.easy.dds.core.lookup;

import cn.lauevan.easy.dds.core.DataSourceLookupKeyHolder;
import cn.lauevan.easy.dds.core.enums.DataSourceType;
import cn.lauevan.easy.dds.core.exception.DDSLookupKeySwitchException;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public final class DynamicDataSourceHelper {

    private DynamicDataSourceHelper() {
    }

    public static void select(DataSourceType type) {
        select(null, null, type);
    }

    public static void select(Long tenantId) {
        select(tenantId, null, null);
    }

    public static void select(String appId) {
        select(null, appId, null);
    }

    public static void select(Long tenantId, String appId, DataSourceType type) {

        final IDataSourceLookupKey current = DataSourceLookupKeyHolder.current();
        DataSourceStdLookupKey newLookupKey;

        if (Objects.nonNull(current)) {

            if (!(current instanceof DataSourceStdLookupKey)) {
                throw new DDSLookupKeySwitchException("The lookup key switches error.");
            }

            DataSourceStdLookupKey currentLookupKey = (DataSourceStdLookupKey) current;
            newLookupKey = new DataSourceStdLookupKey();

            if (Objects.isNull(tenantId)) {
                newLookupKey.setTenantId(currentLookupKey.getTenantId());
            } else {
                newLookupKey.setTenantId(tenantId);
            }

            if (Objects.isNull(type)) {
                newLookupKey.setType(currentLookupKey.getType());
            } else {
                newLookupKey.setType(type);
            }

            if (StringUtils.isBlank(appId)) {
                newLookupKey.setAppId(currentLookupKey.getAppId());
            } else {
                newLookupKey.setAppId(appId);
            }
        } else {

            newLookupKey = DataSourceStdLookupKey.builder()
                    .tenantId(tenantId)
                    .appId(appId)
                    .type(type)
                    .build();
        }

        DataSourceLookupKeyHolder.changeTo(newLookupKey);
    }
}
