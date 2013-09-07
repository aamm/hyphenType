package org.hyphenType.tests.unittesting;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hyphenType.datastructure.annotations.InputChannel;
import org.hyphenType.datastructure.parser.StructureArgument;
import org.hyphenType.datastructure.parser.simple.StructureSimpleArgument;
import org.hyphenType.input.UserInputException;
import org.hyphenType.unittesting.UnitTestingUserInput;
import org.junit.Before;
import org.junit.Test;

public class UnitTestingUserInputTest {

    public interface X {
        public String[] x();
    }
    
    private Method method;
    
    @Before
    public void before() {
        try {
            method = X.class.getMethod("x");
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadArray() {
        
        List<InputChannel> list = Arrays.asList(new InputChannel[]{InputChannel.GUI, InputChannel.TEXT});
        StructureArgument argument = new StructureSimpleArgument(method, "a", true, 0, "", list, "");
        
        UnitTestingUserInput input = new UnitTestingUserInput();
        input.setGraphicalUIAvailable(true);
        input.setTextUIAvailable(false);
        input.addBothUIInput("a");
        input.addBothUIInput("b");
        input.addBothUIInput("c");
        input.addBothUIInput(null);
        try {
            String[] s = input.readArray(argument, "-", "--");
            assertTrue(Arrays.equals(new String[]{"a", "b", "c"}, s));
        } catch (UserInputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadArrayException() throws UserInputException {
        
        StructureArgument argument = new StructureSimpleArgument(method, "a", true, 0, "", new ArrayList<InputChannel>(), "");
        
        UnitTestingUserInput input = new UnitTestingUserInput();
        input.setGraphicalUIAvailable(false);
        input.setTextUIAvailable(false);
        input.addBothUIInput("x");
        try {
            input.readArray(argument, "-", "--");
        } catch(IllegalArgumentException e) {
            assertEquals("Input channels should have at least one of TEXT or GUI.", e.getMessage());
        }
    }

    @Test
    public void testReadArrayWrongCombination() {
        
        List<InputChannel> list = Arrays.asList(new InputChannel[]{InputChannel.GUI});
        StructureArgument argument = new StructureSimpleArgument(method, "a", true, 0, "", list, "");
        
        UnitTestingUserInput input = new UnitTestingUserInput();
        input.setGraphicalUIAvailable(false);
        input.setTextUIAvailable(true);
        input.addBothUIInput("x");
        try {
            input.readArray(argument, "-", "--");
        } catch(UserInputException e) {
            assertEquals("No argument input channel combination available.", e.getMessage());
        }
    }

    @Test
    public void testReadString() {
        
    }

    @Test(expected=UserInputException.class)
    public void testReadStringException() throws UserInputException {
        
        StructureArgument argument = new StructureSimpleArgument(method, "a", true, 0, "", new ArrayList<InputChannel>(), "");

        UnitTestingUserInput input = new UnitTestingUserInput();
        input.setTextUIAvailable(false);
        input.setGraphicalUIAvailable(false);
        input.addBothUIInput("x");
        input.readString(argument, "-", "--");
    }

}
