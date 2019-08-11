package cn.net.metadata.base.service.core;

import java.math.BigDecimal;
import java.util.*;

public class MySQLConstant {

    public static final Map<String, Class> COLUMN_MAPPING = new HashMap<>();

    public static final List<String> SKIP_COLUMNS = new ArrayList<>();

    static {
        COLUMN_MAPPING.put("varchar", String.class);
        COLUMN_MAPPING.put("text", String.class);
        COLUMN_MAPPING.put("int", Integer.class);
        COLUMN_MAPPING.put("bigint", Long.class);
        COLUMN_MAPPING.put("tinyint", Integer.class);
        COLUMN_MAPPING.put("date", Date.class);
        COLUMN_MAPPING.put("datetime", Date.class);
        COLUMN_MAPPING.put("float", BigDecimal.class);
        COLUMN_MAPPING.put("decimal", BigDecimal.class);

        SKIP_COLUMNS.add("id");
        SKIP_COLUMNS.add("create_by");
        SKIP_COLUMNS.add("create_time");
        SKIP_COLUMNS.add("update_by");
        SKIP_COLUMNS.add("update_time");
        SKIP_COLUMNS.add("del_flag");
    }

    public static Class getFieldTypeByDataType(String dataType) {
        return COLUMN_MAPPING.get(dataType);
    }
}
