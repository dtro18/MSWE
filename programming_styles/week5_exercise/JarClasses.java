import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classNames;
    }
    public static Class<?> getClassFromString(URLClassLoader classLoader, String className) {
        try {
            return classLoader.loadClass(className);
        } catch (Throwable t) {
            System.err.println("Skipping class: " + className + " due to " + t.getClass().getSimpleName());
            return null;
        }
    }

    public static Method[] getMethodsOfClass(Class classPtr) {
        try {
            return classPtr.getDeclaredMethods();
        } catch (Throwable t) {
            // Catching Throwable to handle all issues, including linkage errors
            System.err.println("Skipping class2: " + classPtr + " due to " + t.getClass().getSimpleName());
            return new Method[0];
        }
    }

    public static void main(String[] args) {        
        Set<String> classSet = getClassNames(args[0]);
        List<String> classNames = new ArrayList<>(classSet);
        Collections.sort(classNames);
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { new File(args[0]).toURI().toURL() });) {
            for (String className : classNames) {
                try {
                    int publicCount = 0;
                    int privateCount = 0;
                    int protectedCount = 0;
                    int staticCount = 0;
                    int fieldCount = 0;

                    Class<?> clazz = getClassFromString(classLoader, className);
                    
                    if (clazz != null) {
                        fieldCount = clazz.getDeclaredFields().length;
                        for (Method method : getMethodsOfClass(clazz)) {
                            int modifiersInt = method.getModifiers();
                            String[] modifierList = null;
                            try {
                                String modifierStr = Modifier.toString(modifiersInt);
                                modifierList = modifierStr.split(" ");
                            } catch (Exception e) {
                                System.out.println("Method doesn't exist");
                            }
                            for (String modName : modifierList) {
                                if (modName.equals("public")) {
                                    publicCount += 1;
                                } else if (modName.equals("private")) {
                                    privateCount += 1;
                                } else if (modName.equals("protected")) {
                                    protectedCount += 1;
                                } else if (modName.equals("static")) {
                                    staticCount += 1;
                                }
                            }
                        }
                    }
                    
                    System.out.println("----------" + className + "----------");
                    System.out.println("  Public methods: " + publicCount);
                    System.out.println("  Private methods: " + privateCount);
                    System.out.println("  Protected methods: " + protectedCount);
                    System.out.println("  Static methods: " + staticCount);
                    System.out.println("  Fields: " + fieldCount);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Couldn't find class");
                }
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    
    }
}
