package heap.satrk.yaml.generator.model;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class YamlBean {
    Class c;
    String path;
    BufferedWriter bufferedWriter;

    public YamlBean(Class c, String path) throws IOException {
        this.c = c;
        this.path = path;
        new File(path + File.separator + "model").mkdir();
        File file = new File(path + File.separator + "model" + File.separator + c.getSimpleName() + ".yaml");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    public void genYaml() throws Exception {

        //写入默认头
        writeDef(bufferedWriter, c);

        //根据变量写入参数
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            writeFiled(field);
        }

        bufferedWriter.flush();
        bufferedWriter.close();

    }

    /**
     * 公共头
     *
     * @param bufferedWriter
     * @param c
     * @throws Exception
     */
    public void writeDef(BufferedWriter bufferedWriter, Class c) throws Exception {
        bufferedWriter.write("swagger: \"2.0\"");
        bufferedWriter.newLine();
        bufferedWriter.write("definitions:");
        bufferedWriter.newLine();
        bufferedWriter.write("  ");
        String name = c.getSimpleName();
        bufferedWriter.write(name.substring(0, 1).toLowerCase());
        bufferedWriter.write(name.substring(1));
        bufferedWriter.write(":");
        bufferedWriter.newLine();
        bufferedWriter.write("    x-jcloud-module: renewal");
        bufferedWriter.newLine();
        bufferedWriter.write(("    title: " + c.getSimpleName()));
        bufferedWriter.newLine();

        bufferedWriter.write("    type: object");
        bufferedWriter.newLine();

        bufferedWriter.write("    properties:");
        bufferedWriter.newLine();
    }

    /**
     * 当前仅支持 List String Integer Integer Date 以及包内Vo引用
     *
     * @param field
     * @throws Exception
     */
    public void writeFiled(Field field) throws Exception {
        String name = field.getType().getSimpleName();
        if (name.equals("String") || name.equals("Integer") || name.equals("Integer") || name.equals("int") || name.equals("BigDecimal")) {
            writeBasicFiled(field);
        } else if (field.getType().getPackage().getName().equals("model")) {
            writeObjectFiled(field);
        } else if (field.getType().getSimpleName().equals("List")) {
            writeListFiled(field);
        } else if (field.getType().getSimpleName().equals("Date")) {
            writeDateFiled(field);
        }
    }

    /**
     * 基本类型
     *
     * @param field
     * @throws Exception
     */
    public void writeBasicFiled(Field field) throws Exception {
        bufferedWriter.write(("      " + field.getName() + ":"));
        bufferedWriter.newLine();
        if (field.getType().getSimpleName().equals("int")) {
            bufferedWriter.write(("        type: " + "integer"));
            bufferedWriter.newLine();
        } else if (field.getType().getSimpleName().equals("BigDecimal")) {
            bufferedWriter.write(("        type: " + "string"));
            bufferedWriter.newLine();
        } else {
            String s = field.getType().getSimpleName().toLowerCase();
            bufferedWriter.write(("        type: " + s));
            bufferedWriter.newLine();
        }

    }

    /**
     * 引用类型
     *
     * @param field
     * @throws Exception
     */
    public void writeObjectFiled(Field field) throws Exception {


        bufferedWriter.write(("      " + field.getName() + ":"));
        bufferedWriter.newLine();
        String s = field.getType().getSimpleName();
        bufferedWriter.write(("        $ref: \"../model/" + s + ".yaml#/definitions/" + field.getName() + "\""));
        bufferedWriter.newLine();
    }

    /**
     * Date类型
     *
     * @param field
     * @throws Exception
     */
    public void writeDateFiled(Field field) throws Exception {
        bufferedWriter.write(("      " + field.getName() + ":"));
        bufferedWriter.newLine();

        bufferedWriter.write("        type: string");
        bufferedWriter.newLine();

        bufferedWriter.write("        format: date-time");
        bufferedWriter.newLine();
    }

    /**
     * List类型，仅支持四中泛型
     *
     * @param field
     * @throws Exception
     */
    public void writeListFiled(Field field) throws Exception {

        bufferedWriter.write(("      " + field.getName() + ":"));
        bufferedWriter.newLine();

        bufferedWriter.write("        type: array");
        bufferedWriter.newLine();

        bufferedWriter.write("        items:");
        bufferedWriter.newLine();

        Type fieldType = field.getGenericType();
        Type type = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
        String name = type.getTypeName();
        String[] strings = name.split("\\.");
        name = strings[strings.length - 1];
        if (name.equals("String") || name.equals("Integer") || name.equals("Long")) {
            bufferedWriter.write(("          type: " + name.toLowerCase()));
            bufferedWriter.newLine();
        } else {
            String ss = name.substring(0, 1).toLowerCase() + name.substring(1);
            bufferedWriter.write((("          $ref: \"../model/" + name + ".yaml#/definitions/" + ss) + "\""));
            bufferedWriter.newLine();
        }
    }

}
