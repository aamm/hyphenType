package com.github.aamm.hyphenType.tests.documentation;

import com.github.aamm.hyphenType.exit.ExitStatusHelper;
import com.github.aamm.hyphenType.exit.StatusCode;

public enum DummyStatusCodeEnum implements StatusCode {
    SUCCESS,
    ERROR_1,
    ERROR_2
    ;

    @Override
    public void beforeExit(ExitStatusHelper helper) {
    }

}
