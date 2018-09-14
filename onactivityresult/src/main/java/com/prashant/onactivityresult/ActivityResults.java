package com.prashant.onactivityresult;

import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static com.prashant.onactivityresult.annotation.OnActivityResult.GENERATED_FILE_NAME_SUFFIX;

/**
 * @author Prashant Singh
 */
public class ActivityResults {
    private static final String TAG = ActivityResults.class.getSimpleName();

    public static void hook(final Object object, final int requestCode, final int resultCode, final Intent data) {
        try {
            Class<?> clazz = Class.forName(object.getClass().getCanonicalName() + GENERATED_FILE_NAME_SUFFIX);
            final Constructor<?> constructor = clazz.getConstructor(object.getClass());
            final Object hookerObj = constructor.newInstance(object);

            final String methodName = getGeneratedName(requestCode, resultCode);

            final Method declaredMethod = hookerObj.getClass().getDeclaredMethod(methodName, Intent.class);
            declaredMethod.invoke(hookerObj, data);
        } catch (Exception e) {
            if (e instanceof NoSuchMethodException) {
                Log.e(TAG, "Cannot find method annotated with @OnActivityResult(requestCode = "
                        + requestCode + ", resultCode = " + resultCode + ")");
            }
            e.printStackTrace();
        }
    }

    private static String getGeneratedName(final int requestCode, final int resultCode) {
        final StringBuilder methodName = new StringBuilder("requestCode_");
        if (requestCode < 0) {
            methodName.append("neg");
        }
        methodName.append(Math.abs((long) requestCode)).append("_resultCode_");
        if (resultCode < 0) {
            methodName.append("neg");
        }
        methodName.append(Math.abs((long) resultCode));
        return methodName.toString();
    }

    // TODO: 14/9/18 pure reflection method of calling the annotated method. remember me
    /*public static void hook(Object object, int requestCode, int resultCode, Intent data) {
        // get the class of current object
        Class<?> klass = object.getClass();

        // find all the methods declared in the class. Using Class.getDeclaredMethods() as is more efficient than
        // Class.getMethods
        final List<Method> allMethods = new ArrayList<>(Arrays.asList(klass.getDeclaredMethods()));

        for (final Method method : allMethods) {
            if (method.isAnnotationPresent(OnActivityResult.class)) {
                // find our annotated methods
                OnActivityResult annotation = method.getAnnotation(OnActivityResult.class);

                Log.d(TAG, "Method name : " + method.getName());
                Log.d(TAG, "expected requestCode:" + annotation.requestCode());
                Log.d(TAG, "expected resultCode:" + annotation.resultCode());
                Log.d(TAG, "actual requestCode:" + requestCode);
                Log.d(TAG, "actual resultCode:" + resultCode);

                // compare the actual and expected resultCode and requestCode
                if (resultCode == annotation.resultCode() && requestCode == annotation.requestCode()) {
                    // invoke the method
                    try {
                        method.invoke(object, data);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    // break the loop
                    break;
                } else {
                    Log.d(TAG, "result and request code did not match. Checking other annotation method");
                }
            }

            Log.d(TAG, "Looking for other candidate methods");
        }
    }*/
}
