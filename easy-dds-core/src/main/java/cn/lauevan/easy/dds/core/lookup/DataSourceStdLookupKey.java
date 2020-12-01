package cn.lauevan.easy.dds.core.lookup;

import cn.lauevan.easy.dds.core.enums.DataSourceType;
import cn.lauevan.easy.dds.core.exception.DDSLookupKeyIncorrectException;
import cn.lauevan.easy.dds.core.lookup.IDataSourceLookupKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * <p>简单的 lookup key 实现</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 19, 2020 at 09:29:07 GMT+8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataSourceStdLookupKey implements IDataSourceLookupKey {

    private static final String STD_LOOKUP_KEY_ALL_ARGS_FMT =
            "%s" + DEFAULT_LOOKUP_KEY_SEPARATOR + "%s" + DEFAULT_LOOKUP_KEY_SEPARATOR + "%s";

    private static final String STD_LOOKUP_KEY_TWO_ARGS_FMT =
            "%s" + DEFAULT_LOOKUP_KEY_SEPARATOR + "%s";

    private Long tenantId;
    private String appId;
    private DataSourceType type;

    @Override
    public String create() {

        if (Objects.isNull(tenantId) || StringUtils.isBlank(appId)) {
            throw new DDSLookupKeyIncorrectException("The lookup key that tenantId and appId are required");
        }

        String key;

        if (Objects.isNull(type)) {
            key = String.format(STD_LOOKUP_KEY_TWO_ARGS_FMT, tenantId, appId);
        } else {
            key = String.format(STD_LOOKUP_KEY_ALL_ARGS_FMT, tenantId, appId, type.name());
        }

        return key;
    }
}
