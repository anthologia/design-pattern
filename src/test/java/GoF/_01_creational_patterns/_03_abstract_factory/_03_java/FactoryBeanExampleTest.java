package GoF._01_creational_patterns._03_abstract_factory._03_java;

import GoF._01_creational_patterns._02_factory_method._02_after.Ship;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class FactoryBeanExampleTest {

    @Test
    @DisplayName("어노테이션 기반 ac에 적용된 추상 팩토리 패턴")
    void test() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(FactoryBeanConfig.class);
        Ship whiteship = ac.getBean(Ship.class);

        Assertions.assertThat(whiteship.getName()).isEqualTo("whiteship");
    }
}