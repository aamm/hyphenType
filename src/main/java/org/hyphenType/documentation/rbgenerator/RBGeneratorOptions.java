package org.hyphenType.documentation.rbgenerator;

import org.hyphenType.datastructure.Options;
import org.hyphenType.datastructure.annotations.ArgumentsObject;
import org.hyphenType.datastructure.annotations.Option;
import org.hyphenType.datastructure.annotations.OptionValue;
import org.hyphenType.datastructure.annotations.SimpleArgument;

@ArgumentsObject(description="Creates a resource bundle based on a options interface.")
@SuppressWarnings("unchecked")
public interface RBGeneratorOptions extends Options {

    @Option(names={"h", "help"}, description="Shows this help message.")
    boolean h();
    
//    @Option(names={"r", "readrb"}, description="Reads a resource bundle when loading the options interface. The actual file that will be read will be determined by the options interface.")
//    boolean r();
    
    @Option(names={"f", "filename"}, description="The name of the file to write the output to. If more than one language is supported, this name will be used as the basis to create the set of files. If no file is given, this tool will output the contents of the resource bundle to the standard output.")
    boolean f();
    
    @OptionValue(option="f", mandatory=true)
    String fileName();
    
    @SimpleArgument(index = 0, description="The options class that will be analyzed.")
    Class<? extends Options<?>> optionsClass();
    
    @SimpleArgument(index = 1, description="The language variants that will be supported. If no variant is provided, it will only write a template for the default variant. This argument is ignored if the -f, --file option is not used.")
    String[] supportedLanguages();
}
