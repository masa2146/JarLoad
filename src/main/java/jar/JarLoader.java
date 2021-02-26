package jar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

/**
 * @author fatih
 */
public class JarLoader {

    public static HashMap<String, URLClassLoader> classLoaderMap = new HashMap<>();

    /**
     * Load class of the jar in plugins folder.
     *
     * @param jarFolder   common jar folder
     * @param mainPackage common main package
     * @param tClass      common class of all plugins
     * @return Loaded class list.
     * @throws MalformedURLException MalformedURLException
     */
    public static List<Class<?>> load(String jarFolder, String mainPackage, Class<?> tClass) throws IOException {
        List<Class<?>> loadedClass = new ArrayList<>();
        // List all jar files in ./plugins
        File[] pluginFiles = new File(jarFolder).listFiles((f, n) -> n.endsWith(".jar"));
        assert pluginFiles != null;
        List<URI> paths = Arrays.stream(pluginFiles).map(File::toURI).collect(Collectors.toList());
        for (URI path : paths) {
            URLClassLoader masterJar = new URLClassLoader(
                    new URL[]{path.toURL()},
                    JarLoader.class.getClassLoader()
            );
            System.out.println("masterJar.getName(): " + Path.of(path).getFileName().toString());
            if (!classLoaderMap.containsKey(Path.of(path).getFileName().toString())) {
                classLoaderMap.put(Path.of(path).getFileName().toString(), masterJar);
                System.out.println(Arrays.toString(masterJar.getURLs()));
                List<Class<?>> classesInPackage = getClassesInPackage(masterJar, mainPackage, tClass);
                if (classesInPackage.size() == 1) {
//                Class<?> aClass = Class.forName(mainPackage, true, child);
                    loadedClass.add(classesInPackage.get(0));
                } else if (classesInPackage.size() == 0) {
                    System.err.println("The common " + tClass.getName() + " class not found in " + mainPackage);
                } else {
                    System.err.println("There are so much " + tClass.getName() + " classes " + mainPackage);
                }
            }

        }
        return loadedClass;
    }

    /**
     * Found classes in package by class.
     *
     * @param masterJar   this is parent jar
     * @param packageName To be search class in this package name
     * @param tClass      Common class. This class
     * @return found classes
     */
    public static List<Class<?>> getClassesInPackage(URLClassLoader masterJar, String packageName, Class<?> tClass) {
        String classpathEntry = masterJar.getURLs()[0].getFile();
        String path = packageName.replaceAll("\\.", File.separator);
        List<Class<?>> classes = new ArrayList<>();

        String name;
        File jar = new File(classpathEntry);
        try {
            JarInputStream is = new JarInputStream(new FileInputStream(jar));
            JarEntry entry;
            while ((entry = is.getNextJarEntry()) != null) {
                name = entry.getName();
                if (name.endsWith(".class")) {
                    if (name.contains(path) && name.endsWith(".class")) {
                        String classPath = name.substring(0, entry.getName().length() - 6);
                        classPath = classPath.replaceAll("[|/]", ".");
                        Class<?> aClass = Class.forName(classPath, true, masterJar);
                        if (Arrays.asList(aClass.getInterfaces()).contains(tClass)) {
                            classes.add(aClass);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            // Silence is
            ex.printStackTrace();
        }
        return classes;
    }

    public static void downloadJar(URL url, String saveLocation) throws IOException, URISyntaxException {
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        String fileName = url.toString().substring(url.toString().lastIndexOf('/') + 1);
        FileOutputStream fos = new FileOutputStream(saveLocation + "/" + fileName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
