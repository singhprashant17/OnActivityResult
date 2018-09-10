package com.prashant.onactivityresult;

import android.content.Intent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static com.prashant.onactivityresult.annotation.OnActivityResult.GENERATED_FILE_NAME_SUFFIX;

public class ActivityResults {
    private static final String TAG = ActivityResults.class.getSimpleName();

    public static void hook(final Object object, final int requestCode, final int resultCode, final Intent data) {
        try {
            Class<?> clazz = Class.forName(object.getClass().getCanonicalName() + GENERATED_FILE_NAME_SUFFIX);
            final Constructor<?> constructor = clazz.getConstructor(object.getClass());
            final Object hookerObj = constructor.newInstance(object);

            final StringBuilder methodName = new StringBuilder("requestCode_");
            if (requestCode < 0) {
                methodName.append("neg");
            }
            methodName.append(Math.abs(requestCode)).append("_resultCode_");
            if (resultCode < 0) {
                methodName.append("neg");
            }
            methodName.append(Math.abs(resultCode));

            final Method declaredMethod = hookerObj.getClass().getDeclaredMethod(methodName.toString(), Intent.class);
            declaredMethod.invoke(hookerObj, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
