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

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by adam on 31.01.18.
 */
public class ToStringUtilsTest {


    //todo: object test decide what on objects
    @Test
    public void objectClassTest() {
        String string = new ToStringUtils().toString(new Object());
        System.out.println(string);
    }


    //todo: anonymous test
    @Test
    public void objectClassAnnonyousWithFiledTest() {
        Runnable o = new Runnable() {
            @Override
            public void run() {

            }

            private String a = "fieldValue";
        };

        Thread t = new Thread() {

        };

        System.out.println("Suer classes: " + Arrays.toString(o.getClass().getInterfaces()));
        String string = new ToStringUtils().toString(o);
        String string1 = new ToStringUtils().toString(t);
        System.out.println(t.getClass().getSuperclass());
        System.out.println(t.getClass().getInterfaces());

        System.out.println(string1);
        System.out.println(Arrays.toString(o.getClass().getClasses()));
        System.out.println(string);
    }

    //todo: array test
    @Test
    public void objectArrayTest() {
        String string = new ToStringUtils().toString(new Object[]{new Object(), "A"});
        System.out.println(string);
    }

    @Test
    public void objectArray1Test() {
        String string = new ToStringUtils().toString(new Object[]{new Object(), "A", new A()});
        System.out.println(string);
    }

    interface D {}

    class E implements D {}
    class F implements D {}

    @Test
    public void objectArray3Test() {
        String string = new ToStringUtils().toString(new D[]{new E(), new F()});
        Assert.assertEquals("D[E(0){},F(0){},]", string);
    }


    @Test
    public void stringTest() {
        String string = new ToStringUtils().toString("stringValue");
        System.out.println(string);
        Assert.assertEquals("stringValue", string);
    }


    class A {
        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("A{toString");
            sb.append('}');
            return sb.toString();
        }
    }

    @Test
    public void objectWithToStringTest() {
        A a = new A();
        String string = new ToStringUtils().toString(a);
        System.out.println(string);
        Assert.assertEquals(a.toString(), string);
    }

    class A1 {

        private String a = "fieldValue";

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("A1{");
            sb.append("a='").append(a).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    @Test
    public void objectWithToStringAndFieldTest() {
        String string = new ToStringUtils().toString(new A1());
        System.out.println(string);

        Assert.assertEquals("A1{a='fieldValue'}", string);
    }

    class A2 {
        private A2 a = this;
    }

    @Test
    public void nestedObjectTest() {
        String string = new ToStringUtils().toString(new A2());
        Assert.assertEquals("A2(0){a=A2(0)}", string);
    }

    class A3 {
        private A3 a = this;
        private A3 a1 = this;
    }

    @Test
    public void twoNestedObjectTest() {
        String string = new ToStringUtils().toString(new A3());
        Assert.assertEquals("A3(0){a=A3(0);a1=A3(0)}", string);
    }

    class C {}

    class A4 {
        private C c = new C();
        private C c1 = c;
    }

    @Test
    public void twoSameFieldsTest() {
        String string = new ToStringUtils().toString(new A4());
        System.out.println(string);
    }

    class A5 {
        private A5 a;
    }

    @Test
    public void threeCicleTest() {
        A5 a = new A5();
        A5 a1 = new A5();
        a1.a = a;
        a.a = a1;

        String string = new ToStringUtils().toString(a);
        System.out.println(string);
    }


    public class A6 {

        private C1 c1 = new C1();
        private A6 aa = this;

    }

    public class C1 {
        private String c = "classC1";
    }

    @Test
    public void tempTest() {

        ToStringUtils t = new ToStringUtils();

        String string = t.toString(new A6());
        System.out.println(string);

        string = t.toString(new A6());
        System.out.println(string);
    }

    public class A8 {
        private A8[] a = {this, this};
    }

    @Test
    public void arrayOfCyclesTest() {

        ToStringUtils t = new ToStringUtils();

        String string = t.toString(new A8());
        System.out.println(string);

    }

    public class A9 {
        private A9[] a = {this, this};
        private A2 a1 = new A2();
        private A2 a11 = new A2();
        private A2 a111 = a11;
    }

    @Test
    public void arrayOfCycles2Test() {

        ToStringUtils t = new ToStringUtils();

        String string = t.toString(new A9());
        System.out.println(string);

    }

    private class A10 {
        private A10[] a = {this, null};
        private A2 a1 = new A2();
        private A2 a11 = new A2();
        private A2 a111 = a11;
    }

    @Test
    public void arrayOfCycles3Test() {

        ToStringUtils t = new ToStringUtils();

        String string = t.toString(new A10());
        Assert.assertEquals(
                "A10(0){a=A10[A10(0),null,];a1=A2(0){a=A2(0)};a11=A2(1){a=A2(1)};a111=A2(1)}",
                string
        );

    }
}
