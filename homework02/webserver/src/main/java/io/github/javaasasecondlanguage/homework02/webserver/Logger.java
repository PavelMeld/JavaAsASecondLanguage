package io.github.javaasasecondlanguage.homework02.webserver;

public interface Logger {
    public enum MessageSeverity {
        NONE(0),
        URGENT(1),
        NONURGENT(2),
        DEBUG(3);

        Integer value;

        MessageSeverity(Integer value) {
            this.value = value;
        }

        boolean lessThan(MessageSeverity other) {
            return this.value > other.value;
        }
    }

    void info(String msg);

    void info(MessageSeverity severity, String msg);
}
