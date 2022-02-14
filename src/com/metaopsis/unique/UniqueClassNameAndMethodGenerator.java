package com.metaopsis.unique;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class UniqueClassNameAndMethodGenerator {
    private static final Logger log = Logger.getLogger(String.valueOf(UniqueClassNameAndMethodGenerator.class));

    private Map<String, ClassAndMethod> classAndMethodMap;

    public UniqueClassNameAndMethodGenerator(){
        classAndMethodMap = new LinkedHashMap<>();
    }

    /**
     * ui.Main.resolveMetaopsisHome()
     * lastIndexOf(".") should give the className.
     * Remaining should give the method Name.
     * */
    public void addClassLog(String classLog){
        int classNameIndex = classLog.lastIndexOf(".");
        String className = classLog.substring(0,classNameIndex);
        String methodName = classLog.substring(classNameIndex+1);

        log.info(className);
        log.info(methodName);

        ClassAndMethod classAndMethod = classAndMethodMap.get(className);
        if(classAndMethod == null){
            classAndMethod = new ClassAndMethod(className);
        }
        classAndMethod.addMethod(methodName);
        classAndMethodMap.put(className, classAndMethod);
    }

    public void printUniqueClasses() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(new File("uniqueclasses.log")));
        try{

            Iterator<String> classNameIter = classAndMethodMap.keySet().iterator();
            while (classNameIter.hasNext()){
                String className = classNameIter.next();

                ClassAndMethod classAndMethod = classAndMethodMap.get(className);
                if(classAndMethod.isIgnorable()){
                    continue;
                }
                pw.write("\nClassName: "+className);
                for(String methodName: classAndMethod.getMethodNamesList()){
                    pw.write("\t\t\n Method :"+methodName);
                }
                pw.write("\n");
            }
        }catch(Exception e){
            log.severe("Could not write unique classes to file."+e.getCause());
        }finally {
            if(pw != null){
                pw.close();
            }
        }
    }

}
