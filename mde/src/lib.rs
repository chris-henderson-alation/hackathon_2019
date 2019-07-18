extern crate jni;
extern crate serde;
extern crate serde_yaml;

use jni::JNIEnv;
use jni::objects::{JClass, JObject};
use jni::sys::jstring;
use serde::Serialize;

mod convert;

#[derive(Serialize)]
#[allow(non_snake_case)]
struct Schema {
    pub m_locationUri: String,
    pub m_objName: ObjectName,
    pub m_remarks: String
}

impl Schema {
    pub fn new(env: &JNIEnv, object: JObject) -> Schema {
        let location = convert::get_string_field(env, &object, "m_locationUri");
        let remarks = convert::get_string_field(env, &object, "m_remarks");
        Schema{
            m_locationUri: convert::to_string(env, location),
            m_objName: ObjectName { m_name: "".to_string(), m_schemaName: SchemaName { m_schema: "".to_string() } },
            m_remarks: convert::to_string(env, remarks)
        }
    }
}

#[derive(Serialize)]
#[allow(non_snake_case)]
struct SchemaName {
    pub m_schema: String
}

impl SchemaName {
    pub fn new(env: &JNIEnv, object: JObject) -> SchemaName {
        let schema = convert::get_string_field(env, &object, "m_schema");
        SchemaName{m_schema: convert::to_string(env, schema)}
    }
}

#[derive(Serialize)]
#[allow(non_snake_case)]
struct ObjectName {
    pub m_name: String,
    pub m_schemaName: SchemaName
}

impl ObjectName {
    pub fn new(env: &JNIEnv, object: JObject) -> ObjectName {
        let name = convert::get_string_field(env, &object, "m_name");
        let schema_name = SchemaName::new(env,convert::get_string_field(env, &object, "m_schemaName"));
        ObjectName{ m_name: convert::to_string(env, name), m_schemaName: schema_name }
    }
}

#[derive(Serialize)]
#[allow(non_snake_case)]
struct TablesTableTable {
    pub m_extData: TableExtData,
    pub m_objName: ObjectName,
    pub m_tableType: String
}

#[allow(non_snake_case)]
impl TablesTableTable {
    pub fn new(env: &JNIEnv, object: JObject) -> TablesTableTable {
        let m_extData = convert::get_string_field(env, &object, "m_extData");
        let m_extData = TableExtData::new(env, m_extData);

        let m_objName = convert::get_string_field(env, &object, "m_objName");
        let m_objName = ObjectName::new(env, m_objName);

        let m_tableType = convert::get_string_field(env, &object, "m_tableType");
        let m_tableType = convert::to_string(env, m_tableType);

        TablesTableTable{
            m_extData,
            m_objName,
            m_tableType
        }
    }
}

#[derive(Serialize)]
#[allow(non_snake_case)]
struct TableExtData {
    pub m_name: String,
    pub m_viewExpandedText: String,
    pub m_viewOriginalText: String,
    pub m_hiveTableType: String,
    pub m_owner: String,
    pub m_createTime: String,

    pub m_partitionKeys: Vec<String>,
    pub m_dataLocation: String,
    pub m_serdeName: String,
    pub m_serdeLib: String,
    pub m_bucketKeys: Vec<String>,
    pub m_sortKeys: Vec<String>,
    pub m_skewColumns: Vec<String>,
    pub m_skewValueTuples: Vec<Vec<String>>,
    pub m_remarks: String,

    // The legacy codebase has YAML serialzer that ignores null values, which means
    // Hive MDE won't include stuff such as a SAP HANA specific String "m_sapHanaTableType"
    //
    // Unfortunately these three fields are neither relevant toHive nor are they Objects - they are non-nullable primitives.
    // Therefor, they always showed up in the return object with their defualt values of 0 and false.
    pub m_currentSpaceBytes: u64,
    pub m_peakSpaceBytes: u64,
    pub m_isMaterializedView: bool,
}

#[allow(non_snake_case)]
impl TableExtData {
    pub fn new(env: &JNIEnv, object: JObject) -> TableExtData {
        let m_name = convert::get_string_field(env, &object, "m_name");
        let m_name = convert::to_string(env, m_name);

        let m_viewExpandedText = convert::get_string_field(env, &object, "m_viewExpandedText");
        let m_viewExpandedText = convert::to_string(env, m_viewExpandedText);

        let m_viewOriginalText = convert::get_string_field(env, &object, "m_viewOriginalText",);
        let m_viewOriginalText = convert::to_string(env, m_viewOriginalText);

        let m_hiveTableType = convert::get_string_field(env, &object, "m_hiveTableType");
        let m_hiveTableType = convert::to_string(env, m_hiveTableType);

        let m_owner = convert::get_string_field(env, &object, "m_owner");
        let m_owner = convert::to_string(env, m_owner);

        let m_createTime = convert::get_string_field(env, &object, "m_createTime");
        let m_createTime = convert::to_string(env, m_createTime);

        let m_partitionKeys = convert::get_string_field(env, &object, "m_partitionKeys");
        let m_partitionKeys = convert::to_string_vec(&env, m_partitionKeys);

        let m_dataLocation = convert::get_string_field(env, &object, "m_dataLocation");
        let m_dataLocation = convert::to_string(env, m_dataLocation);

        let m_serdeName = convert::get_string_field(env, &object, "m_serdeName");
        let m_serdeName = convert::to_string(env, m_serdeName);

        let m_serdeLib = convert::get_string_field(env, &object, "m_serdeLib");
        let m_serdeLib = convert::to_string(env, m_serdeLib);

        let m_bucketKeys = convert::get_string_field(env, &object, "m_bucketKeys");
        let m_bucketKeys = convert::to_string_vec(&env, m_bucketKeys);

        let m_sortKeys = convert::get_string_field(env, &object, "m_sortKeys");
        let m_sortKeys = convert::to_string_vec(&env, m_sortKeys);

        let m_skewColumns = convert::get_string_field(env, &object, "m_skewColumns");
        let m_skewColumns = convert::to_string_vec(&env, m_skewColumns);

        let m_skewValueTuples = convert::get_string_field(env, &object, "m_skewValueTuples");
        let m_skewValueTuples = convert::to_vvs(&env, m_skewValueTuples);

        let m_remarks = convert::get_string_field(env, &object, "m_remarks");
        let m_remarks = convert::to_string(env, m_remarks);

        TableExtData{
            m_name,
            m_viewExpandedText,
            m_viewOriginalText,
            m_hiveTableType,
            m_owner,
            m_createTime,
            m_partitionKeys,
            m_dataLocation,
            m_serdeName,
            m_serdeLib,
            m_bucketKeys,
            m_sortKeys,
            m_skewColumns,
            m_skewValueTuples,
            m_remarks,
            m_currentSpaceBytes: 0,
            m_peakSpaceBytes: 0,
            m_isMaterializedView: false
        }
    }
}

#[derive(Serialize)]
#[allow(non_snake_case)]
struct Column {
    pub m_columnName: String,
    pub m_normalType: String,
    pub m_nullable: bool,
    pub m_remarks: String,
    pub m_typeName: String,
    pub m_ordinalPosition: u32,
    pub m_objName: ObjectName
}

#[allow(non_snake_case)]
impl Column {
    pub fn new(env: &JNIEnv, object: JObject) -> Column {
        let m_columnName = convert::get_string_field(env, &object, "m_columnName");
        let m_columnName = convert::to_string(env, m_columnName);

        let m_normalType = convert::get_string_field(env, &object, "m_normalType");
        let m_normalType = convert::to_string(env, m_normalType);

        let m_nullable = true;

        let m_remarks = convert::get_string_field(env, &object, "m_remarks");
        let m_remarks = convert::to_string(env, m_remarks);

        let m_typeName = convert::get_string_field(env, &object, "m_typeName");
        let m_typeName = convert::to_string(env, m_typeName);

        let m_ordinalPosition = convert::get_int_field(env, &object, "m_ordinalPosition");

        let m_objName = convert::get_string_field(env, &object, "m_objName");
        let m_objName = ObjectName::new(env, m_objName);

        Column{
            m_columnName,
            m_normalType,
            m_nullable,
            m_remarks,
            m_typeName,
            m_ordinalPosition,
            m_objName
        }
    }
}

#[derive(Serialize)]
#[allow(non_snake_case)]
struct TablesColsTable {
    pub m_objName: ObjectName,
    pub m_subObjects : Vec<Column>
}

#[allow(non_snake_case)]
impl TablesColsTable {
    pub fn new(env: &JNIEnv, object: JObject) -> TablesColsTable {
        let m_objName = convert::get_string_field(env, &object, "m_objName");
        let m_objName = ObjectName::new(env, m_objName);

        let m_subObjects  = convert::get_string_field(env, &object, "m_subObjects");
        let m_subObjects  = convert::to_obj_vec(env, m_subObjects );
        let m_subObjects: Vec<Column>  = m_subObjects.into_iter().map(|obj| Column::new(env, obj)).collect();

        TablesColsTable{ m_objName, m_subObjects }
    }
}

// GOLDEN
//
//#[no_mangle]
//#[allow(non_snake_case)]
//pub extern "system" fn Java_serialize_YAML_serialize(env: JNIEnv, _class: JClass, input: JObject) -> jstring {
//    let schema = Schema::new(&env, input);
//    let result = serde_yaml::to_string(&schema).unwrap();
//    env.new_string(result).unwrap().into_inner()
//}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_serialize_YAML_serialize_1schema(env: JNIEnv, _class: JClass, input: JObject) -> jstring {
    env.new_string(serde_yaml::to_string(&Schema::new(&env, input)).unwrap()).unwrap().into_inner()
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_serialize_YAML_serialize_1schema_1name(env: JNIEnv, _class: JClass, input: JObject) -> jstring {
    env.new_string(serde_yaml::to_string(&SchemaName::new(&env, input)).unwrap()).unwrap().into_inner()
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_serialize_YAML_serialize_1tables_1table(env: JNIEnv, _class: JClass, input: JObject) -> jstring {
    env.new_string(serde_yaml::to_string(&TablesTableTable::new(&env, input)).unwrap()).unwrap().into_inner()
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_serialize_YAML_serialize_1table_1cols(env: JNIEnv, _class: JClass, input: JObject) -> jstring {
    env.new_string(serde_yaml::to_string(&TablesColsTable::new(&env, input)).unwrap()).unwrap().into_inner()
}

#[cfg(test)]
mod tests {
    #[test]
    fn it_works() {
        assert_eq!(2 + 2, 4);
    }
}
