package com.metaopsis.unique;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * UniqueFlowIdentifier is to identify unique set of classes.
 * */
public class UniqueFlowIdentifier {

    private static final Logger log = Logger.getLogger(UniqueFlowIdentifier.class.getName());

    public static void main(String args[]) throws IOException {
        File appLog = new File("app.metaopsis.log");
        BufferedReader bufferedReader = null;
        Set<String> uniqueClassesTraversed = new LinkedHashSet<>();

        try{
             bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(appLog))) ;
             while(bufferedReader.ready()){
                String logLine = bufferedReader.readLine();
                  // Sample log file
                 // 2022-02-04 00:01:57 DEBUG Trace:18 - Aspect-Exits method: int com.metaopsis.dao.ConfigurationStatus.getConfigurationId()
                int indexOfAspect = logLine.indexOf("Aspect-");
                String splitLine = logLine.substring(indexOfAspect);
                uniqueClassesTraversed.add(splitLine);
            }
            log.info("File opened");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            log.info("No. of unique lines :"+ uniqueClassesTraversed.size());

            if(bufferedReader != null)
            bufferedReader.close();
        }

        //Process the Unique Lines.
        processUniqueLines(uniqueClassesTraversed);
    }

    /**
     * @implNote Process Unique Lines based on {@link ClassDiagramGenerator}
     * */
    private static void processUniqueLines(Collection<String> uniqueClassesTraversed) {

        BufferedWriter
                buffWriter = null;
        try {
                buffWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("flow.log")));
                buffWriter.append("@startuml");
            ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(buffWriter);
            for (String line : uniqueClassesTraversed) {
                buffWriter.newLine();
                log.info(line);
                classDiagramGenerator.addStep(line);
            }
            buffWriter.newLine();
            buffWriter.append("@enduml");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(buffWriter != null){
                try {
                    buffWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
