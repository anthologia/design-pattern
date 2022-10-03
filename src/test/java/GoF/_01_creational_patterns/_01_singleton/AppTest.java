package GoF._01_creational_patterns._01_singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    @Test
    @DisplayName("getInstance() 싱글톤 테스트")
    public void getInstance_gives_singleton() throws Exception {
        Settings settings1 = Settings.getInstance();
        Settings settings2 = Settings.getInstance();

        assertThat(settings1).isEqualTo(settings2);
        assertThat(settings1).isEqualTo(Settings.getInstance());
    }

    @Test
    @DisplayName("리플렉션을 사용한 싱글톤 깨트리기")
    public void reflection_breaks_singleton() throws Exception {
        // given
        Settings settings1 = Settings.getInstance();

        // when
        Constructor<Settings> constructor = Settings.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Settings settings2 = constructor.newInstance();

        // then
        assertThat(settings1).isNotEqualTo(settings2);
    }


    @Test
    @DisplayName("직렬화를 사용한 싱글톤 깨트리기 - readResolve X")
    public void given_SettingsHasNoReadResolve_serializable_breaks_singleton() throws Exception {
        // given
        SettingsNoReadResolve settings1 = SettingsNoReadResolve.getInstance();
        SettingsNoReadResolve settings2 = null;

        // when
        try (
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream("settings.obj"))) {
            out.writeObject(settings1);
        }

        try (
            ObjectInput in = new ObjectInputStream(new FileInputStream("settings.obj"))){
            settings2 = (SettingsNoReadResolve) in.readObject();
        }

        // then
        assertThat(settings1).isNotEqualTo(settings2);
    }

    @Test
    @DisplayName("readResolve()를 사용하여 싱글톤 유지")
    public void given_SettingsHasReadResolve_serializable_sustain_singleton() throws Exception {
        // given
        Settings settings1 = Settings.getInstance();
        Settings settings2 = null;

        // when
        try (
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream("settings.obj"))) {
            out.writeObject(settings1);
        }

        try (
            ObjectInput in = new ObjectInputStream(new FileInputStream("settings.obj"))){
            settings2 = (Settings) in.readObject();
        }

        // then
        assertThat(settings1).isEqualTo(settings2);
    }

    static class SettingsNoReadResolve implements Serializable {
        private SettingsNoReadResolve() { }

        private static class SettingsHolder {
            private static final SettingsNoReadResolve INSTANCE = new SettingsNoReadResolve();
        }

        public static synchronized SettingsNoReadResolve getInstance() {
            return SettingsNoReadResolve.SettingsHolder.INSTANCE;
        }
    }
}
