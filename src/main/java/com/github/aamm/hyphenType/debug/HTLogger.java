package com.github.aamm.hyphenType.debug;

import java.io.PrintStream;

/**
 * A logger that only outputs things to a {@link PrintStream} if the
 * {@link HTLogger#HYPHEN_TYPE_DEBUG_MODE} environment variable is set to true (ignoring case).
 * This class also allows for the user to replace the {@link PrintStream}. The default
 * {@link PrintStream} is {@link System#out}.
 * 
 * @author akira
 */
public class HTLogger {
    
   /**
    * System environment key to allow for users to change the debug mode when calling the JVM 
    */
    public static final String HYPHEN_TYPE_DEBUG_MODE = "org.hyphenType.debug.debug-mode";
   
   /**
    * If true, this class was already initialized. 
    */
    private static boolean initialized = false;
   /**
    * The debug mode. If true, this class writes to the print writer. If false, this class does
    * nothing when {@link HTLogger#log(String)} is called.
    */
    private static boolean debugMode = false;
   /**
    * The {@link PrintStream} to be used. Can be replaced at runtime by calling
    * {@link HTLogger#setOutput(PrintStream)}.
    */
    private static PrintStream ps = null; 
   
   /**
    * Lazy initialization of this class. This initialization reads the environment variable and
    * makes sure that {@link HTLogger#ps} is not null; 
    */
   private static void lazyInitialize() {
       if(!initialized) {
           debugMode = Boolean.parseBoolean(System.getProperty(HYPHEN_TYPE_DEBUG_MODE));
           /*
            * ps may be not null if the user already set it to be something different from null
            * before calling the log method.
            */
           if(ps == null) {
               ps = System.out;
           }
           initialized = true;
       }
   }
   
   /**
    * Logs a message to the {@link PrintStream}, if debug mode is true.
    * 
    * @param message The message to log
    */
   public static void log(final String message) {
       lazyInitialize();
       
       if(debugMode) {
           StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
           ps.println(String.format("[%s.%s(%d)] %s", ste.getClassName(), ste.getMethodName(), ste.getLineNumber(), message));
       }
   }
   
   /**
    * Logs the full stack trace of a throwable.
    * 
    * @param message The throwable to log
    */
   public static void log(final Throwable t) {
       lazyInitialize();
       if(debugMode) {
           t.printStackTrace(ps);
       }
   }
   
   /**
    * Replaces the {@link PrintStream} used by this class to log messages. Does nothing if the
    * provided argument is null. If you want this class to stop writing log outputs, you should
    * set the debug mode to false using the method {@link HTLogger#setDebugMode(boolean)}.
    * 
    * @param newPs The new print stream to be used
    */
   public static void setOutput(final PrintStream newPs) {
       if(newPs!=null) {
           ps = newPs;
       }
   }
   
   /**
    * Allows it for programmatic change of the debug mode.
    * 
    * @param newDebugMode The new debug mode
    */
   public static void setDebugMode(final boolean newDebugMode) {
       debugMode = newDebugMode;
   }
   
   public static boolean debugMode() {
       return debugMode;
   }
}
