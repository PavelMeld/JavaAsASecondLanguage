package io.github.javaasasecondlanguage.homework02.webserver;

import io.github.javaasasecondlanguage.homework02.di.Injector;

public class MyLogger implements Logger {
    MessageSeverity currentSeverity = Injector.inject(MessageSeverity.class, "currentLogLevel");

    @Override
    public void info(String msg) {
        info(MessageSeverity.NONE, msg);
    }

    @Override
    public void info(MessageSeverity messageSeverity, String msg) {
        if (messageSeverity.lessThan(currentSeverity)) {
            return;
        }

        System.out.println(msg);

    }
}
