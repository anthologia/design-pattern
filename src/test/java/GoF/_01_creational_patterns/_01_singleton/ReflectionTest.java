package GoF._01_creational_patterns._01_singleton;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    @Test
    @DisplayName("리플렉션 aged() 테스트")
    public void reflection_aged() throws Exception {
        // given
        Person obj = new Person("Jooyoung", 26);
        Class<Person> personClass = Person.class;
        Method aged = personClass.getMethod("aged");

        // when
        aged.invoke(obj, null);

        // then
        Method getAge = personClass.getMethod("getAge");
        int age = (int) getAge.invoke(obj, null);
        assertThat(age).isEqualTo(27);
    }

    @Test
    @DisplayName("리플렉션 private 필드 접근 : setAccessible() 적용 X")
    public void reflection_private_field_no_setAccessible() throws Exception {
        // given
        Person obj = new Person("Jooyoung", 26);
        Class<Person> personClass = Person.class;

        // when
        Field field = personClass.getDeclaredField("name");

        // then
        Assertions.assertThrows(IllegalAccessException.class, () -> {
            field.set(obj, "test");
        });
    }

    @Test
    @DisplayName("리플렉션 private 필드 접근 : setAccessible() 적용 O")
    public void reflection_private_field_with_setAccessible() throws Exception {
        // given
        Person obj = new Person("Jooyoung", 26);
        Class<Person> personClass = Person.class;

        // when
        Field field = personClass.getDeclaredField("name");
        field.setAccessible(true);
        field.set(obj, "test");
        String name = (String) field.get(obj);

        // then
        assertThat(name).isEqualTo("test");
    }

    static class Person {
        private final String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void aged() {
            age++;
        }

        public int getAge() {
            return age;
        }
    }
}
