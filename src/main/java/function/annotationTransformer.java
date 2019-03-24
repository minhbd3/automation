package function;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class annotationTransformer implements IAnnotationTransformer {
    /**
     * @param annotation
     * @param testClass
     * @param testConstructor
     * @param  testMethod
     * @// TODO: 2019-03-07 / set retry for all main cases
     **/
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(retryAnalyzer.class);
    }
}
