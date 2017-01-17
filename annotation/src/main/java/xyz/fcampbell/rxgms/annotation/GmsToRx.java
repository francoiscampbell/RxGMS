package xyz.fcampbell.rxgms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface GmsToRx {
    Class<?> gmsInterface();

    Class<?> optionsClass();
}
