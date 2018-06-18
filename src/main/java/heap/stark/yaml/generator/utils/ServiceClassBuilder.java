package heap.stark.yaml.generator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 */
public class ServiceClassBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceClassBuilder.class);

    private final Set<Class> Controller_CLASS_SET = new HashSet<>();
    private final String S = File.separator;

    /**
     * 获取某包下所有类
     *
     * @param packageName 包名
     * @return 类的完整名称
     */
    public Set<Class> getClassByPackageName(String packageName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        LOGGER.info("ClassLoader :{}", loader);
        String packagePath = packageName.replace(".", S);
        URL url = loader.getResource(packagePath);
        LOGGER.info("ClassLoader resource url :{}", url);
        if (url != null) {
            String type = url.getProtocol();
            LOGGER.info("url type :{}", type);
            if (type.equals("file")) {
                getClassNameByFile(url.getPath());
            } else if (type.equals("jar")) {
                getClassNameByJar(url.getPath());
            }
        }
        LOGGER.info("urls:{}", ((URLClassLoader) loader).getURLs());
        getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath);


        return Controller_CLASS_SET;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath 文件路径
     * @return 类的完整名称
     */
    private void getClassNameByFile(String filePath) {
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                getClassNameByFile(childFile.getPath());
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf(S + "classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace(S, ".");
                    try {
                        Class c = Class.forName(childFilePath);
                        if (c.isAnnotationPresent(RestController.class)) {
                            Controller_CLASS_SET.add(Class.forName(childFilePath));
                        }
                        Controller_CLASS_SET.add(Class.forName(childFilePath));

                    } catch (ClassNotFoundException e) {
                        LOGGER.error("exception when scan package", e);
                    }
                }
            }
        }

    }

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath jar文件路径
     * @return 类的完整名称
     */
    private void getClassNameByJar(String jarPath) {
        LOGGER.info("------------------------------japath:{}",jarPath);
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(S.equals("\\") ? 1 : 0).replace("/", S);
        if (jarFilePath.startsWith("file")) {
            jarFilePath = jarFilePath.substring(5);
        }
        String packagePath = jarInfo[1].substring(1).replace("\\", "/");
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    LOGGER.info("entryName----------------------:{}",entryName);
                    if (entryName.startsWith(packagePath)) {
                        entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                        try {
                            Class c = Class.forName(entryName);
                            if (c.isAnnotationPresent(RestController.class)){
                                Controller_CLASS_SET.add(Class.forName(entryName));
                            }
                            Controller_CLASS_SET.add(Class.forName(entryName));
                        } catch (ClassNotFoundException e) {
                            LOGGER.error("exception when scan package", e);
                        }
                    }

                }
            }
        } catch (IOException e) {
            LOGGER.error("error to get jarFile, jarFilePath:{}", jarFilePath, e);
        }
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     *
     * @param urls        URL集合
     * @param packagePath 包路径
     * @return 类的完整名称
     */
    private void getClassNameByJars(URL[] urls, String packagePath) {
        LOGGER.info("-----------:{}",urls);
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                String urlPath = url.getPath();
                // 不必搜索classes文件夹
                if (urlPath.endsWith("classes" + S)) {
                    continue;
                }
                String jarPath = urlPath + "!" + S + packagePath;
                getClassNameByJar(jarPath);
            }
        }
    }
}
