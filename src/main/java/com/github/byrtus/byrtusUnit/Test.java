package com.github.byrtus.byrtusUnit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface Test {
    String name() default "";
}
