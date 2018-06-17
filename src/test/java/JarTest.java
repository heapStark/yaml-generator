import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by WZL on 2018/6/14.
 */
public class JarTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JarTest.class);

    @Test
    public void jarTest() throws IOException {
        JarFile jarFile = new JarFile("/home/wzl/.m2/repository/heap-stark/yaml-web/1.0-SNAPSHOT/yaml-web-1.0-SNAPSHOT.jar");
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String entryName = jarEntry.getName();
            if (entryName.endsWith(".class")) {
                LOGGER.info(":{}", entryName);
            }
        }
    }

    @Test
    public void classLoaderTest() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packageName = "heap.stark.yaml.generator";
        String packagePath = packageName.replace(".", File.separator);
        URL url = loader.getResource(packagePath);
        LOGGER.info("url:{}", url);


    }
}
