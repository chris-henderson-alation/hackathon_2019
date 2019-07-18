package serialize;

import org.apache.hadoop.hive.metastore.api.Database;

import java.util.Objects;

public class SchemaName {

    public String m_schema;

    public SchemaName(String m_schema) {
        this.m_schema = m_schema;
    }

    public SchemaName(Database database) {
        this.m_schema = database.getName();
    }

    // Empty constructors are necessary for snakeyaml to deserialize these.
    public SchemaName() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemaName that = (SchemaName) o;
        return Objects.equals(m_schema, that.m_schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_schema);
    }
}
