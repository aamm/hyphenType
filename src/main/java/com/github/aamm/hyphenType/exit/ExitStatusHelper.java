package com.github.aamm.hyphenType.exit;

import java.text.MessageFormat;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.util.DefaultAnnotation;
import com.github.aamm.hyphenType.util.I18NResourceBundle;

/**
 * The object that is passed to the {@link StatusCode#beforeExit(ExitStatusHelper)}
 * method.
 * 
 * @author akira
 */
public class ExitStatusHelper {

    /**
     * Message. Prefix i stands for instance.
     */
    private String iMessage;
    
    private Throwable throwable;
    
    /**
     * Creates a helper containing the message extracted
     * from the documentation formatter.
     * 
     * @param optionsInterface The options interface parsed using the command line arguments.
     * @param enumConstant The enum constant used as the basis for creating the key.
     * @param throwable The throwable that was responsible for this termination, if any.
     * @param args Replacements.
     */
    public ExitStatusHelper(final Class<? extends Options<?>> optionsInterface, final Enum<? extends StatusCode> enumConstant, final Throwable throwable, final Object... args) {
        try {
            ExitStatusConstant message = DefaultAnnotation.getAnnotation(enumConstant, ExitStatusConstant.class);
            DefaultAnnotation.fillWithResourceBundle(message, new I18NResourceBundle(optionsInterface));
            if(message.userDescription()==null) {
                iMessage = "";
            }
            else {
                iMessage = MessageFormat.format(message.message(), args);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.throwable = throwable;
    }
    
    /**
     * Gives access to the message associated with this exit
     * status code. At this point, the message was already formatted
     * according with the arguments given in the constructor of
     * this class: {@link ExitStatusHelper#ExitStatusHelper(String, Object...)}.
     * In other words, status code enumeration constants cannot
     * access the raw content of the resource bundles.
     * 
     * @return The message associated to this exit status code.
     */
    public final String getMessage() {
        return iMessage;
    }
    
    /**
     * Gives access to the throwable responsible for this
     * environment termination. Typically, an environment
     * can be for instance a JVM.
     * 
     * @return The throwable, if any.
     */
    public final Throwable getThrowable() {
        return throwable;
    }
}
