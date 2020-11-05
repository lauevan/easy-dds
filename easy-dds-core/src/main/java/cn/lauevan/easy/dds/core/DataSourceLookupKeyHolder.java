package cn.lauevan.easy.dds.core;

import org.springframework.lang.NonNull;

public class DataSourceLookupKeyHolder {

    private static final ThreadLocal<DataSourceLookupKey> LOOKUP_KEY_HOLDER = new InheritableThreadLocal<>();

    public static void changeTo(@NonNull DataSourceLookupKey lookupKey) {
        LOOKUP_KEY_HOLDER.set(lookupKey);
    }

    public static DataSourceLookupKey current() {
        return LOOKUP_KEY_HOLDER.get();
    }

    public static void clear() {
        LOOKUP_KEY_HOLDER.remove();
    }
}
