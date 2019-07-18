use jni::objects::{JObject, JString, JList};
use jni::JNIEnv;

pub const STRING: &str = "Ljava/lang/String;";
pub const OBJECT: &str = "Ljava/lang/Object;";
pub const INT: &str = "I";

pub fn to_string(env: &JNIEnv, jstring: JObject) -> String {
   env.get_string(JString::from(jstring)).unwrap().into()
}

pub fn to_string_vec(env: &JNIEnv, jlist: JObject) -> Vec<String> {
    let jlist = JList::from_env(&env, jlist).unwrap();
    jlist.iter().unwrap().map(|obj| to_string(env, obj)).collect()
}

pub fn to_obj_vec<'a>(env: &'a JNIEnv, jlist: JObject<'a>) -> Vec<JObject<'a>> {
    let jlist = JList::from_env(&env, jlist).unwrap();
    jlist.iter().unwrap().map(|obj| obj).collect()
}

pub fn to_vvs(env: &JNIEnv, jlist: JObject) -> Vec<Vec<String>> {
    let jlist = JList::from_env(&env, jlist).unwrap();
    jlist.iter().unwrap().map(|obj| to_string_vec(env, obj)).collect()
}

pub fn get_string_field<'a>(env: &'a JNIEnv, parent: &'a JObject, name: &str) -> JObject<'a> {
    env.get_field(*parent, name, STRING).unwrap().l().unwrap()
}

pub fn get_int_field<'a>(env: &'a JNIEnv, parent: &'a JObject, name: &str) -> u32 {
    env.get_field(*parent, name, INT).unwrap().i().unwrap() as u32
}