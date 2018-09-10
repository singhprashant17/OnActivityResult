package com.prashant.onactivityresult.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnActivityResult {
    String GENERATED_FILE_NAME_SUFFIX = "_Hooker";

    int requestCode();

    int resultCode();
}
