import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

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
        LOGGER.info("urls:{}",((URLClassLoader)loader).getURLs());
        if (File.separator.equals("\\")){
            Assert.assertTrue(url.getPath().startsWith("/"));
        } else if (url.getProtocol().equals("jar")){
            Assert.assertTrue(url.getPath().startsWith("file:"));
        }else {

        }

    }
}
