package serialize.TableCols;

import serialize.ObjectName;
import serialize.SchemaName;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Table;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class TableColsTable {


    public ObjectName m_objName;

    // This is a list of objects because the original is a list of
    // objects, which causes individual items to have a tag, like so:
    //
    //  - !adbc:column
    //      'm_columnName': 'test_col1'
    //      'm_normType': 'INT'
    //      ...
    //
    // Which may or may not be necessary for callers to deserialize, but we're
    // not willing to stress test that here and now.
    //
    // Additionally, a preallocated array of the same size as the number of columns
    // in the provided table allows us to map the columns to their destination without
    // losing ordinality as well as maintaining a lock free datastructure.
    public List<Column> m_subObjects;

    public TableColsTable(Table table) {
        this.m_objName = new ObjectName(table.getTableName(), new SchemaName(table.getDbName()));
        List<FieldSchema> columns = table.getSd().getCols();
        columns.addAll(table.getPartitionKeys());
        Column[] m_subObjects = new Column[columns.size()];
        IntStream.range(0, columns.size()).parallel().forEach(i -> {
            m_subObjects[i] = new Column(columns.get(i), this.m_objName.clone(), i + 1);
        });
        this.m_subObjects = Arrays.asList(m_subObjects);
    }

    // Empty constructors are necessary for snakeyaml to deserialize these.
    public TableColsTable() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableColsTable that = (TableColsTable) o;
        return Objects.equals(m_objName, that.m_objName) &&
                Objects.equals(m_subObjects, that.m_subObjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_objName, m_subObjects);
    }
}
