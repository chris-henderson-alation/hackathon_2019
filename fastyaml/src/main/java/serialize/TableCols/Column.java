package serialize.TableCols;

import serialize.HiveToJDBC;
import serialize.ObjectName;
import org.apache.hadoop.hive.metastore.api.FieldSchema;

import java.util.Objects;

/**
 * Derived from alation.extractor.adbc.core.base.DbColumn
 */
public class Column {

    // https://yaml.org/type/bool.html
    // Which is to say, yes, "Y" and "N" are
    // apparently valid YAML boolean types.
    public enum Nullable {
        Y,
        N,
    }

    public String m_columnName;
    public String m_normType;
    public Nullable m_nullable;
    public String m_remarks;
    public String m_typeName;

    public int m_ordinalPosition;
    public ObjectName m_objName;

    public Column(FieldSchema col, ObjectName m_objName, int m_ordinalPosition) {
        this.m_columnName = col.getName();
        this.m_normType = HiveToJDBC.toJDBCTypeString(col.getType());
        this.m_nullable = Nullable.Y;
        this.m_typeName = col.getType();
        this.m_remarks = col.getComment();
        this.m_objName = m_objName;
        this.m_ordinalPosition = m_ordinalPosition;
    }

    // Empty constructors are necessary for snakeyaml to deserialize these.
    public Column() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return m_ordinalPosition == column.m_ordinalPosition &&
                Objects.equals(m_columnName, column.m_columnName) &&
                Objects.equals(m_normType, column.m_normType) &&
                m_nullable == column.m_nullable &&
                Objects.equals(m_remarks, column.m_remarks) &&
                Objects.equals(m_typeName, column.m_typeName) &&
                Objects.equals(m_objName, column.m_objName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_columnName, m_normType, m_nullable, m_remarks, m_typeName, m_ordinalPosition, m_objName);
    }
}
