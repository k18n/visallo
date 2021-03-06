package org.visallo.core.util;

import org.visallo.core.exception.VisalloException;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassUtil {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> forName(String className) {
        try {
            return (Class<? extends T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new VisalloException("Could not load class " + className, e);
        }
    }

    public static Method findMethod(Class clazz, String methodName, Class[] parameters) {
        while (clazz != null) {
            try {
                return getDeclaredMethod(clazz, methodName, parameters);
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    public static void logClasspath(ClassLoader classLoader) {
        logClasspath(classLoader, System.out);
    }

    public static void logClasspath(ClassLoader classLoader, PrintStream printStream) {
        outputClasspath(classLoader, printStream);
    }

    public static void logClasspath(ClassLoader classLoader, VisalloLogger visalloLogger) {
        outputClasspath(classLoader, visalloLogger);
    }

    private static void outputClasspath(ClassLoader classLoader, Object outputObject) {
        StringBuilder sb = new StringBuilder();

        if (classLoader instanceof URLClassLoader) {
            sb.append(classLoader.getClass().getName());
            for (URL url : ((URLClassLoader) classLoader).getURLs()) {
                sb.append(LINE_SEPARATOR).append(url.toString());
            }
        } else {
            sb.append("unable to enumerate entries for ").append(classLoader.getClass().getName());
        }

        if (outputObject instanceof PrintStream) {
            ((PrintStream) outputObject).println(sb.toString());
        } else if (outputObject instanceof VisalloLogger) {
            ((VisalloLogger) outputObject).debug(sb.toString());
        } else {
            throw new VisalloException("unexpected outputObject");
        }

        ClassLoader parentClassLoader = classLoader.getParent();
        if (parentClassLoader != null) {
            outputClasspath(parentClassLoader, outputObject);
        }
    }

    @SuppressWarnings("unchecked")
    private static Method getDeclaredMethod(Class clazz, String methodName, Class[] parameters) throws NoSuchMethodException {
        return clazz.getDeclaredMethod(methodName, parameters);
    }
}
