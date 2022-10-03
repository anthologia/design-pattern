package GoF._01_creational_patterns._01_singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringBeanTest {

    @Test
    @DisplayName("스프링 빈의 싱글톤 스코프")
    public void singleton_bean() throws Exception {
        // given
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);

        // when
        String hello1 = ac.getBean("hello", String.class);
        String hello2 = ac.getBean("hello", String.class);

        // then
        assertThat(hello1).isEqualTo(hello2);
    }
}
