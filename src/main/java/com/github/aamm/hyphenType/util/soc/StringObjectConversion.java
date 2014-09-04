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
package com.github.aamm.hyphenType.util.soc;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.StringTokenizer;

import com.github.aamm.hyphenType.debug.HTLogger;

/**
 * Converts strings to objects and vice-versa.
 * 
 * @author Aurelio Akira M. Matsui
 */
public final class StringObjectConversion {

    /**
     * Turns it into a library class.
     */
    private StringObjectConversion() {
    }

    /**
     * Translates a string into an object. The algorithm used is as follows. If
     * {@code clazz} is {@link String}, simply returns the string value
     * received. If {@code clazz} is primitive or a wrapper for a primitive,
     * returns the result of calling a {@code valueOf} method. If {@code clazz} is
     * of a type {@link Class}, it searches for the class using
     * {@link Class#forName(String)}. Finally, if {@code clazz} does not match
     * any of those tests, tries to construct a new object, guessing that
     * {@code clazz} has a constructor that receives only a {@link String}.
     * 
     * @param <T> The type to convert to.
     * @param clazz The same as the type T.
     * @param value The string value of the object.
     * @return The object equivalent to the string.
     * @throws StringParsingError If there is any reflection problem.
     */
    public static <T> T fromString(final Class<? extends T> clazz, final String value) throws StringParsingError {
        return fromString(clazz, value, false, "[", "]", ",");
    }

    /**
     * Translates a string into an object. The algorithm used is as follows. If
     * {@code clazz} is {@link String}, simply returns the string value
     * received. If {@code clazz} is primitive or a wrapper for a primitive,
     * returns the result of calling a {@code valueOf} method. If {@code clazz} is
     * of a type {@link Class}, it searches for the class using
     * {@link Class#forName(String)}. Finally, if {@code clazz} does not match
     * any of those tests, tries to construct a new object, guessing that
     * {@code clazz} has a constructor that receives only a {@link String}.
     * 
     * @param <T> The type to convert to.
     * @param clazz The same as the type T.
     * @param value The string value of the object.
     * @param arrayForgiving Whether it will forgive the absence of square brackets.
     * @return The object equivalent to the string.
     * @throws StringParsingError If there is any reflection problem.
     */
    public static <T> T fromString(final Class<? extends T> clazz, final String value, final boolean arrayForgiving) throws StringParsingError {
        return fromString(clazz, value, arrayForgiving, "[", "]", ",");
    }

    /**
     * Translates a string into an object. The algorithm used is as follows. If
     * {@code clazz} is {@link String}, simply returns the string value
     * received. If {@code clazz} is primitive or a wrapper for a primitive,
     * returns the result of calling a {@code valueOf} method. If {@code clazz} is
     * of a type {@link Class}, it searches for the class using
     * {@link Class#forName(String)}. Finally, if {@code clazz} does not match
     * any of those tests, tries to construct a new object, guessing that
     * {@code clazz} has a constructor that receives only a {@link String}.
     * 
     * @param <T> The type to convert to.
     * @param clazz The same as the type T.
     * @param value The string value of the object.
     * @param arrayForgiving Whether it will forgive the absence of square brackets.
     * @return The object equivalent to the string.
     * @throws StringParsingError If there is any reflection problem.
     */
    public static <T> T fromString(final Class<? extends T> clazz, final String value, final boolean arrayForgiving, final String separator) throws StringParsingError {
        return fromString(clazz, value, arrayForgiving, "[", "]", separator);
    }

    /**
     * Translates a string into an object. The algorithm used is as follows. If
     * {@code clazz} is {@link String}, simply returns the string value
     * received. If {@code clazz} is primitive or a wrapper for a primitive,
     * returns the result of calling a {@code valueOf} method. If {@code clazz} is
     * of a type {@link Class}, it searches for the class using
     * {@link Class#forName(String)}. Finally, if {@code clazz} does not match
     * any of those tests, tries to construct a new object, guessing that
     * {@code clazz} has a constructor that receives only a {@link String}.
     * 
     * @param <T> The type to convert to.
     * @param clazz The same as the type T.
     * @param value The string value of the object.
     * @param arrayForgiving Whether it will forgive the absence of square brackets.
     * @param arrayStart
     * @param arrayEng
     * @param arraySeparator
     * @return
     * @throws StringParsingError
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromString(final Class<? extends T> clazz, final String value, final boolean arrayForgiving, final String arrayStart, final String arrayEnd, final String arraySeparator) throws StringParsingError {

        try {
            if (clazz.equals(String.class)) {
                return (T) value;
            }
            if (clazz.equals(boolean.class)) {
                return (T) Boolean.valueOf(value);
            }
            if (clazz.equals(Boolean.class)) {
                return (T) Boolean.valueOf(value);
            }
            if (clazz.equals(byte.class)) {
                return (T) Byte.valueOf(value);
            }
            if (clazz.equals(Byte.class)) {
                return (T) Byte.valueOf(value);
            }
            if (clazz.equals(char.class)) {
                return (T) Character.valueOf(value.charAt(0));
            }
            if (clazz.equals(Character.class)) {
                return (T) Character.valueOf(value.charAt(0));
            }
            if (clazz.equals(short.class)) {
                return (T) Short.valueOf(value);
            }
            if (clazz.equals(Short.class)) {
                return (T) Short.valueOf(value);
            }
            if (clazz.equals(int.class)) {
                return (T) Integer.valueOf(value);
            }
            if (clazz.equals(Integer.class)) {
                return (T) Integer.valueOf(value);
            }
            if (clazz.equals(long.class)) {
                return (T) Long.valueOf(value);
            }
            if (clazz.equals(Long.class)) {
                return (T) Long.valueOf(value);
            }
            if (clazz.equals(float.class)) {
                return (T) Float.valueOf(value);
            }
            if (clazz.equals(Float.class)) {
                return (T) Float.valueOf(value);
            }
            if (clazz.equals(double.class)) {
                return (T) Double.valueOf(value);
            }
            if (clazz.equals(Double.class)) {
                return (T) Double.valueOf(value);
            }
            if (clazz.equals(Class.class)) {
                return (T) Class.forName(value);
            }
            if (clazz.isEnum()) {
                return (T) Enum.valueOf((Class<Enum>) clazz, value);
            }
            if (clazz.isArray()) {
                String correctedValue;
                if (arrayForgiving) {
                    correctedValue = value;
                    if(!value.startsWith(arrayStart)) {
                        correctedValue = arrayStart + value + arrayEnd;
                    }
                } else {
                    correctedValue = value;
                    if (!(value.startsWith(arrayStart) && value.endsWith(arrayEnd))) {
                        throw new StringParsingError("Invalid string representation of an array:\"" + value + "\". Valid patterns should be between square brackets.");
                    }
                }
                StringTokenizer tokenizer = new StringTokenizer(correctedValue.substring(1, correctedValue.length()-1), arraySeparator);
                Object result = Array.newInstance(clazz.getComponentType(), tokenizer.countTokens());
                int i = 0;
                while(tokenizer.hasMoreTokens()) {
                    Array.set(result, i, fromString(clazz.getComponentType(), tokenizer.nextToken().trim()));
                    i++;
                }
                return (T) result;
            }
            Constructor constructor;
            constructor = clazz.getConstructor(String.class);
            return (T) constructor.newInstance(value);
        } catch (SecurityException e) {
            throw new StringParsingError("Cannot access the constructor " + clazz.getName() + "(String)");
        } catch (NoSuchMethodException e) {
            throw new StringParsingError("Constructor " + clazz.getName() + "(String) not found");
        } catch (ClassNotFoundException e) {
            HTLogger.log(e);
            throw new StringParsingError("Cannot find class " + value);
        } catch (IllegalArgumentException e) {
            throw new StringParsingError("Could not instantiate using value: " + value, e);
        } catch (InstantiationException e) {
            throw new StringParsingError("Could not instantiate using value: " + value, e);
        } catch (IllegalAccessException e) {
            throw new StringParsingError("Could not instantiate using value: " + value, e);
        } catch (InvocationTargetException e) {
            throw new StringParsingError("Could not instantiate using value: " + value, e);
        } catch (Throwable e) {
            throw new StringParsingError("Problems while parsing the value " + value + " to the class " + clazz.getName(), e);
        }
    }
    
    /**
     * Parses the object into a primitive.
     * 
     * @param clazz The primitive class.
     * @param object The object that will be converted to a primitive.
     * @return The equivalent primitive, or null if there is no equivalent.
     */
    @SuppressWarnings("unchecked")
    public static Object toPrimitive(final Class clazz, final Object object) {
        if (clazz.equals(boolean.class)) {
            return ((Boolean) object).booleanValue();
        }
        if (clazz.equals(Boolean.class)) {
            return ((Boolean) object).booleanValue();
        }
        if (clazz.equals(byte.class)) {
            return ((Byte) object).byteValue();
        }
        if (clazz.equals(Byte.class)) {
            return ((Byte) object).byteValue();
        }
        if (clazz.equals(char.class)) {
            return ((Character) object).charValue();
        }
        if (clazz.equals(Character.class)) {
            return ((Character) object).charValue();
        }
        if (clazz.equals(short.class)) {
            return ((Short) object).shortValue();
        }
        if (clazz.equals(Short.class)) {
            return ((Short) object).shortValue();
        }
        if (clazz.equals(int.class)) {
            return ((Integer) object).intValue();
        }
        if (clazz.equals(Integer.class)) {
            return ((Integer) object).intValue();
        }
        if (clazz.equals(long.class)) {
            return ((Long) object).longValue();
        }
        if (clazz.equals(Long.class)) {
            return ((Long) object).longValue();
        }
        if (clazz.equals(float.class)) {
            return ((Float) object).floatValue();
        }
        if (clazz.equals(Float.class)) {
            return ((Float) object).floatValue();
        }
        if (clazz.equals(double.class)) {
            return ((Double) object).doubleValue();
        }
        if (clazz.equals(Double.class)) {
            return ((Double) object).doubleValue();
        }
        return null;
    }
}
