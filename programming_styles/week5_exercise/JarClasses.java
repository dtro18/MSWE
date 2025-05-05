import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.annotation.Annotation;

import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Method;
import java.io.File;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class JarClasses {
    // Takes a jar file as command line argument and prints out a listing of all the classes inside
    // Also print number of declared public, private, protected static methods, number of declared fields for each
    public static Set<String> getClassNames(String givenFile) {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                    .replace("/", ".")
                    .replace(".class", "");
                    classNames.add(className);
                }
            }
        System.out.println(classNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classNames;
    }

    public static Method[] getMethodsOfClass(String jarPath, String className) {
        try (URLClassLoader classLoader = new URLClassLoader(
                new URL[] { new File(jarPath).toURI().toURL() })) {
            
            Class<?> clazz = classLoader.loadClass(className);
            return clazz.getDeclaredMethods();
            
        } catch (Throwable t) {
            // Catching Throwable to handle all issues, including linkage errors
            System.err.println("Skipping class: " + className + " due to " + t.getClass().getSimpleName());
            return new Method[0];
        }
    }

    public static void main(String[] args) {
        File file = new File("output.txt");
        try {
            PrintStream printStream = new PrintStream(file);
            System.setOut(printStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Redirect standard output to the file
        
        Set<String> classNames = getClassNames(args[0]);
        System.out.println(classNames);
        for (String className : classNames) {
            System.out.println(className);
            System.out.println("\n");
            for (Method method : getMethodsOfClass(args[0], className)) {
                
                try {
                    System.out.println(method.toString());
                } catch (Exception e) {
                    System.out.println("Method doesn't exist");
                }
                
            }
        }

    
    }
}
