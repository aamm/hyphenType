package org.hyphenType.tests.documentation.rbgenerator;

import org.hyphenType.datastructure.Options;
import org.hyphenType.datastructure.annotations.ArgumentsObject;
import org.hyphenType.datastructure.annotations.Option;
import org.hyphenType.datastructure.annotations.OptionValue;
import org.hyphenType.datastructure.annotations.SimpleArgument;
import org.hyphenType.documentation.lib.HTMLFormatterEngine.HTMLFormatter;
import org.hyphenType.exit.CanonicalExitCode;
import org.hyphenType.tests.documentation.rbgenerator.RBGeneratorTestValidatorEngine.DummyValidator;
import org.hyphenType.tests.documentation.rbgenerator.RBGeneratorTestValidatorEngine.Node;
import org.hyphenType.tests.documentation.rbgenerator.RBGeneratorTestValidatorEngine.SubNode;

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