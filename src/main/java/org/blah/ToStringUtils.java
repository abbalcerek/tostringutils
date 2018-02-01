/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.blah;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 *
 *
 * Class containing methods for recursive printing Java objects.
 *
 * Created by adam on 31.01.18.
 */
public class ToStringUtils {

    //todo: add support for collection in with objects with no toStringMethod
    //todo: if class is anonymous

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

    /**
     * Returns recursive string representation of an object.
     *
     * @param object object for witch we want to produce string representation
     * @return string representation of an object
     */
    public String toString(Object object) {
        printedObjects = new HashSet<>();
        String result = toString1(object);
        return result;
    }

    private String toString1(Object object) {

        if (object == null) {
            return "null";
        }

        Class clazz = object.getClass();
        int id = objectId(object, clazz);

        if (clazz.isArray()) {
            return arrayToString(object, clazz);
        }

        if (hasToString(clazz))
            return object.toString();

        ToStringBuilder builder = new ToStringBuilder().setClass(clazz.getSimpleName() + "(" + id +")");

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
                    builder.addKeyValue(name, fieldClass.getSimpleName() + "(" + objectId(value, fieldClass) + ")");
                } else if (isArray(fieldClass)) {
                    String res = arrayToString(value, fieldClass);
                    builder.addKeyValue(name, res);
                } else {
                    objectId(value, fieldClass);
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
                res += element.getClass().getSimpleName() + "(" + objectId(element, element.getClass()) + "),";
            } else {
                if (element != null)
                    objectId(element, element.getClass());
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

    private void addObject(Object value, Class clazz) {
        printedObjects.add(value);
        if (idMap.containsKey(clazz)) {
            idMap.get(clazz).add(value);
        } else {
            List<Object> values = new ArrayList<>();
            values.add(value);
            idMap.put(clazz, values);
        }
    }

    private Integer objectId(Object value, Class clazz) {
        if (clazz.isArray())
            return -1;
        if (value == null)
            return -2;
        if (!printedObjects.contains(value)) {
            addObject(value, clazz);
        }
        return idMap.get(clazz).indexOf(value);
    }


}
