import serialize.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String ... argv) throws Exception {
        StringWriter s = new StringWriter();
        simple(s);
        System.out.println(s.toString());
    }

    public static void list(Writer out) throws Exception {
//        List<String> list = new ArrayList<>();
//        list.add("hello");
//        list.add("hello2");
//        YAML.write(list, out);
    }

    public static void simple(Writer out) throws Exception {
        Schema s = new Schema();
        s.m_locationUri = "http://some.place.com";
        s.m_objName = new ObjectName();
        s.m_objName.m_schemaName = new SchemaName("biggy");
        s.m_objName.m_name = "objname_name";
        s.m_remarks = "nope Paranah Plant, you're no fun";
        YAML.write(s, out);
    }
}
