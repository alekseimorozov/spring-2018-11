package ru.otus.training.alekseimorozov;

import org.junit.BeforeClass;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CommonRules {
    private static AnnotationConfigApplicationContext context;

    @BeforeClass
    public static void init() {
        context = new AnnotationConfigApplicationContext(Config.class);
    }

    protected static AnnotationConfigApplicationContext getContext() {
        return context;
    }
}
