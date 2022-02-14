package com.metaopsis.unique;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UniqueFlowIdentifier is to identify unique set of classes.
 * */
public class UniqueFlowIdentifier {

    private static final Logger log = Logger.getLogger(UniqueFlowIdentifier.class.getName());

    /**
     * Pass three arguments
     * 1) folderPath, 2) baseLogFileName, 3) numLogFiles, 4) isCompleteFlow
     * Args java com.metaopsis.unique.UniqueFlowIdentifier app_logs app.log 16
     * */
    public static void main(String args[]) throws Exception {

        String folderPath;
        String baseLogFileName;
        int numLogFiles =0;
        boolean isCompleteFlow = false; // def

        if(args != null && args.length > 3){
            folderPath = args[0];
            baseLogFileName = args[1];
            try{
                numLogFiles = Integer.parseInt(args[2]);
            }catch(NumberFormatException nfe){
                log.log(Level.SEVERE, "numLogFiles should be numeric!"+ numLogFiles );
                throw new Exception(nfe.getCause());
            }
            try{
                isCompleteFlow = Boolean.parseBoolean(args[3]);
            }catch(RuntimeException pe){
                log.log(Level.SEVERE, "Defaulting to Unique flow"+ isCompleteFlow );
            }
        }else{
            log.log(Level.SEVERE, "Please provide 1) folderPath, 2) baseLogFileName, 3) numLogFiles");
            throw new IllegalArgumentException("Please provide 1) folder path, 2) baseLogFileName, 3) numLogFiles");
        }

        //Create collection based on the usecase and use it.
        Collection<String> uniqueClassesTraversed = createClassesTraversed(isCompleteFlow);


        String appLogFolder = folderPath;
        String appLogBasePath = baseLogFileName;
        int logIndex = numLogFiles;
        while(logIndex > -1) {
            processAppLog(uniqueClassesTraversed, appLogFolder + "/"+ appLogBasePath+"."+logIndex);
            System.out.println(logIndex);
            logIndex--;
        }

        //Process the Unique Lines.
        processUniqueLines(uniqueClassesTraversed);

    }

    /**
     * @implNote Create Collection based on Complete or unique flow
     * @param isCompleteFlow - true - Complete, false - Unique
     * */
    private static Collection<String> createClassesTraversed(boolean isCompleteFlow) {
        if(isCompleteFlow){
            log.info("Creating Complete Flow");
            return new ArrayList<>();
        }else{
            log.info("Creating Unique Flow");
            return new LinkedHashSet<>();
        }
    }

    /**
     * @implNote Process each appLog file.
     * @param uniqueClassesTraversed
     * @param appLog
     * */
    private static void processAppLog(Collection<String> uniqueClassesTraversed, String appLog) throws IOException {

        File appLogFile = new File(appLog);
        BufferedReader bufferedReader = null;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(appLogFile))) ;
            while(bufferedReader.ready()){
                String logLine = bufferedReader.readLine();
                // Sample log file
                // 2022-02-04 00:01:57 DEBUG Trace:18 - Aspect-Exits method: int com.metaopsis.dao.ConfigurationStatus.getConfigurationId()
                int indexOfAspect = logLine.indexOf("Aspect-");
                if(indexOfAspect > -1) {
                    String splitLine = logLine.substring(indexOfAspect);
                    uniqueClassesTraversed.add(splitLine);
                }else {
                   // System.out.println("Un Aspected :" + logLine);
                }
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
    }

    /**
     * @implNote Process Unique Lines based on {@link SequenceDiagramMetadataGenerator}
     * @param uniqueClassesTraversed
     * */
    private static void processUniqueLines(Collection<String> uniqueClassesTraversed) {

        BufferedWriter
                buffWriter = null;
        try {
                buffWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("flow.log")));
                buffWriter.append("@startuml");
            SequenceDiagramMetadataGenerator sequenceDiagramMetadataGenerator = new SequenceDiagramMetadataGenerator(buffWriter);
            for (String line : uniqueClassesTraversed) {
                buffWriter.newLine();
                log.fine(line);
                sequenceDiagramMetadataGenerator.addStep(line);
            }
            buffWriter.newLine();
            buffWriter.append("@enduml");

            //Process Unique Classes and Methods
            sequenceDiagramMetadataGenerator.processUniqueClassesAndMethods();

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
