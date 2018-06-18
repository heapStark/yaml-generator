package heap.stark.yaml.generator.service;

import heap.stark.yaml.generator.config.Config;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class YamlServiceBean {
    private Class c;
    private BufferedWriter bufferedWriter;
    private List<Class> classList;
    private Config config;
    private List<PathVariable> pathVariableList;

    public YamlServiceBean(Class c, Config config) throws Exception {
        this.c = c;
        this.config = config;
        new File(config.getResultPath() + File.separator + "service").mkdirs();
        File file = new File(config.getResultPath() + File.separator + "service" + File.separator + c.getSimpleName() + ".yaml");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);
        classList = new ArrayList<>();
        pathVariableList = new ArrayList<>();
    }

    public void genYaml() throws Exception {

        writeDef();
        //根据方法写入信息
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            writeMethod(method);
        }
        writePathParams();
        bufferedWriter.flush();
        bufferedWriter.close();
        config.getModelClassSet().addAll(classList);

    }

    /**
     * 写入请求头
     *
     * @throws Exception
     */
    public void writeDef() throws Exception {
        bufferedWriter.write("swagger: \"2.0\"");
        bufferedWriter.newLine();
        writeNewLine("info:");
        writeNewLine("  title: JCLOUD " + config.getModuleName() + " API");
        writeNewLine("  description: API related to " + config.getModuleName());
        RequestMapping basePath = (RequestMapping) c.getAnnotation(RequestMapping.class);
        writeNewLine("basePath: " + basePath.value()[0]);
        writeNewLine("paths:");
    }

    /**
     * @param
     * @param method
     * @throws Exception
     */
    public void writeMethod(Method method) throws Exception {
        RequestMapping requestMapping = (RequestMapping) method.getAnnotation(RequestMapping.class);
        String message = requestMapping.value()[0];
        writeNewLine("  \"" + message + "\":");
        RequestMethod requestMethod = requestMapping.method()[0];
        if (requestMethod.equals(RequestMethod.GET)) {
            writeNewLine("    get:");
        } else if (requestMethod.equals(RequestMethod.POST)){
            writeNewLine("    post:");
        } else if (requestMethod.equals(RequestMethod.DELETE)){
            writeNewLine("    delete:");
        }else if (requestMethod.equals(RequestMethod.PUT)){
            writeNewLine("    put:");
        }else if (requestMethod.equals(RequestMethod.PATCH)){
            writeNewLine("    patch:");
        }
        //todo operationId命名规则
        String[] strings = message.split(":");
        String action = null;
        if (strings != null && strings.length > 1 && strings[strings.length - 1] != null) {
            action = strings[strings.length - 1];
        }
        message = "      operationId: " + message.split("/")[1];
        if (action != null) {
            message = message + action;
        }
        writeNewLine(message);
        //写入请求参数
        Parameter[] parameters = method.getParameters();
        writeParams(parameters);


        writeNewLine("      responses:");
        writeNewLine("        200:");
        writeNewLine("          description: OK");
        writeNewLine("          schema:");
        writeNewLine("            properties:");
        writeNewLine("              result:");
        Type result = method.getGenericReturnType();
        Type type = ((ParameterizedType) result).getActualTypeArguments()[0];

        //返回结果的泛型参数
        String name = type.getTypeName();

        Class c = Class.forName(name);
        classList.add(c);

        String nameUp = c.getSimpleName();
        name = nameUp.substring(0, 1).toLowerCase() + nameUp.substring(1);

        writeNewLine("                $ref: \"../model/" + nameUp + ".yaml#/definitions/" + name + "\"");
        writeNewLine("              requestId:");
        writeNewLine("                type: string");
        writeNewLine("        404:");
        writeNewLine("          description: NOT_FOUND");
        pathVariableList.addAll(getPathParams(parameters));


    }

    public List<PathVariable> getPathParams(Parameter[] parameters) throws Exception {
        if (parameters == null || parameters.length == 0) {
            return null;
        } else {
            List<PathVariable> pathVariableList = new ArrayList<PathVariable>();

            for (Parameter parameter : parameters) {
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);

                if (pathVariable != null) {
                    pathVariableList.add(pathVariable);
                }
            }
            return pathVariableList;
        }
    }

    public void writePathParams() throws Exception {
        if (pathVariableList == null || pathVariableList.size() == 0) {
            return;
        }
        Set<String> nameSet = new HashSet<String>();
        writeNewLine("parameters:");
        for (PathVariable pathVariable : pathVariableList) {

            String name = pathVariable.value();
            if (nameSet.contains(name)) {
                break;
            }
            writeNewLine("  " + name + ":");
            writeNewLine("    name: " + name);
            writeNewLine("    in: path");
            writeNewLine("    required: true");
            writeNewLine("    type: string");
            nameSet.add(name);
        }
    }

    public void writeParams(Parameter[] parameters) throws Exception {
        if (parameters == null || parameters.length == 0) {
            return;
        } else {

            for (Parameter parameter : parameters) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
                if (requestParam != null || requestBody != null) {
                    writeNewLine("      parameters:");
                    break;
                }
            }
            for (Parameter parameter : parameters) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
                if (requestParam != null || requestBody != null) {
                    writeParam(parameter);
                }
            }
        }
    }

    public void writeParam(Parameter parameter) throws Exception {
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        if (requestParam != null) {
            String name = requestParam.value();
            boolean required = requestParam.required();
            if (parameter.getType().getSimpleName().equals("Map")) {
                writeNewLine("        - name: filters");
                writeNewLine("          in: query");
                writeNewLine("          type: array");
                writeNewLine("          items:");
                writeNewLine("            $ref: \"../../common/model/Filter.yaml#/definitions/filter\"");
            } else {
                writeNewLine("        - name: " + name);
                writeNewLine("          in: query");
                writeNewLine("          type: " + toLow(parameter.getType().getSimpleName()));
                writeNewLine("          required: " + required);
            }
        }
        RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
        if (requestBody != null) {
            classList.add(Class.forName(parameter.getType().getName()));
            String name = parameter.getType().getSimpleName();
            String nameLow = toLow(name);
            writeNewLine("        - name: " + nameLow);
            writeNewLine("          in: body");
            writeNewLine("          schema:");
            writeNewLine("            $ref: " + "\"../model/" + name + ".yaml#/definitions/" + nameLow + "\"");
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
