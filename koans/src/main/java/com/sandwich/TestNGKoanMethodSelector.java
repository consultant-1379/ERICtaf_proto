package com.sandwich;

import org.testng.*;
import java.util.List;

public class TestNGKoanMethodSelector implements IMethodSelector {

    @Override
    public boolean includeMethod(IMethodSelectorContext context, ITestNGMethod method, boolean isTestMethod) {
        return method.getConstructorOrMethod().getMethod().isAnnotationPresent(com.sandwich.koan.Koan.class);
    }

    @Override
    public void setTestMethods(List<ITestNGMethod> testMethods) {

    }
}
