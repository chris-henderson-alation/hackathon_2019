package serialize.TablesTable;

import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Table;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableExtData {

    public String m_name;
    public String m_viewExpandedText;
    public String m_viewOriginalText;
    public String m_hiveTableType;
    public String m_owner;
    public Date m_createTime;

    public List<String> m_partitionKeys;
    public String m_dataLocation;
    public String m_serdeName;
    public String m_serdeLib;
    public List<String> m_bucketKeys;
    public List<String> m_sortKeys;
    public List<String> m_skewColumns;
    public List<List<String>> m_skewValueTuples;
    public String m_remarks;

    // The legacy codebase has YAML serialzer that ignores null values, which means
    // Hive MDE won't include stuff such as a SAP HANA specific String "m_sapHanaTableType"
    //
    // Unfortunately these three fields are neither relevant toHive nor are they Objects - they are non-nullable primitives.
    // Therefor, they always showed up in the return object with their defualt values of 0 and false.
    public long m_currentSpaceBytes;
    public long m_peakSpaceBytes;
    public boolean m_isMaterializedView;

    // Refer to alation.extractor.adbc.hivemetastore.HiveMsSchemaTableIterator::convertToTableExtData
    // for insight on what we're trying to recreate here.
    public TableExtData(Table table) {
        this.m_name = table.getTableName();
        this.m_viewExpandedText = table.getViewExpandedText();
        this.m_viewOriginalText = table.getViewOriginalText();
        this.m_hiveTableType = table.getTableType();
        this.m_owner = table.getOwner();
        this.m_createTime = new Date(table.getCreateTime() * 1000L);
        this.m_partitionKeys = table.getPartitionKeys().stream().map(FieldSchema::getName).collect(Collectors.toList());
        this.m_dataLocation = table.getSd().getLocation();
        this.m_serdeName = table.getSd().getSerdeInfo().getName();
        this.m_serdeLib = table.getSd().getSerdeInfo().getSerializationLib();
        this.m_bucketKeys = table.getSd().getBucketCols();
        this.m_sortKeys = table.getSd().getSortCols().stream()
                .map(key -> key.getOrder() < 0 ? "-" : "" + key.getCol())
                .collect(Collectors.toList());
        this.m_skewColumns = table.getSd().getSkewedInfo().getSkewedColNames();
        this.m_skewValueTuples = table.getSd().getSkewedInfo().getSkewedColValues();
        this.m_remarks = table.getParameters().get("comment");
    }

    // Empty constructors are necessary for snakeyaml to deserialize these.
    public TableExtData() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableExtData that = (TableExtData) o;
        return m_currentSpaceBytes == that.m_currentSpaceBytes &&
                m_peakSpaceBytes == that.m_peakSpaceBytes &&
                m_isMaterializedView == that.m_isMaterializedView &&
                Objects.equals(m_name, that.m_name) &&
                Objects.equals(m_viewExpandedText, that.m_viewExpandedText) &&
                Objects.equals(m_viewOriginalText, that.m_viewOriginalText) &&
                Objects.equals(m_hiveTableType, that.m_hiveTableType) &&
                Objects.equals(m_owner, that.m_owner) &&
                Objects.equals(m_createTime, that.m_createTime) &&
                Objects.equals(m_partitionKeys, that.m_partitionKeys) &&
                Objects.equals(m_dataLocation, that.m_dataLocation) &&
                Objects.equals(m_serdeName, that.m_serdeName) &&
                Objects.equals(m_serdeLib, that.m_serdeLib) &&
                Objects.equals(m_bucketKeys, that.m_bucketKeys) &&
                Objects.equals(m_sortKeys, that.m_sortKeys) &&
                Objects.equals(m_skewColumns, that.m_skewColumns) &&
                Objects.equals(m_skewValueTuples, that.m_skewValueTuples) &&
                Objects.equals(m_remarks, that.m_remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_name, m_viewExpandedText, m_viewOriginalText, m_hiveTableType, m_owner, m_createTime, m_partitionKeys, m_dataLocation, m_serdeName, m_serdeLib, m_bucketKeys, m_sortKeys, m_skewColumns, m_skewValueTuples, m_remarks, m_currentSpaceBytes, m_peakSpaceBytes, m_isMaterializedView);
    }
}
