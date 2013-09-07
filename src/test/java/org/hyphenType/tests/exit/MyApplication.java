package org.hyphenType.tests.exit;

import org.hyphenType.datastructure.annotations.ArgumentsObject;

@ArgumentsObject(statusCodeEnum=MyExitStatus.class)
public class MyApplication {
    public void main(MyOptions options) {
        if(options.y().equals("a")) {
            throw new IllegalArgumentException("xa");
        } else if(options.y().equals("b")) {
            throw new NoSuchMethodError("xb");
        } else if(options.y().equals("c")) {
            throw new LinkageError("xc");
        }
    }
}
