package com.worksit.app.commons.switchknife.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SKYNET-DEV01 on 26/07/2017.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindOnEditorAction {
    int[] value();
}
