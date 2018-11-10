package com.prashant.onactivityresult.annotation;

/**
 * @author Prashant Singh
 */
public class Helpers {
    private static final String GENERATED_FILE_NAME_SUFFIX = "_Hooker";

    public static String getGeneratedMethodName(final int requestCode, final int resultCode) {
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

    public static String getGeneratedFileNameSuffix() {
        return GENERATED_FILE_NAME_SUFFIX;
    }
}
