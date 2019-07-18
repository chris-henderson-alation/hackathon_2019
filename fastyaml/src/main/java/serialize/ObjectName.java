package serialize;

import java.util.Objects;

public class ObjectName {

    public String m_name;
    public SchemaName m_schemaName;

    public ObjectName(String m_name, SchemaName m_schemaName) {
        this.m_name = m_name;
        this.m_schemaName = m_schemaName;
    }

    public ObjectName(SchemaName m_schemaName) {
        this.m_schemaName = m_schemaName;
    }

    /**
     * clone is useful if you wish to refer to the same data more than once within a YAML
     * serialized datastructure, but you do not wish for the result to use YAML aliases.
     *
     * An alias is essentially a pointer of the form &id<NUMBER> that lets you cheaply refer
     * to the same object multiple times within one document.
     *
     * https://www.google.com/search?q=YAML+alias
     *
     * https://yaml.org/refcard.html
     *
     * @return
     */
    public ObjectName clone() {
        return new ObjectName(this.m_name, new SchemaName(this.m_schemaName.m_schema));
    }

    // Empty constructors are necessary for snakeyaml to deserialize these.
    public ObjectName() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectName that = (ObjectName) o;
        return Objects.equals(m_name, that.m_name) &&
                Objects.equals(m_schemaName, that.m_schemaName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_name, m_schemaName);
    }
}
