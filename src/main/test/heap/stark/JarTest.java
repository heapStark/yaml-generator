package heap.stark;

import org.junit.Test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by WZL on 2018/6/14.
 */
public class JarTest {

    @Test
    public void t() throws IOException {
        JarFile jarFile = new JarFile("C:\\Users\\WZL\\.m2\\repository\\heap-stark\\yaml-web\\1.0-SNAPSHOT\\yaml-web-1.0-SNAPSHOT.jar");
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String entryName = jarEntry.getName();
            if (entryName.endsWith(".class")) {
            }
        }
    }
}
