package GoF._01_creational_patterns._03_abstract_factory._03_java;

import GoF._01_creational_patterns._02_factory_method._02_after.Ship;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FactoryBeanExample {

    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(FactoryBeanConfig.class);
        Ship whiteship = ac.getBean(Ship.class);
        System.out.println(whiteship.getName());
    }
}
