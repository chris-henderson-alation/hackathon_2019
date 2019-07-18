package serialize;

import org.apache.hadoop.hive.metastore.api.Database;

import java.util.Objects;

public class Schema {

    public String m_locationUri;
    public ObjectName m_objName;
    public String m_remarks;

    public Schema(Database database) {
        this.m_locationUri = database.getLocationUri();
        this.m_objName = new ObjectName(new SchemaName(database));
        this.m_remarks = database.getDescription();
    }

    // Empty constructors are necessary for snakeyaml to deserialize these.
    public Schema() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schema schema = (Schema) o;
        return Objects.equals(m_locationUri, schema.m_locationUri) &&
                Objects.equals(m_objName, schema.m_objName) &&
                Objects.equals(m_remarks, schema.m_remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_locationUri, m_objName, m_remarks);
    }

}
