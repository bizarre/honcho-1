package com.qrakn.honcho.command;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMeta {

    String[] label();
    String permission() default "";
    String description() default "";
    boolean autoAddSubCommands() default true;
    boolean async() default false;

}
