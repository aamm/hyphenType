package org.hyphenType.tests.documentation;

import org.hyphenType.exit.ExitStatusHelper;
import org.hyphenType.exit.StatusCode;

public enum DummyStatusCodeEnum implements StatusCode {
    SUCCESS,
    ERROR_1,
    ERROR_2
    ;

    @Override
    public void beforeExit(ExitStatusHelper helper) {
    }

}
