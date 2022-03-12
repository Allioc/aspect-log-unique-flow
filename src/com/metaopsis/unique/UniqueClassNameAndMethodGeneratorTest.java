package com.metaopsis.unique;


import org.junit.Test;

import java.io.FileNotFoundException;

public class UniqueClassNameAndMethodGeneratorTest {

    @Test
    public void addClassLog() {
        UniqueClassNameAndMethodGenerator uniqueClassNameAndMethodGenerator = new UniqueClassNameAndMethodGenerator();
        uniqueClassNameAndMethodGenerator.addClassLog("ui.Main.resolveMetaopsisHome()");
        uniqueClassNameAndMethodGenerator.addClassLog("utilities.MetaopsisLicense.setCompany(String)");
        uniqueClassNameAndMethodGenerator.addClassLog("utilities.MetaopsisLicense.setEmail(String)");
        uniqueClassNameAndMethodGenerator.addClassLog("utilities.MetaopsisLicense.setAnalyze(boolean)");
        uniqueClassNameAndMethodGenerator.addClassLog("sample.MySample.setAnalyze(boolean)");
        uniqueClassNameAndMethodGenerator.addClassLog("sample.MySample.getAnalyze()");


        try {
            uniqueClassNameAndMethodGenerator.printUniqueClasses();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}