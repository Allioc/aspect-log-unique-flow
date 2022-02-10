package com.metaopsis.unique;

import java.io.IOException;
import java.io.Writer;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * PlantUml Class Generator from AspectJ Enter - Exit Entry point logs.
 * Sample logs.
 * =============
 * 2022-02-04 00:01:43 DEBUG Trace:9 - Aspect-Enters on method: void com.metaopsis.ui.Main.initLogging()
 * 2022-02-04 00:01:43 DEBUG Trace:18 - Aspect-Exits method: void com.metaopsis.ui.Main.initLogging()
 * 2022-02-04 00:01:43 DEBUG Trace:9 - Aspect-Enters on method: void com.metaopsis.ui.Main.createRequiredDirctories()
 * 2022-02-04 00:01:43 DEBUG Trace:18 - Aspect-Exits method: void com.metaopsis.ui.Main.createRequiredDirctories()
 * 2022-02-04 00:01:43 DEBUG Trace:9 - Aspect-Enters on method: boolean com.metaopsis.utilities.LicenseValidator.retrieveLicense(String)
 * 2022-02-04 00:01:43 DEBUG Trace:18 - Aspect-Exits method: boolean com.metaopsis.utilities.LicenseValidator.retrieveLicense(String)
 * 2022-02-04 00:01:43 DEBUG Trace:9 - Aspect-Enters on method: String com.metaopsis.utilities.Encryptor.decrypt(String)
 * */
public class SequenceDiagramMetadataGenerator extends Stack<String> {
    private static final Logger log = Logger.getLogger(SequenceDiagramMetadataGenerator.class.getName());

    /**
     * Initialize to Actor by default. The starting point.
     * */
    private String actor = "Actor";

    /**
     * Writes the output with PlantUml specific syntax.
     * */
    private Writer writer;

    public SequenceDiagramMetadataGenerator(Writer writer){
        super();
        this.writer = writer;
        push(actor);
    }

    /**
     * @implNote Identify the class Names by removing Aspect-.
     * Identify EnterStep or ExistStep based on the keyword.
     * */
    public void addStep(String classLog) throws IOException {

        int indexOfAspect = classLog.indexOf("Aspect-");

        String classWithEntersOrExit = classLog.substring(indexOfAspect);
        int classNameIndex = classWithEntersOrExit.indexOf("com.");
        String className = classWithEntersOrExit.substring(classNameIndex);

        if(classWithEntersOrExit.startsWith("Aspect-Enters")) {
            addEnterStep(className);
        }else if ( classWithEntersOrExit.startsWith("Aspect-Exits")) {
            addExitStep(className);
        }

    }

    /**
     * Actor - Parent
     * 1. Peek and set source = "peekedValue"
     * 2. Set the NewNode as Target.
     * 3. Write plantuml Expression
     * 4. Push NewNode into Stack.
     * */
    private void addEnterStep(String className) throws IOException {
        // 1. Peek and set source = "peekedValue"
        String source  = peek();

        // 2. Set the NewNode as Target.
        String target = className;

        // 3. Write plantuml Expression
        writePlantUmlExpression(true, source, target);

        // 4. Push NewNode into Stack.
        push(className);
    }

    /**
     * @implNote Writes the sequence diagram flow.
     *
     * //TODO: Need to move the package name to properties so that this can be generalized.
     * */
    private void writePlantUmlExpression(boolean isEnters, String source, String target) throws IOException {
        source = source.replaceAll("com.metaopsis.", "");
        target = target.replaceAll("com.metaopsis.", "");
        String arrow = " -> ";
        if(!isEnters){
            arrow = " -[#0000FF]-> ";
        }
        String umlLine = "\""+source +"\"" + arrow+ "\"" + target +"\"";
        writer.write(umlLine);

    }

    /**
     * 1. Peek to Ensure the top node that matches the Existing Node
     * 2. Pop it and assign it as Source.
     * 3. Peek to get the Target.
     * 4. Write Source -> Target
     *
     * */
    private void addExitStep(String className) throws IOException {
       // 1. Peek to Ensure the top node that matches the Existing Node
        String source = peek();

        // An Exit step should match with an Entry Step. Otherwise,
        if(!source.equalsIgnoreCase(className)){
            throw new RuntimeException("UnExpected Node");
        }

        // 2. Pop it and assign it as Source. ( D - Source )
        // A -> B -> C -> D
        pop();
        // A -> B -> C ( After pop )

       // 3. Peek to get the Target. ( C - Target )
        String target = peek();

       // 4. Write Source -> Target
        writePlantUmlExpression(false, source, target);
    }


}
