package com.prinjsystems.asctlib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Every class in the classpath that have this annotation will be identified as a tile by ASCT.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlaceableTile {
    /**
     * Category of the tile. The category is case-insensitive, and should be registered in {@link TileCategoryHolder}
     * at startup.
     */
    String value() default "structural";
}
