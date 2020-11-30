package cn.lauevan.easy.dds.core;

import cn.lauevan.easy.dds.core.lookup.IDataSourceLookupKey;
import org.springframework.lang.NonNull;

public class DataSourceLookupKeyHolder {

    private static final ThreadLocal<IDataSourceLookupKey> LOOKUP_KEY_HOLDER = new InheritableThreadLocal<>();

    public static void changeTo(@NonNull IDataSourceLookupKey lookupKey) {
        LOOKUP_KEY_HOLDER.set(lookupKey);
    }

    public static IDataSourceLookupKey current() {
        return LOOKUP_KEY_HOLDER.get();
    }

    public static void clear() {
        LOOKUP_KEY_HOLDER.remove();
    }
}
