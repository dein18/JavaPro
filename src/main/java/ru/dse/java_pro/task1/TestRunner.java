package ru.dse.java_pro.task1;

import ru.dse.java_pro.task1.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestRunner {
    public static void runTests(Class<?> testClass) throws Exception {
        Method beforeSuite = null;
        Method afterSuite = null;
        List<Method> beforeTests = new ArrayList<>();
        List<Method> afterTests = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuite != null) throw new RuntimeException("Only one @BeforeSuite allowed");
                if (!Modifier.isStatic(method.getModifiers())) throw new RuntimeException("@BeforeSuite must be static");
                beforeSuite = method;
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuite != null) throw new RuntimeException("Only one @AfterSuite allowed");
                if (!Modifier.isStatic(method.getModifiers())) throw new RuntimeException("@AfterSuite must be static");
                afterSuite = method;
            } else if (method.isAnnotationPresent(BeforeTest.class)) {
                beforeTests.add(method);
            } else if (method.isAnnotationPresent(AfterTest.class)) {
                afterTests.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }

        Object instance = testClass.getConstructor().newInstance();

        if (beforeSuite != null) beforeSuite.invoke(null);

        testMethods.sort(Comparator.comparingInt(m -> -m.getAnnotation(Test.class).priority()));

        for (Method test : testMethods) {
            for (Method bt : beforeTests) bt.invoke(instance);

            CsvSource csv = test.getAnnotation(CsvSource.class);
            if (csv != null) {
                Object[] args = parseCsvArgs(csv.value(), test.getParameterTypes());
                test.invoke(instance, args);
            } else {
                test.invoke(instance);
            }

            for (Method at : afterTests) at.invoke(instance);
        }

        if (afterSuite != null) afterSuite.invoke(null);
    }

    private static Object[] parseCsvArgs(String csv, Class<?>[] types) {
        String[] parts = csv.split("\\s*,\\s*");
        if (parts.length != types.length) throw new RuntimeException("CSV argument count mismatch");
        Object[] args = new Object[parts.length];

        for (int i = 0; i < types.length; i++) {
            args[i] = convert(parts[i], types[i]);
        }
        return args;
    }

    private static Object convert(String value, Class<?> type) {
        if (type == int.class || type == Integer.class) return Integer.parseInt(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        if (type == String.class) return value;
        if (type == double.class || type == Double.class) return Double.parseDouble(value);
        throw new RuntimeException("Unsupported parameter type: " + type.getName());
    }
}