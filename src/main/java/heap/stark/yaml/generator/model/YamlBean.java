package heap.stark.yaml.generator.model;


import heap.stark.yaml.generator.config.Config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class YamlBean {
    Class c;
    Config config;
    BufferedWriter bufferedWriter;
    Set<Class> newModelClass;


    public YamlBean(Class c, Config config) throws IOException {
        this.c = c;
        this.config = config;
        new File(config.getResultPath() + File.separator + "model").mkdirs();
        File file = new File(config.getResultPath() + File.separator + "model" + File.separator + c.getSimpleName() + ".yaml");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);
        newModelClass = new HashSet<>();
    }

    public Set<Class> genYaml() throws Exception {

        //写入默认头
        writeDef(bufferedWriter, c);

        //根据变量写入参数
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            try {
                writeFiled(field);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        bufferedWriter.flush();
        bufferedWriter.close();

        return newModelClass;

    }

    /**
     * 公共头
     *
     * @param bufferedWriter
     * @param c
     * @throws Exception
     */
    public void writeDef(BufferedWriter bufferedWriter, Class c) throws Exception {
        writeNewLine("swagger: \"2.0\"");

        writeNewLine("definitions:");

        bufferedWriter.write("  ");
        String name = c.getSimpleName();
        bufferedWriter.write(toLow(name));
        writeNewLine(":");
        writeNewLine("    x-jcloud-module: " + config.getModuleName());
        writeNewLine(("    title: " + c.getSimpleName()));
        writeNewLine("    type: object");
        writeNewLine("    properties:");

    }

    /**
     * 当前仅支持 List String Integer Integer Date 以及包内引用
     *
     * @param field
     * @throws Exception
     */
    public void writeFiled(Field field) throws Exception {
        Class fieldClass = field.getType();
        if (fieldClass.equals(String.class)
                || fieldClass.isPrimitive()
                || Objects.equals(fieldClass.getSuperclass(), Number.class)
                ) {
            writeBasicFiled(field);
        } else if (field.getType().getSimpleName().equals("List")) {
            writeListFiled(field);
        } else if (field.getType().getSimpleName().equals("Date")) {
            writeDateFiled(field);
        } else if (config.getModelClassSet().contains(field.getType())) {
            writeObjectFiled(field);
        } else {
            writeObjectFiled(field);
            newModelClass.add(field.getType());
        }
    }

    /**
     * 基本类型
     *
     * @param field
     * @throws Exception
     */
    public void writeBasicFiled(Field field) throws Exception {
        writeNewLine(("      " + field.getName() + ":"));
        if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
            writeNewLine(("        type: " + "integer"));
        } else if (Objects.equals(String.class, field.getType())) {
            writeNewLine(("        type: " + "string"));
        } else if (field.getType().isPrimitive()) {
            writeNewLine(("        type: " + field.getType().getSimpleName()));
        } else {
            writeNewLine(("        type: number"));
        }

    }

    /**
     * 引用类型
     *
     * @param field
     * @throws Exception
     */
    public void writeObjectFiled(Field field) throws Exception {


        writeNewLine(("      " + field.getName() + ":"));
        String s = field.getType().getSimpleName();
        writeNewLine(("        $ref: \"../model/" + s + ".yaml#/definitions/" + field.getName() + "\""));
    }

    /**
     * Date类型
     *
     * @param field
     * @throws Exception
     */
    public void writeDateFiled(Field field) throws Exception {
        writeNewLine(("      " + field.getName() + ":"));

        writeNewLine("        type: string");

        writeNewLine("        format: date-time");
    }

    /**
     * List类型，仅支持四中泛型
     *
     * @param field
     * @throws Exception
     */
    public void writeListFiled(Field field) throws Exception {

        writeNewLine(("      " + field.getName() + ":"));

        writeNewLine("        type: array");

        writeNewLine("        items:");

        Type fieldType = field.getGenericType();
        Type type = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
        String name = type.getTypeName();
        String[] strings = name.split("\\.");
        name = strings[strings.length - 1];
        if (name.equals("String") || name.equals("Integer") || name.equals("Long")) {
            writeNewLine(("          type: " + toLow(name)));
        } else {
            String ss = toLow(name);
            writeNewLine((("          $ref: \"../model/" + name + ".yaml#/definitions/" + ss) + "\""));
        }
    }

    private void writeNewLine(String message) throws Exception {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
    }

    private String toLow(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

}
