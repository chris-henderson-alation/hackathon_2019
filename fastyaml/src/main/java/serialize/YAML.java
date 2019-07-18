package serialize;

import serialize.TableCols.TableColsTable;
import serialize.TablesTable.TablesTableTable;

import java.io.Writer;
import java.io.IOException;

/**
 * YAML aggregates a snakeyaml serializer with the right options pre-chosen.
 *
 * YAML exposes only limited subset of its internal yaml serializer. In on small
 * part this is due to snakeyaml's proclivity for converting checked exceptions into
 * runtime exceptions, which is an API that we do not want to expose in general.
 */
public class YAML {

    private static native String serialize_schema(Object obj);
    private static native String serialize_schema_name(Object obj);
    private static native String serialize_table_cols(Object obj);
    private static native String serialize_tables_table(Object obj);

    static {
        System.loadLibrary("mde");
    }

    public YAML() {
    }

    public static void write(Schema obj, Writer out) throws IOException {
        out.write(serialize_schema(obj));
        out.flush();
    }

    public static void write(SchemaName obj, Writer out) throws IOException {
        out.write(serialize_schema_name(obj));
        out.flush();
    }

    public static void write(TablesTableTable obj, Writer out) throws IOException {
        out.write(serialize_tables_table(obj));
        out.flush();
    }

    public static void write(TableColsTable obj, Writer out) throws IOException {
        out.write(serialize_table_cols(obj));
        out.flush();
    }
}
