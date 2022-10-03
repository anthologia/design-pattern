package GoF._01_creational_patterns._01_singleton;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

class EnumTest {

    @Test
    @DisplayName("enum 사용시 Reflection 방지")
    public void enumBlocksReflection() throws Exception {
        Constructor<?>[] declaredConstructors = EnumSettings.class.getDeclaredConstructors();

        // CASE 1
        for (Constructor<?> constructor : declaredConstructors) {
            constructor.setAccessible(true);
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                constructor.newInstance("INSTANCE");
            });
        }

        // CASE 2
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            for (Constructor<?> constructor : declaredConstructors) {
                constructor.setAccessible(true);
                constructor.newInstance("INSTANCE");
            }
        });
    }

    @Test
    @DisplayName("Enum의 직렬화 & 역직렬화시 싱글톤 유지")
    public void enum_serializable_keep_singleton() throws Exception {
        // given
        EnumSettings settings1 = EnumSettings.INSTANCE;
        EnumSettings settings2 = null;

        // when
        try (
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream("enum_settings.obj"))) {
            out.writeObject(settings1);
        }

        try (
            ObjectInput in = new ObjectInputStream(new FileInputStream("enum_settings.obj"))){
            settings2 = (EnumSettings) in.readObject();
        }

        // then
        assertThat(settings1).isEqualTo(settings2);
    }
}

