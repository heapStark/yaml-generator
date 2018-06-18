package heap.stark.yaml.generator.service;

import heap.stark.yaml.generator.config.Config;
import org.objectweb.asm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;


public class YamlServiceBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(YamlServiceBean.class);
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
            if (method.getAnnotation(RequestMapping.class) == null) {
                continue;
            }
            List<String> paramNames = getMethodParameterNamesByAsm4(method);
            LOGGER.info("paramNames for method {},:{}", method.getName(), paramNames);
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
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        List<String> pathList = new ArrayList<String>(Arrays.asList(requestMapping.value()));
        pathList.addAll(Arrays.asList(requestMapping.path()));
        String message = "/";
        if (!CollectionUtils.isEmpty(pathList)) {
            message = pathList.get(0);
        } else {
        }
        writeNewLine("  \"" + message + "\":");
        RequestMethod requestMethod = requestMapping.method()[0];
        if (requestMethod.equals(RequestMethod.GET)) {
            writeNewLine("    get:");
        } else if (requestMethod.equals(RequestMethod.POST)) {
            writeNewLine("    post:");
        } else if (requestMethod.equals(RequestMethod.DELETE)) {
            writeNewLine("    delete:");
        } else if (requestMethod.equals(RequestMethod.PUT)) {
            writeNewLine("    put:");
        } else if (requestMethod.equals(RequestMethod.PATCH)) {
            writeNewLine("    patch:");
        } else {
            writeNewLine("    get:");
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
        List<String> parameterNames = getMethodParameterNamesByAsm4(method);
        LOGGER.info("parameters in method {}:{}", method, parameters);
        writeParams(parameters, parameterNames);


        writeNewLine("      responses:");
        writeNewLine("        200:");
        writeNewLine("          description: OK");
        writeNewLine("          schema:");
        writeNewLine("            properties:");
        writeNewLine("              result:");
        java.lang.reflect.Type result = method.getGenericReturnType();
        java.lang.reflect.Type type = ((ParameterizedType) result).getActualTypeArguments()[0];

        //返回结果的泛型参数
        String name = type.getTypeName();

        Class c = Class.forName(name);
        classList.add(c);

        String nameUp = c.getSimpleName();
        name = toLow(nameUp);

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
        if (CollectionUtils.isEmpty(pathVariableList)) {
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

    public void writeParams(Parameter[] parameters, List<String> names) throws Exception {

        if (parameters == null || parameters.length == 0) {
            return;
        } else {
            //todo
            for (Parameter parameter : parameters) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
                if (parameter.getAnnotations() == null || parameter.getAnnotations().length == 0) {
                    writeNewLine("      parameters:");
                    break;
                }
                if (requestParam != null || requestBody != null) {
                    writeNewLine("      parameters:");
                    break;
                }
            }
            Iterator<String> iterator = names.iterator();
            for (Parameter parameter : parameters) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
                {
                    writeParam(parameter, iterator.next());
                }
            }
        }
    }

    public void writeParam(Parameter parameter, String string) throws Exception {
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
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
        } else if (requestBody != null) {
            classList.add(Class.forName(parameter.getType().getName()));
            String name = parameter.getType().getSimpleName();
            String nameLow = toLow(name);
            writeNewLine("        - name: " + nameLow);
            writeNewLine("          in: body");
            writeNewLine("          schema:");
            writeNewLine("            $ref: " + "\"../model/" + name + ".yaml#/definitions/" + nameLow + "\"");
        } else if (parameter.getAnnotations() == null || parameter.getAnnotations().length == 0) {
            writeNewLine("        - name: " + string);
            writeNewLine("          in: query");
            writeNewLine("          type: " + toLow(parameter.getType().getSimpleName()));
            writeNewLine("          required: " + false);
        }
    }

    private void writeNewLine(String message) throws Exception {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
    }

    private String toLow(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    /**
     * 获取方法参数名,兼容@RequestParam value=null的情况
     *
     * @param method
     * @return
     */
    private List<String> getMethodParameterNamesByAsm4(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return null;
        }
        final Type[] types = new Type[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Type.getType(parameterTypes[i]);
        }
        final List<String> parameterNames = new ArrayList<>();

        String className = c.getName();
        int lastDotIndex = className.lastIndexOf(".");
        className = className.substring(lastDotIndex + 1) + ".class";
        InputStream is = c.getResourceAsStream(className);
        try {
            ClassReader classReader = new ClassReader(is);
            classReader.accept(new ClassVisitor(Opcodes.ASM4) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    // 只处理指定的方法
                    Type[] argumentTypes = Type.getArgumentTypes(desc);
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
                        return null;
                    }
                    return new MethodVisitor(Opcodes.ASM4) {
                        @Override
                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                            // 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this
                            if (Modifier.isStatic(method.getModifiers())) {
                                parameterNames.add(name);
                            } else if (index > 0) {
                                parameterNames.add(name);
                            }
                        }
                    };

                }
            }, 0);
        } catch (IOException e) {
            LOGGER.error("get Method ParameterNames ByAsm4 error", e);
        }
        return parameterNames;
    }

}
