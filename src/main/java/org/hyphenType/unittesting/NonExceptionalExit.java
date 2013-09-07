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
package org.hyphenType.unittesting;

import org.hyphenType.datastructure.Options;

/**
 * A (strange) exception that is thrown to simulate JVM exit when we utilize
 * {@link MockArgumentsInvocationHandler}. Behind the scenes, the
 * {@link MockArgumentsInvocationHandler} will utilize a
 * {@link UnitTestingOptionExtractor}, which in turn is responsible for throwing
 * exceptions of type {@link NonExceptionalExit}. There are cases in which
 * programmers may chose to design their algorithms under the assumption that a
 * call to methods {@link Options#exit(Enum)} or {@link Options#exit(int)} will
 * prevent subsequent code to be executed. This is the case, for instance, in
 * the following code:
 * 
 * <pre>
 * <code>
 * public static int x(Opt opt) {
 * 	if (!opt.a())
 * 		opt.exit(17);
 * 	return 12;
 * }
 * </code>
 * </pre>
 * 
 * Assume that {@code opt.a()} returns {@code true}. In such case, execution of
 * the method {@code x(Opt opt)} should never reach the {@code return}
 * statement, even when we execute this method during unit testing. Not
 * suspending the execution of the method {@code x(Opt opt)} when we reach the
 * statement {@code opt.exit(1)} may cause the behavior of the code under test
 * to change during unit testing. To prevent such behavior change, this
 * exception ( {@link NonExceptionalExit}) is silently thrown every time to
 * methods {@link Options#exit(Enum)} or {@link Options#exit(int)} are called.<br/>
 * <br/>
 * Since {@link NonExceptionalExit} is a {@link RuntimeException}, procedures
 * that call {@link Options#exit(Enum)} or {@link Options#exit(int)} directly do
 * not need to catch the exception for the source code to compile. Note that
 * there is no exception handling in the method {@code x(Opt opt)} above. On the
 * other hand, unit tests that cause the execution of methods such as {@code
 * x(Opt opt)} above will need to catch this exception. This is demonstrated in
 * the following code:
 * 
 * <pre>
 * <code>
 * &#64;Test
 * public void testX() {
 * 	UnitTestingOptionExtractor<Opt> unitTestingOptionExtractor = new UnitTestingOptionExtractor<Opt>(Opt.class);
 * 	Opt opt = oe.options("-b");
 * 	try {
 * 		x(opt); // Calling x(Opt opt) as defined above
 * 		assertFail("We should not reach this line.");
 * 	}
 * 	catch(NonExceptionalExit e) {
 * 		assertTrue(unitTestingOptionExtractor.exitIntCalled(opt));
 * 	}
 * }
 * </code>
 * </pre>
 * 
 * In short, throwing this exception does not mean a failure (that's why this
 * exception is called {@link NonExceptionalExit}), but is only a means to
 * interrupt execution of a procedure under test. To prevent
 * {@link UnitTestingOptionExtractor} to throw this exception (strictly
 * speaking, to prevent {@link MockArgumentsInvocationHandler} to throw this
 * exception), use the constructor
 * {@link UnitTestingOptionExtractor#UnitTestingOptionExtractor(Class, boolean)}
 * and pass {@code false} as the second argument.<br/>
 * <br/>
 * This class also provides the following methods to check how
 * {@link Options#exit(int)} or {@link Options#exit(Enum)} was called, and which
 * of them was called:
 * <ul>
 * <li> {@link NonExceptionalExit#getStatusCodeEnum()}</li>
 * <li> {@link NonExceptionalExit#exitEnumCalled()}</li>
 * <li> {@link NonExceptionalExit#getStatusCodeInt()}</li>
 * <li> {@link NonExceptionalExit#exitIntCalled()}</li>
 * </ul>
 * Those methods are equivalent to:
 * <ul>
 * <li> {@link UnitTestingOptionExtractor#getStatusCodeEnum(Options)}</li>
 * <li> {@link UnitTestingOptionExtractor#exitEnumCalled(Options)}</li>
 * <li> {@link UnitTestingOptionExtractor#getStatusCodeInt(Options)}</li>
 * <li> {@link UnitTestingOptionExtractor#exitIntCalled(Options)}</li>
 * </ul>
 * Here is an example on how to use those methods in unit testing:
 * 
 * <pre>
 * <code>
 * &#64;Test
 * public void testX() {
 * 	UnitTestingOptionExtractor<Opt> unitTestingOptionExtractor = new UnitTestingOptionExtractor<Opt>(Opt.class);
 * 	Opt opt = oe.options("-b");
 * 	try {
 * 		x(opt); // Calling x(Opt opt) as defined above
 * 		assertFail("We should not reach this line.");
 * 	}
 * 	catch(NonExceptionalExit e) {
 * 		assertTrue(e.exitIntCalled());
 * 		assertEquals(17, getStatusCodeInt());
 * 	}
 * }
 * </code>
 * </pre>
 * 
 * Additionally, the method {@link NonExceptionalExit#exitCallPoint()} can be
 * used to find in which class, file, and line is the statement that called
 * {@link Options#exit(Enum)} or {@link Options#exit(int)}:
 * 
 * <pre>
 * <code>
 * &#64;Test
 * public void sample1() throws InvalidOptionsInterfaceException, InvalidOptionException {
 * 	UnitTestingOptionExtractor<AnotherOptions> optionExtractor = new UnitTestingOptionExtractor<AnotherOptions>(AnotherOptions.class);
 * 	try {
 * 		AnotherApplication.main(optionExtractor.options("-x"));
 * 		fail("Should have thrown an exception on the line above.");
 * 	}
 * 	catch (NonExceptionalExit e) {
 * 		assertEquals(AnotherApplication.class.getName(), e.exitCallPoint().getClassName());
 * 		// Line number detection won't work unless classes were compiled using debug mode.
 * 		assertEquals(6, e.exitCallPoint().getLineNumber());
 * 		assertEquals("main", e.exitCallPoint().getMethodName());
 * 	}
 * }
 * </code>
 * </pre>
 * 
 * The return type of {@link NonExceptionalExit#exitCallPoint()} is
 * {@link StackTraceElement}, which is obtained via
 * {@link Exception#getStackTrace()}. Therefore, the stack trace element
 * returned by {@link NonExceptionalExit#exitCallPoint()} will only have the
 * line number data if the classes were compiled using debug mode. If classes
 * were not compiled with debug mode, the standard behavior is that those line
 * numbers will be all set to -1.
 * 
 * @author Aurelio Akira M. Matsui
 * @see UnitTestingOptionExtractor
 * @see MockArgumentsInvocationHandler
 * @see Options#exit(Enum)
 * @see Options#exit(int)
 */
public final class NonExceptionalExit extends RuntimeException {

    private static final long serialVersionUID = 1450961275714733458L;

    private final boolean intConstuctorCalled;
    private final int exitCode;
    private final Enum<?> exitCodeEnum;
    private StackTraceElement exitCallPoint;

    /**
     * This constructor is not visible from outside of this package to control which class can throw this exception.
     * 
     * @param exitCode
     */
    NonExceptionalExit(int exitCode) {
        this.intConstuctorCalled = true;
        this.exitCode = exitCode;
        this.exitCodeEnum = null;
        findExitCallPoint();
    }

    /**
     * This constructor is not visible from outside of this package to control which class can throw this exception.
     * 
     * @param exitCodeEnum
     */
    NonExceptionalExit(Enum<?> exitCodeEnum) {
        this.intConstuctorCalled = false;
        this.exitCode = exitCodeEnum.ordinal();
        this.exitCodeEnum = exitCodeEnum;
        findExitCallPoint();
    }

    private void findExitCallPoint() {
        exitCallPoint = null;
        StackTraceElement[] elements = this.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getLineNumber() < 0 && elements[i].getFileName() == null && elements[i].getClassName().startsWith("$Proxy") && elements[i].getMethodName().equals("exit") && i + 1 < elements.length) {
                exitCallPoint = elements[i + 1];
            }
        }
    }

    public Enum<?> getStatusCodeEnum() {
        return exitCodeEnum;
    }

    public boolean exitEnumCalled() {
        return !intConstuctorCalled;
    }

    public int getStatusCodeInt() {
        return exitCode;
    }

    public boolean exitIntCalled() {
        return intConstuctorCalled;
    }

    public StackTraceElement exitCallPoint() {
        return exitCallPoint;
    }
}
