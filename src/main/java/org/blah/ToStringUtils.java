package org.blah;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by adam on 31.01.18.
 */
public class ToStringUtils {

    private Set<Object> printedObjects = new HashSet<>();

    private Map<Class, List<Object>> idMap = new HashMap<>();

    private boolean hasToString(Class clazz) {
        boolean hasToString = false;

        try {
            Class otherClass = clazz.getMethod("toString").getDeclaringClass();
            hasToString = otherClass.getCanonicalName().equals(clazz.getCanonicalName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return hasToString;
    }

    public String toString(Object object) {
        printedObjects = new HashSet<>();
        String result = toString1(object);
        return result;
    }

    private String toString1(Object object) {

        //todo: if class is an array
        //todo: if class is anonymous

        if (object == null) {
            return "null";
        }

        printedObjects.add(object);

        Class clazz = object.getClass();

        if (clazz.isArray()) {
            return arrayToString(object, clazz);
        }

        if (hasToString(clazz))
            return object.toString();

        ToStringBuilder builder = new ToStringBuilder().setClass(clazz.getSimpleName());

        for (Field f: object.getClass().getDeclaredFields()) {

            if (f.getName().startsWith("this$"))
                continue;

            f.setAccessible(true);

            String name = f.getName();

            try {
                Object value = f.get(object);

                Class fieldClass = f.getType();
                boolean hasToString = hasToString(fieldClass);

                if (hasToString) {
                    builder.addKeyValue(name, value.toString());
                } else if (printedObjects.contains(value)) {
                    builder.addKeyValue(name, fieldClass.getSimpleName() + "(Cyclic reference)");
                } else if (isArray(fieldClass)) {
                    String res = arrayToString(value, fieldClass);
                    builder.addKeyValue(name, res);
                } else {
                    printedObjects.add(object);
                    builder.addKeyValue(name, toString1(value));

                }

            } catch (IllegalAccessException e) {
                builder.addKeyValue(f.getName(),"=(Can not access value)");
            }

        }

        return builder.build();
    }

    private String arrayToString(Object value, Class claszz) {
        int len = Array.getLength(value);
        String res = claszz.getComponentType().getSimpleName() + "[";
        for (int i = 0; i < len; i ++) {
            Object element = Array.get(value, i);

            if (printedObjects.contains(element)) {
                res += element.getClass().getSimpleName() + "(Cyclic reference),";
            } else {
                printedObjects.add(element);
                res += toString1(element) + ",";
            }

        }
        res += "]";
        return res;
    }

    private boolean isArray(Class clazz) {
        return clazz != null && clazz.isArray();
    }

    private void putOnMap() {
        
    }


}
