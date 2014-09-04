package com.github.aamm.hyphenType.util.soc;

/**
 * An exception thrown for parsing errors.
 * @author akira
 */
public class StringParsingError extends Exception {
    
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 5608990012096939503L;

    /**
     * Constructor having only a message.
     * 
     * @param message The exception message.
     */
    StringParsingError(final String message) {
        super(message);
    }

    /**
     * Constructor having a message and a cause.
     * 
     * @param message The exception message.
     * @param cause The original cause.
     */
    StringParsingError(final String message, final Throwable cause) {
        super(message, cause);
    }
}