package serialize;

import org.apache.hive.jdbc.JdbcColumn;


import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.sql.Types;

/**
 * Traced back to alation.extractor.adbc.jdbc.typeStandardizer.normalizer
 *
 * See https://cwiki.apache.org/confluence/display/Hive/LanguageManual+Types for Hive type documentation.
 */
public class HiveToJDBC {

    private static final Logger LOGGER = Logger.getLogger(HiveToJDBC.class.getName());

    public static String toJDBCTypeString(String hiveType) {
        try {
            return JDBCEnumToString(hiveStringToJDBCEnum(hiveType));
        } catch (SQLException e) {
            LOGGER.warn(String.format("Failed to convert column type, \"%s\", to a JDBC type string. Defaulting to " +
                    "\"NA\". Please report this warning to Alation engineering.", hiveType));
            return "NA";
        }
    }

    private static int hiveStringToJDBCEnum(String name) throws SQLException {
        // Hive types may be "qualified", for example varchar is qualified to have
        // a length, which manifests as "varchar(25)". The TypeInfoUtils can strip away
        // these qualifications and return to us a string that can be used to map Hive types
        // to enumerated JDBC types.
        //
        // For a list of all of the Hive types and how their representation strings are constructed,
        // navigate to org.apache.hadoop.hive.serde2.typeinfo and start looking through the classes that
        // extend TypeInfo
        String basename = getBaseName(name);
        return hiveTypeToSqlType(basename);
    }


    private static int hiveTypeToSqlType(String typeName) throws SQLException {
        // Hive's own JDBC support seems to have forgotten about unions.
        if (typeName != null && typeName.startsWith(serdeConstants.UNION_TYPE_NAME)) {
            return Integer.MIN_VALUE;
        }
        return JdbcColumn.hiveTypeToSqlType(typeName);
    }

    private static String getBaseName(String typeName) {
        // This is really a copy oforg.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils.getBaseName
        // except this version honors the fact that arrays/map/struct/unions are parameterized on a type
        // us <>. For example:
        //      array<int>
        int idx = typeName.indexOf('(');
        if (idx != -1) {
            return typeName.substring(0, idx);
        }
        idx = typeName.indexOf('<');
        if (idx != -1) {
            return typeName.substring(0, idx);
        }
        return typeName;
    }

    /**
     * This function normalizes JDBC type to Alation's chosen string representation.
     *
     * @param type
     * @return String
     */
    private static String JDBCEnumToString(final int type) {
        String result = "NA";

        switch (type) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.CLOB:
            case Types.SQLXML:
            case Types.NCHAR:
            case Types.NCLOB:
            case Types.DATALINK:
            case Types.DISTINCT:
            case Types.LONGNVARCHAR:
            case Types.NVARCHAR:
                result = "STRING";
                break;

            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.ROWID:
                result = "INT";
                break;

            case Types.REAL:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                result = "FLOAT";
                break;

            case Types.BIT:
            case Types.BOOLEAN:
                result = "BOOL";
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
                result = "BYTES";
                break;

            case Types.DATE:
                result = "DATE";
                break;

            case Types.TIME:
                result = "TIME";
                break;

            case Types.TIMESTAMP:
                result = "TIMESTAMP";
                break;
            case Types.ARRAY:
                result = "ARRAY";
                break;
            case Types.STRUCT:
                result = "STRUCT";
                break;
            case Types.REF:
                result = "REF";
                break;

            case Types.JAVA_OBJECT:
                // Hive complex types like Map<> are categorized as Java-Objects
                result = "OBJECT";
                break;
        }
        return result;
    }
}
