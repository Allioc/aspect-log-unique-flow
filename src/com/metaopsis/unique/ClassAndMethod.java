package com.metaopsis.unique;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class is to capture the Unique set of Classes and the unique list of methods in those classes.
 * */
public class ClassAndMethod implements Comparable<ClassAndMethod>{
    private String className;
    private Set<String> methodNamesList;
    private int ignorableMethodCount;

    public ClassAndMethod(String className) {
        this.className = className;
        this.methodNamesList = new LinkedHashSet<>();
    }

    public void addMethod(String methodName){
        methodNamesList.add(methodName);

        if(methodName.startsWith("set") || methodName.startsWith("get") || methodName.startsWith("toString")){
            ignorableMethodCount++;
        }

    }

    @Override
    public int compareTo(ClassAndMethod o) {
        return this.className.compareTo(o.className);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassAndMethod that = (ClassAndMethod) o;
        return className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }

    @Override
    public String toString() {
        return "ClassAndMethod{" +
                "className='" + className + '\'' +
                ", methodNamesList=" + methodNamesList +
                '}';
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Set<String> getMethodNamesList() {
        return methodNamesList;
    }

    public void setMethodNamesList(Set<String> methodNamesList) {
        this.methodNamesList = methodNamesList;
    }

    public boolean isIgnorable(){
        return methodNamesList.size() == ignorableMethodCount;
    }
}
