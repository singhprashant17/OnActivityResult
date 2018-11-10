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

    /**
     * Method to be invoked (generally from {@link android.app.Activity#onActivityResult(int, int, Intent)}). This method, supplied with the
     * required data, resolves the resultCode and requestCode to appropriate annotated method and invokes the method.
     *
     * @param target      The class which has the annotated method defined in it. ({@link android.app.Activity} of
     *                    {@link android.app.Fragment})
     * @param requestCode the requestCode from the activity result
     * @param resultCode  the resultCode from activity result
     * @param data        the {@link Intent} data returned from the activity result
     */
    public static void hook(final Object target, final int requestCode, final int resultCode, final Intent data) {
        try {
            // gget the class object for the generated hooker class
            Class<?> clazz = Class.forName(target.getClass().getName() + getGeneratedFileNameSuffix());

            // check the cache if the hooker object was previously created
            Object hookerObj = CACHE.get(target);
            if (hookerObj == null) {
                // create new hooker object instance
                final Constructor<?> constructor = clazz.getConstructor(target.getClass());
                hookerObj = constructor.newInstance(target);
                CACHE.put(target, hookerObj);
            }

            final String methodName = getGeneratedMethodName(requestCode, resultCode);

            // invoke method using java reflection
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
