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
package com.github.aamm.hyphenType.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.ResourceBundle;

import com.github.aamm.hyphenType.util.soc.StringObjectConversion;
import com.github.aamm.hyphenType.util.soc.StringParsingError;

/**
 * @author Aurelio Akira M. Matsui
 */
public final class DefaultAnnotation implements InvocationHandler {

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // STATIC SECTION
    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String METHOD_SEPARATOR = ".";
    public static final String FIELD_SEPARATOR = ".";
    public static final String ANNOTATION_SEPARATOR = "@";
    
    /**
     * @param clazz TODO
     * @return TODO
     */
    @SuppressWarnings("unchecked")
    public static Annotation[] getAnnotations(final Class clazz) {

        Annotation[] annotations = clazz.getAnnotations();
        Annotation[] result = new Annotation[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            result[i] = getAnnotation(clazz, annotations[i].annotationType());
        }
        return result;
    }

    /**
     * @param <T>
     *            TODO
     * @param clazz
     *            TODO
     * @param annotationClass
     *            TODO
     * @return TODO
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(final Class clazz, final Class<T> annotationClass) {
//        if (clazz.isAnnotationPresent(annotationClass)) {
            Class proxyClass = Proxy.getProxyClass(DefaultAnnotation.class.getClassLoader(), new Class[] { annotationClass });
            try {
                return (T) proxyClass.getConstructor(new Class[] { InvocationHandler.class }).newInstance(new Object[] { new DefaultAnnotation(annotationClass, clazz.getAnnotation(annotationClass), clazz) });
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
//        } else {
//            return getAnnotationWithDefaults(annotationClass);
//        }
        return null;
    }

    /**
     * @param clazz
     *            TODO
     * @return TODO
     */
    public static Annotation[] getAnnotations(final Method method) {

        Annotation[] annotations = method.getAnnotations();
        Annotation[] result = new Annotation[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            result[i] = getAnnotation(method, annotations[i].annotationType());
        }
        return result;
    }

    /**
     * @param <T>
     *            TODO
     * @param clazz
     *            TODO
     * @param annotationClass
     *            TODO
     * @return TODO
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(final Method method, final Class<T> annotationClass) {
//        if (method.isAnnotationPresent(annotationClass)) {
            Class proxyClass = Proxy.getProxyClass(DefaultAnnotation.class.getClassLoader(), new Class[] { annotationClass });
            try {
                return (T) proxyClass.getConstructor(new Class[] { InvocationHandler.class }).newInstance(new Object[] { new DefaultAnnotation(annotationClass, method.getAnnotation(annotationClass), method) });
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
//        } else {
//            return getAnnotationWithDefaults(annotationClass);
//        }
        return null;
    }
    
    /**
     * @param clazz
     *            TODO
     * @return TODO
     */
    public static Annotation[] getAnnotations(final Field field) {

        Annotation[] annotations = field.getAnnotations();
        Annotation[] result = new Annotation[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            result[i] = getAnnotation(field, annotations[i].annotationType());
        }
        return result;
    }

    /**
     * @param <T>
     *            TODO
     * @param clazz
     *            TODO
     * @param annotationClass
     *            TODO
     * @return TODO
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(final Field field, final Class<T> annotationClass) {
//        if (field.isAnnotationPresent(annotationClass)) {
            Class proxyClass = Proxy.getProxyClass(DefaultAnnotation.class.getClassLoader(), new Class[] { annotationClass });
            try {
                return (T) proxyClass.getConstructor(new Class[] { InvocationHandler.class }).newInstance(new Object[] { new DefaultAnnotation(annotationClass, field.getAnnotation(annotationClass), field) });
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
//        } else {
//            return getAnnotationWithDefaults(annotationClass);
//        }
        return null;
    }
    
    /**
     * @param enumConstant
     *            An enumeration constant.
     * @return TODO
     */
    public static Annotation[] getAnnotations(final Enum<?> enumConstant) {
        Field f;
        try {
            f = enumConstant.getClass().getField(enumConstant.name());
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
        
        return getAnnotations(f);
    }

    /**
     * @param <T>
     *            TODO
     * @param enumConstant
     *            An enumeration constant.
     * @param annotationClass
     *            TODO
     * @return TODO
     */
    public static <T extends Annotation> T getAnnotation(final Enum<?> enumConstant, final Class<T> annotationClass) {
        Field f;
        try {
            f = enumConstant.getClass().getField(enumConstant.name());
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
        
        return getAnnotation(f, annotationClass);
    }
    
    /**
     * @param <T>
     *            TODO
     * @param annotationClass
     *            TODO
     * @return TODO
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotationWithDefaults(final Class<? extends Annotation> annotationClass) {
        try {
            Class proxyClass = Proxy.getProxyClass(DefaultAnnotation.class.getClassLoader(), new Class[] { annotationClass });
            return (T) proxyClass.getConstructor(new Class[] { InvocationHandler.class }).newInstance(new Object[] { new DefaultAnnotation(annotationClass) });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param annotation
     *            TODO
     * @param method
     *            TODO
     * @param value
     *            TODO
     */
    public static void set(final Annotation annotation, final Method method, final Object value) {

        if (!isDefaultAnnotation(annotation)) {
            throw new IllegalArgumentException("The first argument should be an annotation retrieved from " + DefaultAnnotation.class.getName());
        }
        DefaultAnnotation da = (DefaultAnnotation) Proxy.getInvocationHandler(annotation);
        da.values.put(method, value);
    }

    /**
     * @param annotation
     *            TODO
     * @param methodName
     *            TODO
     * @param value
     *            TODO
     */
    public static void set(final Annotation annotation, final String methodName, final Object value) {

        Method method;

        try {
            method = annotation.annotationType().getMethod(methodName);
        } catch (SecurityException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }

        InvocationHandler ih = Proxy.getInvocationHandler(annotation);
        if (!(ih instanceof DefaultAnnotation)) {
            throw new IllegalArgumentException("The first argument should be an annotation retrieved from " + DefaultAnnotation.class.getName());
        }
        DefaultAnnotation da = (DefaultAnnotation) ih;
        da.values.put(method, value);
    }

    /**
     * @param annotation
     *            TODO
     * @param resourceBundle
     *            TODO
     */
    public static void fillWithResourceBundle(final Annotation annotation, final ResourceBundle resourceBundle) {
        recursiveFillWithResourceBundle(annotation, resourceBundle, annotation.annotationType().getName());
    }

    /**
     * TODO A big, huge one! We still need to check if the contents of the resource bundle
     * are providing everything the annotation needs. It is expected that annotations
     * always have all its properties set. This requirement is so strong that it is
     * reinforced by compilation checks. In this method, we are not checking this condition,
     * so we urgently need to take a look at this. 
     * 
     * @param annotation
     *            TODO
     * @param resourceBundle
     *            TODO
     * @param key
     *            TODO
     */
    @SuppressWarnings("unchecked")
    private static void recursiveFillWithResourceBundle(final Annotation annotation, final ResourceBundle resourceBundle, final String key) {

        Class<? extends Annotation> anClass = annotation.annotationType();
        DefaultAnnotation defaultAnnotation = (DefaultAnnotation) Proxy.getInvocationHandler(annotation);

        for (Method method : anClass.getDeclaredMethods()) {

            String newkey = key + METHOD_SEPARATOR + method.getName();
            if (method.getReturnType().isAnnotation()) {
                Annotation subAnnotation = (Annotation) defaultAnnotation.values.get(method);
                recursiveFillWithResourceBundle(subAnnotation, resourceBundle, newkey);
                set(annotation, method, subAnnotation);
            } else {
                try {

                    if (method.getReturnType().isArray()) {

                        if (method.getReturnType().getComponentType().isAnnotation()) {

                            /* The following pattern for arrays:
                             * key = [ a, b, c]
                             * is not allowed for annotations, since annotations need to have properties as in:
                             * key[0].property1 = p1value
                             * key[0].property2 = p2value
                             * key[1].property1 = ...
                             * key[1].property2 = ...
                             */
                            
                            Object array = defaultAnnotation.invoke(null, method, new Object[] {});

                            int i = 0;
                            String arrayKey = newkey + "[" + i + "]";
                            boolean hasPrefix = hasPrefix(resourceBundle, arrayKey);
                            
                            ArrayList<Annotation> newArrayValue = new ArrayList<Annotation>();
                            
                            /* Here we need to split the algorithm into two parts. If the array
                             * is null, that means we are creating a new array from scratch. So
                             * we should simply ignore the "array" object. Conversely, if the
                             * array is not null, we overwrite its values when the resource
                             * bundle is offering an alternative and we keep the values from
                             * the original array when there is no alternative.
                             */
                            if (array == null) {
                                while (hasPrefix) {
                                    Annotation subAnnotation = DefaultAnnotation.getAnnotationWithDefaults((Class<? extends Annotation>) method.getReturnType().getComponentType());
                                    recursiveFillWithResourceBundle(subAnnotation, resourceBundle, arrayKey);
                                    newArrayValue.add(subAnnotation);
                                    i++;
                                    arrayKey = newkey + "[" + i + "]";
                                    hasPrefix = hasPrefix(resourceBundle, arrayKey);
                                }
                            } else {
                                while (hasPrefix || i < Array.getLength(array)) {
                                    if (i >= Array.getLength(array)) {
                                        // New value from resource bundle
                                        Annotation subAnnotation = DefaultAnnotation.getAnnotationWithDefaults((Class<? extends Annotation>) method.getReturnType().getComponentType());
                                        recursiveFillWithResourceBundle(subAnnotation, resourceBundle, arrayKey);
                                        newArrayValue.add(subAnnotation);
                                    } else if (hasPrefix) {
                                        // Replacing old value with one from resource bundle
                                        Annotation subAnnotation = (Annotation) Array.get(array, i);
                                        recursiveFillWithResourceBundle(subAnnotation, resourceBundle, arrayKey);
                                        newArrayValue.add(subAnnotation);
                                    } else {
                                        // Keeping old value
                                        newArrayValue.add((Annotation) Array.get(array, i));
                                    }
                                    i++;
                                    arrayKey = newkey + "[" + i + "]";
                                    hasPrefix = hasPrefix(resourceBundle, arrayKey);
                                }
                            }
                            
                            
                            defaultAnnotation.values.put(method, newArrayValue.toArray((Object[]) Array.newInstance(method.getReturnType().getComponentType(), newArrayValue.size())));
                        } else {
                            
                            /* If the array is described using a pattern such as:
                             * key = [ a, b, c] 
                             */
                            if(resourceBundle.containsKey(defaultAnnotation.getBasePrefix() + newkey)) {
                                set(annotation, method, StringObjectConversion.fromString(method.getReturnType(), resourceBundle.getString(defaultAnnotation.getBasePrefix() + newkey)));
                            }
                            /* If the pattern is:
                             * key[0] = a
                             * key[1] = b
                             * key[2] = c
                             */
                            else {
                                ArrayList<String> value = new ArrayList<String>();
                                int i = 0;
                                String arrayKey = newkey + "[" + i + "]";
                                while (resourceBundle.containsKey(arrayKey)) {
                                    value.add(resourceBundle.getString(arrayKey));
                                    i++;
                                    arrayKey = newkey + "[" + i + "]";
                                }
                                
                                if (value.size() > 0) {
                                    Object[] array = (Object[]) Array.newInstance(method.getReturnType().getComponentType(), value.size());
                                    set(annotation, method, value.toArray(array));
                                }
                            }
                        }
                    } else {
                        if(resourceBundle.containsKey(defaultAnnotation.getBasePrefix() + newkey)) {
                            set(annotation, method, StringObjectConversion.fromString(method.getReturnType(), resourceBundle.getString(defaultAnnotation.getBasePrefix() + newkey)));
                        } else if (resourceBundle.containsKey(newkey)) {
                            set(annotation, method, StringObjectConversion.fromString(method.getReturnType(), resourceBundle.getString(newkey)));
                        }
                    }
                } catch (StringParsingError e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param resourceBundle
     *            TODO
     * @param prefix
     *            TODO
     * @return TODO
     */
    private static boolean hasPrefix(final ResourceBundle resourceBundle, final String prefix) {
        for (String s : resourceBundle.keySet()) {
            if (s.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param annotation
     *            TODO
     * @return TODO
     */
    public static boolean isDefaultAnnotation(final Annotation annotation) {
        InvocationHandler ih = Proxy.getInvocationHandler(annotation);
        return ih instanceof DefaultAnnotation;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // NON-STATIC SECTION
    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 
     */
    private final Annotation annotation;
    /**
     * 
     */
    private final Class<? extends Annotation> annotationClass;
    /**
     * 
     */
    private final Hashtable<Method, Object> values = new Hashtable<Method, Object>();
    /**
     * 
     */
    private final String basePrefix;

    /**
     * @param clazz
     *            TODO
     */
    private DefaultAnnotation(final Class<? extends Annotation> clazz) {
        annotation = null;
        annotationClass = clazz;
        basePrefix = null;
        init();
    }

    /**
     * @param clazz
     *            TODO
     */
    private DefaultAnnotation(final Class<? extends Annotation> clazz, final Class<?> base) {
        annotation = null;
        annotationClass = clazz;
        basePrefix = base.getName() + ANNOTATION_SEPARATOR;
        init();
    }

    /**
     * @param clazz
     *            TODO
     */
    private DefaultAnnotation(final Class<? extends Annotation> clazz, final Method base) {
        annotation = null;
        annotationClass = clazz;
        basePrefix = base.getDeclaringClass().getName() + METHOD_SEPARATOR + base.getName() + ANNOTATION_SEPARATOR;
        init();
    }

    /**
     * @param clazz
     *            TODO
     */
    private DefaultAnnotation(final Class<? extends Annotation> clazz, final Field base) {
        annotation = null;
        annotationClass = clazz;
        basePrefix = base.getDeclaringClass().getName() + FIELD_SEPARATOR + base.getName() + ANNOTATION_SEPARATOR;
        init();
    }

    /**
     * @param clazz
     *            TODO
     * @param annotationObject
     *            TODO
     */
    private DefaultAnnotation(final Class<? extends Annotation> clazz, final Annotation annotationObject) {
        annotation = annotationObject;
        annotationClass = clazz;
        basePrefix = null;
        init();
    }
    
    /**
     * @param clazz
     *            TODO
     * @param annotationObject
     *            TODO
     */
    private DefaultAnnotation(final Class<? extends Annotation> clazz, final Annotation annotationObject, final Class<?> base) {
        annotation = annotationObject;
        annotationClass = clazz;
        basePrefix = base.getName() + ANNOTATION_SEPARATOR;
        init();
    }
    
    private DefaultAnnotation(final Class<? extends Annotation> clazz, final Annotation annotationObject, final Method base) {
        annotation = annotationObject;
        annotationClass = clazz;
        basePrefix = base.getDeclaringClass().getName() + METHOD_SEPARATOR + base.getName() + ANNOTATION_SEPARATOR;
        init();
    }
    
    private DefaultAnnotation(final Class<? extends Annotation> clazz, final Annotation annotationObject, final Field base) {
        annotation = annotationObject;
        annotationClass = clazz;
        basePrefix = base.getDeclaringClass().getName() + FIELD_SEPARATOR + base.getName() + ANNOTATION_SEPARATOR;
        init();
    }
    
    @SuppressWarnings("unchecked")
    private void init() {
        /*
         * If any of the annotation fields (methods) is also an annotation or an
         * array of annotations, we need these fields to be DefaultAnnotation
         * dynamic proxies too, otherwise we won't be able to change their
         * values later.
         */
        if (annotation != null) {
            try {
                for (Method method : annotationClass.getDeclaredMethods()) {
                    if (method.getReturnType().isAnnotation()) {
                        Annotation subAnnotation = (Annotation) method.invoke(annotation, new Object[] {});
                        Class proxyClass = Proxy.getProxyClass(DefaultAnnotation.class.getClassLoader(), new Class[] { method.getReturnType() });
                        Object dynamicProxy = proxyClass.getConstructor(new Class[] { InvocationHandler.class }).newInstance(new Object[] { new DefaultAnnotation((Class<? extends Annotation>) method.getReturnType(), subAnnotation, method) });
                        values.put(method, dynamicProxy);
                    } else if (method.getReturnType().isArray() && method.getReturnType().getComponentType().isAnnotation()) {
                        Object array = method.invoke(annotation, new Object[] {});
                        Object subArray = Array.newInstance(method.getReturnType().getComponentType(), Array.getLength(array));
                        for (int i = 0; i < Array.getLength(array); i++) {
                            Annotation subAnnotation = (Annotation) Array.get(array, i);
                            Class proxyClass = Proxy.getProxyClass(DefaultAnnotation.class.getClassLoader(), new Class[] { method.getReturnType().getComponentType() });
                            Object dynamicProxy = proxyClass.getConstructor(new Class[] { InvocationHandler.class }).newInstance(new Object[] { new DefaultAnnotation((Class<? extends Annotation>) method.getReturnType().getComponentType(), subAnnotation, method) });
                            Array.set(subArray, i, dynamicProxy);
                        }
                        values.put(method, subArray);
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            InvocationHandler ih = Proxy.getInvocationHandler(obj);
            if (ih instanceof DefaultAnnotation) {
                DefaultAnnotation other = (DefaultAnnotation) ih;
                return this.values.equals(other.values);
            } else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        try {
            String keyValueString = "";
            for (Method m : annotationClass.getDeclaredMethods()) {
                if (m.getReturnType().isArray()) {
                    keyValueString += String.format("%s=%s, ", m.getName(), Arrays.toString((Object[]) invoke(null, m, new Object[] {})));
                } else {
                    keyValueString += String.format("%s=%s, ", m.getName(), invoke(null, m, new Object[] {}));
                }
            }

            // Removing the trailing comma and space characters.
            if (annotationClass.getDeclaredMethods().length > 1) {
                keyValueString = keyValueString.substring(0, keyValueString.length() - 2);
            }

            return String.format("@%s(%s)", annotationClass.getName(), keyValueString);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return TODO
     * @see Annotation#annotationType()
     */
    public Class<? extends Annotation> annotationType() {
        return annotationClass;
    }
    
    public String getBasePrefix() {
        return basePrefix;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

        if (method.equals(Annotation.class.getMethod("equals", Object.class))) {
            return this.equals(args[0]);
        }
        if (method.equals(Annotation.class.getMethod("hashCode"))) {
            return this.hashCode();
        }
        if (method.equals(Annotation.class.getMethod("toString"))) {
            return this.toString();
        }
        if (method.equals(Annotation.class.getMethod("annotationType"))) {
            return this.annotationType();
        }
        if (method.equals(Object.class.getMethod("notify"))) {
            this.notify();
            return null;
        }
        if (method.equals(Object.class.getMethod("notifyAll"))) {
            this.notifyAll();
            return null;
        }
        if (method.equals(Object.class.getMethod("wait"))) {
            this.wait();
            return null;
        }
        if (method.equals(Object.class.getMethod("wait", long.class))) {
            this.wait((Long) args[0]);
            return null;
        }
        if (method.equals(Object.class.getMethod("wait", long.class, int.class))) {
            this.wait((Long) args[0], (Integer) args[1]);
            return null;
        }

        if (values.containsKey(method)) {
            return values.get(method);
        }

        if (annotation != null) {
            return method.invoke(annotation, args);
        }

        return method.getDefaultValue();
    }
}
