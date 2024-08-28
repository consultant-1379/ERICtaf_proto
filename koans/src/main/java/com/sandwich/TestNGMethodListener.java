package com.sandwich;

import com.sandwich.koan.KoanIncompleteException;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;

import java.lang.annotation.Annotation;

import static com.sandwich.koan.KoanConstants.__;

public class TestNGMethodListener implements IInvokedMethodListener{

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if(isKoanPresent(method)){
            for (Annotation[] annotation: getParameterAnnotations(method)){
                for( Annotation anno: annotation){
                    if(anno.toString().contains(__)){
                        throw new KoanIncompleteException("");
                    }
                }
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    private boolean isKoanPresent(IInvokedMethod method){
        return method.getTestMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(com.sandwich.koan.Koan.class);
    }

    private Annotation[][] getParameterAnnotations(IInvokedMethod method){
        return method.getTestMethod().getConstructorOrMethod().getMethod().getParameterAnnotations();
    }
}
