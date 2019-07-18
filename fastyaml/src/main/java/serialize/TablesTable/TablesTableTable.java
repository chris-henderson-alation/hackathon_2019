package serialize.TablesTable;

import serialize.ObjectName;
import serialize.SchemaName;
import org.apache.hadoop.hive.metastore.api.Table;

import java.util.Objects;

public class TablesTableTable {

    public TableExtData m_extData;
    public ObjectName m_objName;
    public String m_tableType;

    public TablesTableTable(Table table) {
        this.m_objName = new ObjectName(table.getTableName(), new SchemaName(table.getDbName()));
        this.m_tableType = this.hiveTypeToJDBCType(table.getTableType());
        this.m_extData = new TableExtData(table);
    }

    private String hiveTypeToJDBCType(String type) {
        switch (type) {
            case "MANAGED_TABLE":
                return "TABLE";
            case "EXTERNAL_TABLE":
                return "TABLE";
            case "VIRTUAL_VIEW":
                return "VIEW";
            default:
                return type;
        }
    }

    // Empty constructors are necessary for snakeyaml to deserialize these.
    public TablesTableTable() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TablesTableTable that = (TablesTableTable) o;
        return Objects.equals(m_extData, that.m_extData) &&
                Objects.equals(m_objName, that.m_objName) &&
                Objects.equals(m_tableType, that.m_tableType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_extData, m_objName, m_tableType);
    }
}
