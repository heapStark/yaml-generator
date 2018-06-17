import org.junit.Assert;
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
    public void jarTest()  {

    }

    @Test
    public void classLoaderTest() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packageName = "heap.stark.yaml.generator";
        String packagePath = packageName.replace(".", File.separator);
        URL url = loader.getResource(packagePath);
        if (File.separator.equals("\\")){
            Assert.assertTrue(url.getPath().startsWith("/"));
        } else {
            Assert.assertTrue(url.getPath().startsWith("file:"));
        }

    }
}
