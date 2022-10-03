package GoF._01_creational_patterns._01_singleton;

import java.io.Serializable;

public class Settings implements Serializable {
    private Settings() { }

    private static class SettingsHolder {
        private static final Settings INSTANCE = new Settings();
    }

    public static synchronized Settings getInstance() {
        return SettingsHolder.INSTANCE;
    }

    protected Object readResolve() {
        return getInstance();
    }
}
