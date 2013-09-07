/*
 * This file is part of hyphenType. hyphenType is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. hyphenType is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with hyphenType. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.hyphenType.tests.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

import org.hyphenType.util.DefaultAnnotation;
import org.hyphenType.util.resourcebundles.ConfigurableListResourceBundle;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Aurelio Akira M. Matsui
 */
public class DefaultAnnotationTest {

    // ///////////////////////////////////////////////////////////////////////
    // Very simple annotation (no arrays or other annotations as properties).
    // ///////////////////////////////////////////////////////////////////////

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X1 {
	String a() default "aaa";

	String b() default "bbb";

	String c();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface X1Method {
        String d() default "dd";

        String e() default "ee";

        String f();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface X1Field {
        String g() default "ggg";

        String h() default "hhh";

        String i();
    }

    @X1(a = "xyz", c = "ccc")
    class Z1 {

        @X1Field(i="i1")
        public String f1;
        @X1Field(g="g2", i="i2")
        public String f2;
        
        @X1Method(f = "jj")
        public void x() {
            
        }

        @X1Method(f = "aa")
        public void y() {
            
        }
    }

    @Test
    public void basicDefaultAnnotationFunctions() {
        X1 annotation = DefaultAnnotation.getAnnotation(Z1.class, X1.class);
        assertEquals("xyz", annotation.a());
        assertEquals("bbb", annotation.b());
        DefaultAnnotation.set(annotation, "a", "test");
        assertEquals("test", annotation.a());
    }

    @Test
    public void annotationFromNothing() {
        X1 annotation = DefaultAnnotation.getAnnotationWithDefaults(X1.class);
        assertEquals("aaa", annotation.a());
        assertEquals("bbb", annotation.b());
        DefaultAnnotation.set(annotation, "a", "test");
        assertEquals("test", annotation.a());
        assertNull(annotation.c());
        DefaultAnnotation.set(annotation, "c", "qqq");
        assertEquals("qqq", annotation.c());
    }

    @Test
    public void fillWithResourceBundle() {
        X1 annotation = DefaultAnnotation.getAnnotation(Z1.class, X1.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1.a", "avocado");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1.b", "pineapple");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1.c", "lemon");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1.d", "strawberry");
        /*
         * DefaultAnnotation should tolerate options that are not present in the annotation.
         */

        DefaultAnnotation.fillWithResourceBundle(annotation, new ConfigurableListResourceBundle(map));

        assertEquals("avocado", annotation.a());
        assertEquals("pineapple", annotation.b());
        assertEquals("lemon", annotation.c());
    }

    @Test
    public void fillWithResourceBundleUsingDefault() {
        X1 annotation = DefaultAnnotation.getAnnotationWithDefaults(X1.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1.c", "banana");

        DefaultAnnotation.fillWithResourceBundle(annotation, new ConfigurableListResourceBundle(map));

        assertEquals("aaa", annotation.a());
        assertEquals("bbb", annotation.b());
        assertEquals("banana", annotation.c());
    }

    @Test
    public void getAnnotations() {
        Annotation[] annotations = DefaultAnnotation.getAnnotations(Z1.class);
        assertEquals(1, annotations.length);
        assertEquals(X1.class, annotations[0].annotationType());
        assertEquals("xyz", ((X1) annotations[0]).a());
        assertEquals("bbb", ((X1) annotations[0]).b());
        assertEquals("ccc", ((X1) annotations[0]).c());
    }

    @Test
    public void basicDefaultAnnotationFunctions_Method() throws SecurityException, NoSuchMethodException {
        X1Method annotation = DefaultAnnotation.getAnnotation(Z1.class.getMethod("x"), X1Method.class);
        assertEquals("dd", annotation.d());
        assertEquals("ee", annotation.e());
        DefaultAnnotation.set(annotation, "d", "test");
        assertEquals("test", annotation.d());
    }

    @Test
    public void annotationFromNothing_Method() {
        X1Method annotation = DefaultAnnotation.getAnnotationWithDefaults(X1Method.class);
        assertEquals("dd", annotation.d());
        assertEquals("ee", annotation.e());
        DefaultAnnotation.set(annotation, "d", "test");
        assertEquals("test", annotation.d());
        assertNull(annotation.f());
        DefaultAnnotation.set(annotation, "f", "qqq");
        assertEquals("qqq", annotation.f());
    }

    @Test
    public void fillWithResourceBundle_Method() throws SecurityException, NoSuchMethodException {
        X1Method annotation = DefaultAnnotation.getAnnotation(Z1.class.getMethod("x"), X1Method.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1Method.d", "avocado");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1Method.e", "pineapple");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1Method.f", "lemon");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1Method.g", "strawberry");
        /*
         * DefaultAnnotation should ignore entries in the resource bundles that are not present in the annotation.
         * g is an example of such entry.
         */

        DefaultAnnotation.fillWithResourceBundle(annotation, new ConfigurableListResourceBundle(map));

        assertEquals("avocado", annotation.d());
        assertEquals("pineapple", annotation.e());
        assertEquals("lemon", annotation.f());
    }

    @Test
    public void fillWithResourceBundleUsingDefault_Method() {
        X1Method annotation = DefaultAnnotation.getAnnotationWithDefaults(X1Method.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1Method.f", "banana");

        DefaultAnnotation.fillWithResourceBundle(annotation, new ConfigurableListResourceBundle(map));

        assertEquals("dd", annotation.d());
        assertEquals("ee", annotation.e());
        assertEquals("banana", annotation.f());
    }

    /**
     * Tests if fillWithResourceBundle will find entries in the resource bundle
     * that are specifically attached with the annotated class.
     * 
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void fillWithResourceBundle_ClassBased_Method() throws SecurityException, NoSuchMethodException {
        X1 annotation = DefaultAnnotation.getAnnotation(Z1.class, X1.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$Z1@org.hyphenType.tests.util.DefaultAnnotationTest$X1.a", "AA");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1.a", "QQQ");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1.b", "BB");

        DefaultAnnotation.fillWithResourceBundle(annotation, new ConfigurableListResourceBundle(map));

        assertEquals("AA", annotation.a());
        assertEquals("BB", annotation.b());
        assertEquals("ccc", annotation.c());
    }
    
    /**
     * Tests if fillWithResourceBundle will find entries in the resource bundle
     * that are specifically attached with the annotated method.
     * 
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void fillWithResourceBundle_MethodBased_Method() throws SecurityException, NoSuchMethodException {
        X1Method xAnnotation = DefaultAnnotation.getAnnotation(Z1.class.getMethod("x"), X1Method.class);
        X1Method yAnnotation = DefaultAnnotation.getAnnotation(Z1.class.getMethod("y"), X1Method.class);
        
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$Z1.x@org.hyphenType.tests.util.DefaultAnnotationTest$X1Method.d", "xD");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1Method.e", "xE");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$Z1.y@org.hyphenType.tests.util.DefaultAnnotationTest$X1Method.d", "yD");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$Z1.y@org.hyphenType.tests.util.DefaultAnnotationTest$X1Method.e", "yE");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$Z1.y@org.hyphenType.tests.util.DefaultAnnotationTest$X1Method.f", "yF");
        
        DefaultAnnotation.fillWithResourceBundle(xAnnotation, new ConfigurableListResourceBundle(map));
        DefaultAnnotation.fillWithResourceBundle(yAnnotation, new ConfigurableListResourceBundle(map));
        
        assertEquals("xD", xAnnotation.d());
        assertEquals("xE", xAnnotation.e());
        assertEquals("jj", xAnnotation.f());
        assertEquals("yD", yAnnotation.d());
        assertEquals("yE", yAnnotation.e());
        assertEquals("yF", yAnnotation.f());
    }
    
    /**
     * Tests if fillWithResourceBundle will find entries in the resource bundle
     * that are specifically attached with the annotated field.
     * 
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws NoSuchFieldException 
     */
    @Test
    public void fillWithResourceBundle_FieldBased_Method() throws SecurityException, NoSuchFieldException {
        X1Field f1Annotation = DefaultAnnotation.getAnnotation(Z1.class.getField("f1"), X1Field.class);
        X1Field f2Annotation = DefaultAnnotation.getAnnotation(Z1.class.getField("f2"), X1Field.class);
        
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$Z1.f1@org.hyphenType.tests.util.DefaultAnnotationTest$X1Field.g", "f1g");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X1Field.h", "hhhh");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$Z1.f2@org.hyphenType.tests.util.DefaultAnnotationTest$X1Field.g", "f2g");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$Z1.f2@org.hyphenType.tests.util.DefaultAnnotationTest$X1Field.h", "f2h");
        map.put("org.hyphenType.tests.util.DefaultAnnotationTest$Z1.f2@org.hyphenType.tests.util.DefaultAnnotationTest$X1Field.i", "f2i");
        
        DefaultAnnotation.fillWithResourceBundle(f1Annotation, new ConfigurableListResourceBundle(map));
        DefaultAnnotation.fillWithResourceBundle(f2Annotation, new ConfigurableListResourceBundle(map));
        
        assertEquals("f1g", f1Annotation.g());
        assertEquals("hhhh", f1Annotation.h());
        assertEquals("i1", f1Annotation.i());
        assertEquals("f2g", f2Annotation.g());
        assertEquals("f2h", f2Annotation.h());
        assertEquals("f2i", f2Annotation.i());
    }
    
    @Test
    public void getAnnotations_Method() throws SecurityException, NoSuchMethodException {
        Annotation[] annotations = DefaultAnnotation.getAnnotations(Z1.class.getMethod("x"));
        assertEquals(1, annotations.length);
        assertEquals(X1Method.class, annotations[0].annotationType());
        assertEquals("dd", ((X1Method) annotations[0]).d());
        assertEquals("ee", ((X1Method) annotations[0]).e());
        assertEquals("jj", ((X1Method) annotations[0]).f());
    }

    // ///////////////////////////////////////////////////////////////////////
    // Annotation with array.
    // ///////////////////////////////////////////////////////////////////////

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X2 {
	String[] a() default { "a1", "a2" };

	String[] b();
    }

    @X2(b = "b1")
    class Z2 {
    }

    @Test
    public void testPropertyArray() {
	X2 x2 = DefaultAnnotation.getAnnotation(Z2.class, X2.class);
	assertArrayEquals(new String[] { "a1", "a2" }, x2.a());
	assertArrayEquals(new String[] { "b1" }, x2.b());
    }

    @Test
    public void changePropertyArray() {
	X2 x2 = DefaultAnnotation.getAnnotation(Z2.class, X2.class);
	DefaultAnnotation.set(x2, "a", new String[] { "Venus", "Moon" });
	assertArrayEquals(new String[] { "Venus", "Moon" }, x2.a());
	assertArrayEquals(new String[] { "b1" }, x2.b());
    }

    @Test
    public void propertyArrayFromNothing() {
	X2 x2 = DefaultAnnotation.getAnnotationWithDefaults(X2.class);
	assertArrayEquals(new String[] { "a1", "a2" }, x2.a());
	assertNull(x2.b());
    }

    @Test
    public void fillPropertyArrayFromResourceBundle() {
	X2 x2 = DefaultAnnotation.getAnnotation(Z2.class, X2.class);

	HashMap<String, String> map = new HashMap<String, String>();
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X2.a[0]", "tuna");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X2.a[1]", "shrimp");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X2.b[0]", "starfish");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X2.b[1]", "oister");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X2.b[2]", "cod");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X2.c", "strawberry"); // DefaultAnnotation
	// should tolerate
	// options that are
	// not present in the
	// annotation.

	DefaultAnnotation.fillWithResourceBundle(x2, new ConfigurableListResourceBundle(map));

	assertArrayEquals(new String[] { "tuna", "shrimp" }, x2.a());
	assertArrayEquals(new String[] { "starfish", "oister", "cod" }, x2.b());
    }

    // ///////////////////////////////////////////////////////////////////////
    // Annotation having another annotation as property.
    // ///////////////////////////////////////////////////////////////////////

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X3 {
	X3Sub subA() default @X3Sub(a = "1", b = { "2", "3" });

	X3Sub subB();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X3Sub {
	String a();

	String[] b();
    }

    @X3(subB = @X3Sub(a = "4", b = { "5", "6", "7", "8" }))
    class Z3 {
    }

    @Test
    public void testSubAnnotation() {
	X3 x3 = DefaultAnnotation.getAnnotation(Z3.class, X3.class);
	assertEquals("1", x3.subA().a());
	assertArrayEquals(new String[] { "2", "3" }, x3.subA().b());
	assertEquals("4", x3.subB().a());
	assertArrayEquals(new String[] { "5", "6", "7", "8" }, x3.subB().b());
    }

    @Test
    public void settingSubAnnotation() {
	X3 x3 = DefaultAnnotation.getAnnotation(Z3.class, X3.class);
	X3Sub x3Sub = DefaultAnnotation.getAnnotationWithDefaults(X3Sub.class);
	DefaultAnnotation.set(x3Sub, "a", "test1");
	DefaultAnnotation.set(x3Sub, "b", new String[] { "test2", "test3" });
	DefaultAnnotation.set(x3, "subA", x3Sub);

	assertEquals("test1", x3.subA().a());
	assertArrayEquals(new String[] { "test2", "test3" }, x3.subA().b());
	assertEquals("4", x3.subB().a());
	assertArrayEquals(new String[] { "5", "6", "7", "8" }, x3.subB().b());
    }

    @Test
    public void loadingSubAnnotationFromResourceBundle() {
	X3 x3 = DefaultAnnotation.getAnnotation(Z3.class, X3.class);

	HashMap<String, String> map = new HashMap<String, String>();
	
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X3.subA.a", "w1");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X3.subA.b[0]", "w2");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X3.subA.b[1]", "w3");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X3.subA.b[2]", "w4");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X3.subB.a", "w5");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X3.subB.b[0]", "w6");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X3.subB.b[1]", "w7");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X3.subB.b[2]", "w8");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X3.subB.b[3]", "w9");

	DefaultAnnotation.fillWithResourceBundle(x3, new ConfigurableListResourceBundle(map));

	assertEquals("w1", x3.subA().a());
	assertArrayEquals(new String[] { "w2", "w3", "w4" }, x3.subA().b());
	assertEquals("w5", x3.subB().a());
	assertArrayEquals(new String[] { "w6", "w7", "w8", "w9" }, x3.subB()
		.b());
    }

    // ///////////////////////////////////////////////////////////////////////
    // Annotation having an array of another annotation as property
    // ///////////////////////////////////////////////////////////////////////

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X4 {
	X4Sub[] subA() default { @X4Sub(a = "i", b = { "ii", "iii" }),
		@X4Sub(a = "iv", b = { "v" }) };

	X4Sub subB();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X4Sub {
	String a();

	String[] b();
    }

    @X4(subB = @X4Sub(a = "vi", b = { "vii", "viii", "ix", "x" }))
    class Z4 {
    }

    @Test
    public void testAnnotationWithAnotherAnnotationsArrayAsProperty() {
	X4 x4 = DefaultAnnotation.getAnnotation(Z4.class, X4.class);
	X4Sub[] subArray = x4.subA();
	assertEquals(2, subArray.length);

	assertEquals("i", subArray[0].a());
	assertArrayEquals(new String[] { "ii", "iii" }, subArray[0].b());

	assertEquals("iv", subArray[1].a());
	assertArrayEquals(new String[] { "v" }, subArray[1].b());

	assertEquals("vi", x4.subB().a());
	assertArrayEquals(new String[] { "vii", "viii", "ix", "x" }, x4.subB()
		.b());
    }

    @Test
    public void testSubAnnotationsFromResourceBundles() {

	X4 x4 = DefaultAnnotation.getAnnotation(Z4.class, X4.class);

	HashMap<String, String> map = new HashMap<String, String>();
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subA[0].a", "w1");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subA[0].b[0]", "w2");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subA[0].b[1]", "w3");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subA[0].b[2]", "w4");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subA[1].a", "w5");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subA[1].b[0]", "w6");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subA[1].b[1]", "w7");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subA[1].b[2]", "w8");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subA[1].b[3]", "w9");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subA[1].b[4]", "w10");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subB.a", "w11");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subB.b[0]", "w12");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subB.b[1]", "w13");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subB.b[2]", "w14");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subB.b[3]", "w15");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subB.b[4]", "w16");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subB.b[5]", "w17");

	DefaultAnnotation.fillWithResourceBundle(x4, new ConfigurableListResourceBundle(map));

	assertEquals("w1", x4.subA()[0].a());
	assertArrayEquals(new String[] { "w2", "w3", "w4" }, x4.subA()[0].b());
	assertEquals("w5", x4.subA()[1].a());
	assertArrayEquals(new String[] { "w6", "w7", "w8", "w9", "w10" }, x4.subA()[1].b());
	assertEquals("w11", x4.subB().a());
	assertArrayEquals(new String[] { "w12", "w13", "w14", "w15", "w16", "w17" }, x4.subB().b());
    }

    @Test
    public void testSubAnnotationsFromResourceBundlesNotOverridingSomeDefaultProperties() {

	X4 x4 = DefaultAnnotation.getAnnotation(Z4.class, X4.class);

	// The following map is not overriding the subA property.
	HashMap<String, String> map = new HashMap<String, String>();
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subB.a", "z0");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subB.b[0]", "z1");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subB.b[1]", "z2");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subB.b[2]", "z3");

	DefaultAnnotation.fillWithResourceBundle(x4, new ConfigurableListResourceBundle(map));

	assertEquals("i", x4.subA()[0].a());
	assertArrayEquals(new String[] { "ii", "iii" }, x4.subA()[0].b());
	assertEquals("iv", x4.subA()[1].a());
	assertArrayEquals(new String[] { "v" }, x4.subA()[1].b());
	assertEquals("z0", x4.subB().a());
	assertArrayEquals(new String[] { "z1", "z2", "z3" }, x4.subB().b());
    }

    @Test
    public void testLoadOnlyPartOfSubAnnotationFromResourceBundle() {

	X4 x4 = DefaultAnnotation.getAnnotation(Z4.class, X4.class);

	HashMap<String, String> map = new HashMap<String, String>();
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X4.subA[1].a", "tttttt");

	DefaultAnnotation.fillWithResourceBundle(x4, new ConfigurableListResourceBundle(map));

	assertEquals("i", x4.subA()[0].a());
	assertArrayEquals(new String[] { "ii", "iii" }, x4.subA()[0].b());
	assertEquals("tttttt", x4.subA()[1].a());
	assertArrayEquals(new String[] { "v" }, x4.subA()[1].b());
	assertEquals("vi", x4.subB().a());
	assertArrayEquals(new String[] { "vii", "viii", "ix", "x" }, x4.subB()
		.b());
    }

    // ///////////////////////////////////////////////////////////////////////
    // Multilevel, recursive
    // ///////////////////////////////////////////////////////////////////////

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X5 {
	String name();

	X5Sub[] subs() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X5Sub {
	String name();

	X5SubSub[] subs() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X5SubSub {
	String name();
    }

    @X5(name = "ichi", subs = {
	    @X5Sub(name = "ni", subs = { @X5SubSub(name = "san"),
		    @X5SubSub(name = "shi"), @X5SubSub(name = "go") }),
	    @X5Sub(name = "roku") })
    class Z5 {
    }

    @Test
    public void threeLevelsOfNestedAnnotations() {
	X5 x5 = DefaultAnnotation.getAnnotation(Z5.class, X5.class);
	assertEquals("ichi", x5.name());
	assertEquals(2, x5.subs().length);
	assertEquals("ni", x5.subs()[0].name());
	assertEquals(3, x5.subs()[0].subs().length);
	assertEquals("san", x5.subs()[0].subs()[0].name());
	assertEquals("shi", x5.subs()[0].subs()[1].name());
	assertEquals("go", x5.subs()[0].subs()[2].name());
	assertEquals("roku", x5.subs()[1].name());
	assertEquals(0, x5.subs()[1].subs().length);
    }

    /**
     * It's important that the whole structure that comes from
     * {@link DefaultAnnotation.getAnnotation(Class, Class)} is either
     * primitive, string, or annotations that are actually a dynamic proxy whose
     * invocation handler is an instance of {@link DefaultAnnotation}. This test
     * checks this requisite.
     */
    @Test
    public void testEverythingFromGetAnnotationIsDefaultAnnotation() {
	X5 x5 = DefaultAnnotation.getAnnotation(Z5.class, X5.class);
	assertTrue(DefaultAnnotation.isDefaultAnnotation(x5));
	assertTrue(DefaultAnnotation.isDefaultAnnotation(x5.subs()[0]));
	assertTrue(DefaultAnnotation.isDefaultAnnotation(x5.subs()[0].subs()[0]));
	assertTrue(DefaultAnnotation.isDefaultAnnotation(x5.subs()[0].subs()[1]));
	assertTrue(DefaultAnnotation.isDefaultAnnotation(x5.subs()[0].subs()[2]));
	assertTrue(DefaultAnnotation.isDefaultAnnotation(x5.subs()[1]));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X6 {
	String name();

	X6Sub[] subs() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X6Sub {
	String name();

	X6SubSub[] subs() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X6SubSub {
	String name();

	X6SubSubSub[] subs() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface X6SubSubSub {
	String name();
    }

    @Test
    public void threeLevelsReadingFromResourceBundle() {
	X6 x6 = DefaultAnnotation.getAnnotationWithDefaults(X6.class);

	assertNotNull(x6);

	HashMap<String, String> map = new HashMap<String, String>();
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.name", "World");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].name", "Americas");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[0].name", "North America");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[0].subs[0].name", "Canada");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[0].subs[1].name", "United States");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[0].subs[2].name", "Mexico");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[1].name", "Central America");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[1].subs[0].name", "Cuba");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[1].subs[1].name", "El Salvador");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[1].subs[2].name", "Puerto Rico");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[1].subs[3].name", "Haiti");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[2].name", "South America");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[2].subs[0].name", "Colombia");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[2].subs[1].name", "Venezuela");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[2].subs[2].name", "Brazil");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[2].subs[3].name", "Chile");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[2].subs[4].name", "Uruguay");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[1].name", "Eurasia");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[1].subs[0].name", "Europe");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[1].subs[0].subs[0].name", "France");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[1].subs[0].subs[1].name", "Germany");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[1].subs[0].subs[2].name", "Italy");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[1].subs[1].name", "Asia");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[1].subs[1].subs[0].name", "China");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[1].subs[1].subs[1].name", "Mongolia");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[1].subs[1].subs[2].name", "Japan");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[1].subs[1].subs[3].name", "South Korea");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[1].subs[1].subs[4].name", "Vietnam");

	DefaultAnnotation.fillWithResourceBundle(x6,
		new ConfigurableListResourceBundle(map));

	assertEquals("World", x6.name());
	assertEquals("Americas", x6.subs()[0].name());
	assertEquals("North America", x6.subs()[0].subs()[0].name());
	assertEquals("Canada", x6.subs()[0].subs()[0].subs()[0].name());
	assertEquals("United States", x6.subs()[0].subs()[0].subs()[1].name());
	assertEquals("Mexico", x6.subs()[0].subs()[0].subs()[2].name());
	assertEquals("Central America", x6.subs()[0].subs()[1].name());
	assertEquals("Cuba", x6.subs()[0].subs()[1].subs()[0].name());
	assertEquals("El Salvador", x6.subs()[0].subs()[1].subs()[1].name());
	assertEquals("Puerto Rico", x6.subs()[0].subs()[1].subs()[2].name());
	assertEquals("Haiti", x6.subs()[0].subs()[1].subs()[3].name());
	assertEquals("South America", x6.subs()[0].subs()[2].name());
	assertEquals("Colombia", x6.subs()[0].subs()[2].subs()[0].name());
	assertEquals("Venezuela", x6.subs()[0].subs()[2].subs()[1].name());
	assertEquals("Brazil", x6.subs()[0].subs()[2].subs()[2].name());
	assertEquals("Chile", x6.subs()[0].subs()[2].subs()[3].name());
	assertEquals("Uruguay", x6.subs()[0].subs()[2].subs()[4].name());
	assertEquals("Eurasia", x6.subs()[1].name());
	assertEquals("Europe", x6.subs()[1].subs()[0].name());
	assertEquals("France", x6.subs()[1].subs()[0].subs()[0].name());
	assertEquals("Germany", x6.subs()[1].subs()[0].subs()[1].name());
	assertEquals("Italy", x6.subs()[1].subs()[0].subs()[2].name());
	assertEquals("Asia", x6.subs()[1].subs()[1].name());
	assertEquals("China", x6.subs()[1].subs()[1].subs()[0].name());
	assertEquals("Mongolia", x6.subs()[1].subs()[1].subs()[1].name());
	assertEquals("Japan", x6.subs()[1].subs()[1].subs()[2].name());
	assertEquals("South Korea", x6.subs()[1].subs()[1].subs()[3].name());
	assertEquals("Vietnam", x6.subs()[1].subs()[1].subs()[4].name());

	map = new HashMap<String, String>();
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[1].subs[2].name", "Nicaragua");

	DefaultAnnotation.fillWithResourceBundle(x6, new ConfigurableListResourceBundle(map));
	assertEquals("World", x6.name());
	assertEquals("Americas", x6.subs()[0].name());
	assertEquals("North America", x6.subs()[0].subs()[0].name());
	assertEquals("Canada", x6.subs()[0].subs()[0].subs()[0].name());
	assertEquals("United States", x6.subs()[0].subs()[0].subs()[1].name());
	assertEquals("Mexico", x6.subs()[0].subs()[0].subs()[2].name());
	assertEquals("Central America", x6.subs()[0].subs()[1].name());
	assertEquals("Cuba", x6.subs()[0].subs()[1].subs()[0].name());
	assertEquals("El Salvador", x6.subs()[0].subs()[1].subs()[1].name());
	assertEquals("Nicaragua", x6.subs()[0].subs()[1].subs()[2].name());
	assertEquals("Haiti", x6.subs()[0].subs()[1].subs()[3].name());
	assertEquals("South America", x6.subs()[0].subs()[2].name());
	assertEquals("Colombia", x6.subs()[0].subs()[2].subs()[0].name());
	assertEquals("Venezuela", x6.subs()[0].subs()[2].subs()[1].name());
	assertEquals("Brazil", x6.subs()[0].subs()[2].subs()[2].name());
	assertEquals("Chile", x6.subs()[0].subs()[2].subs()[3].name());
	assertEquals("Uruguay", x6.subs()[0].subs()[2].subs()[4].name());
	assertEquals("Eurasia", x6.subs()[1].name());
	assertEquals("Europe", x6.subs()[1].subs()[0].name());
	assertEquals("France", x6.subs()[1].subs()[0].subs()[0].name());
	assertEquals("Germany", x6.subs()[1].subs()[0].subs()[1].name());
	assertEquals("Italy", x6.subs()[1].subs()[0].subs()[2].name());
	assertEquals("Asia", x6.subs()[1].subs()[1].name());
	assertEquals("China", x6.subs()[1].subs()[1].subs()[0].name());
	assertEquals("Mongolia", x6.subs()[1].subs()[1].subs()[1].name());
	assertEquals("Japan", x6.subs()[1].subs()[1].subs()[2].name());
	assertEquals("South Korea", x6.subs()[1].subs()[1].subs()[3].name());
	assertEquals("Vietnam", x6.subs()[1].subs()[1].subs()[4].name());

	map = new HashMap<String, String>();
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[3].name", "Others");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[3].subs[0].name", "Easter Island");
	map.put("org.hyphenType.tests.util.DefaultAnnotationTest$X6.subs[0].subs[3].subs[1].name", "Hawaii");

	DefaultAnnotation.fillWithResourceBundle(x6, new ConfigurableListResourceBundle(map));
	assertEquals("World", x6.name());
	assertEquals("Americas", x6.subs()[0].name());
	assertEquals("North America", x6.subs()[0].subs()[0].name());
	assertEquals("Canada", x6.subs()[0].subs()[0].subs()[0].name());
	assertEquals("United States", x6.subs()[0].subs()[0].subs()[1].name());
	assertEquals("Mexico", x6.subs()[0].subs()[0].subs()[2].name());
	assertEquals("Central America", x6.subs()[0].subs()[1].name());
	assertEquals("Cuba", x6.subs()[0].subs()[1].subs()[0].name());
	assertEquals("El Salvador", x6.subs()[0].subs()[1].subs()[1].name());
	assertEquals("Nicaragua", x6.subs()[0].subs()[1].subs()[2].name());
	assertEquals("Haiti", x6.subs()[0].subs()[1].subs()[3].name());
	assertEquals("South America", x6.subs()[0].subs()[2].name());
	assertEquals("Colombia", x6.subs()[0].subs()[2].subs()[0].name());
	assertEquals("Venezuela", x6.subs()[0].subs()[2].subs()[1].name());
	assertEquals("Brazil", x6.subs()[0].subs()[2].subs()[2].name());
	assertEquals("Chile", x6.subs()[0].subs()[2].subs()[3].name());
	assertEquals("Uruguay", x6.subs()[0].subs()[2].subs()[4].name());
	assertEquals("Others", x6.subs()[0].subs()[3].name());
	assertEquals("Easter Island", x6.subs()[0].subs()[3].subs()[0].name());
	assertEquals("Hawaii", x6.subs()[0].subs()[3].subs()[1].name());
	assertEquals("Eurasia", x6.subs()[1].name());
	assertEquals("Europe", x6.subs()[1].subs()[0].name());
	assertEquals("France", x6.subs()[1].subs()[0].subs()[0].name());
	assertEquals("Germany", x6.subs()[1].subs()[0].subs()[1].name());
	assertEquals("Italy", x6.subs()[1].subs()[0].subs()[2].name());
	assertEquals("Asia", x6.subs()[1].subs()[1].name());
	assertEquals("China", x6.subs()[1].subs()[1].subs()[0].name());
	assertEquals("Mongolia", x6.subs()[1].subs()[1].subs()[1].name());
	assertEquals("Japan", x6.subs()[1].subs()[1].subs()[2].name());
	assertEquals("South Korea", x6.subs()[1].subs()[1].subs()[3].name());
	assertEquals("Vietnam", x6.subs()[1].subs()[1].subs()[4].name());

    }
}
