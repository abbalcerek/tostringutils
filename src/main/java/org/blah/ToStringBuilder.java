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

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Utility class for building string representation of a class
 *
 * Created by adam on 31.01.18.
 */
class ToStringBuilder {

    private String fieldSep = ";";
    private String valueSep = "=";

    private List<String> tokens = new ArrayList<>();
    private String clazz = null;

    /**
     * Adds fields as key value pairs
     * @param key name of field
     * @param value value of field
     * @return builder itself
     */
    public ToStringBuilder addKeyValue(String key, String value) {
        tokens.add(key);
        tokens.add(valueSep);
        tokens.add(value);
        tokens.add(fieldSep);
        return this;
    }

    /**
     * Adds name of the class to object
     * @param clazz class of the object
     * @return builder itself
     */
    public ToStringBuilder setClass(String clazz) {
        this.clazz = clazz;
        return this;
    }

    /**
     * Builds final string from added components and clears builder state.
     * @return
     */
    public String build() {
        if (!tokens.isEmpty()) {
            tokens.remove(tokens.size() - 1);
        }
        tokens.add(0, "{");
        tokens.add("}");
        StringBuilder b = new StringBuilder();

        if (clazz != null) {
            b.append(clazz);
        }

        clazz = null;

        for (String f: tokens)
            b.append(f);

        tokens.clear();

        return b.toString();
    }

}
