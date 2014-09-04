package com.github.aamm.hyphenType.tests.documentation.rbgenerator;

import com.github.aamm.hyphenType.datastructure.Options;
import com.github.aamm.hyphenType.datastructure.annotations.ArgumentsObject;
import com.github.aamm.hyphenType.datastructure.annotations.Option;
import com.github.aamm.hyphenType.datastructure.annotations.OptionValue;
import com.github.aamm.hyphenType.datastructure.annotations.SimpleArgument;
import com.github.aamm.hyphenType.documentation.lib.HTMLFormatterEngine.HTMLFormatter;
import com.github.aamm.hyphenType.exit.CanonicalExitCode;
import com.github.aamm.hyphenType.tests.documentation.rbgenerator.RBGeneratorTestValidatorEngine.DummyValidator;
import com.github.aamm.hyphenType.tests.documentation.rbgenerator.RBGeneratorTestValidatorEngine.Node;
import com.github.aamm.hyphenType.tests.documentation.rbgenerator.RBGeneratorTestValidatorEngine.SubNode;

@ArgumentsObject(description="xyz")
@DummyValidator(
    p1=true,
    p2="pinapple",
    tree={
        @Node(
            subtree = {},
            subp1 = "red",
            subp2 = "green"
        ),
        @Node(
                subtree = {
                        @SubNode(subp1 = "blue", subp2 = "yellow"),
                        @SubNode(subp1 = "purple", subp2 = "pink")
                },
                subp1 = "black",
                subp2 = "grey"
        ),
        @Node(
                subtree = {},
                subp1 = "brown",
                subp2 = "orange"
        )
    }
)
@HTMLFormatter(usage="lime", meaning="white")
public interface RBGeneratorTestOptionsInterface extends Options<CanonicalExitCode> {

    @Option(description="243", names = {"i", "ii", "iii"})
    boolean a();

    @Option(description="oiy")
    boolean b();
    
    @OptionValue(option="b", mandatory=true)
    String bValue();
    
    @SimpleArgument(description="lalala")
    String arg1();
}