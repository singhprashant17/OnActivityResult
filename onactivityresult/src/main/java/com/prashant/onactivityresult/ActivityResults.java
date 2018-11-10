package com.prashant.onactivityresult;

import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import static com.prashant.onactivityresult.annotation.Helpers.getGeneratedFileNameSuffix;
import static com.prashant.onactivityresult.annotation.Helpers.getGeneratedMethodName;

/**
 * @author Prashant Singh
 */
public class ActivityResults {
    private static final String TAG = ActivityResults.class.getSimpleName();
    private static final HashMap<Object, Object> CACHE = new HashMap<>();

    public static void hook(final Object target, final int requestCode, final int resultCode, final Intent data) {
        try {
            Class<?> clazz = Class.forName(target.getClass().getName() + getGeneratedFileNameSuffix());

            Object hookerObj = CACHE.get(target);
            if (hookerObj == null) {
                final Constructor<?> constructor = clazz.getConstructor(target.getClass());
                hookerObj = constructor.newInstance(target);
                CACHE.put(target, hookerObj);
            }

            final String methodName = getGeneratedMethodName(requestCode, resultCode);

            final Method declaredMethod = hookerObj.getClass().getDeclaredMethod(methodName, Intent.class);
            declaredMethod.invoke(hookerObj, data);
        } catch (Exception e) {
            if (e instanceof NoSuchMethodException) {
                Log.e(TAG, "Cannot find method annotated with @OnActivityResult(requestCode = "
                        + requestCode + ", resultCode = " + resultCode + ")");
            } else {
                Log.e(TAG, "hook: ", e);
            }
        }
    }
}
