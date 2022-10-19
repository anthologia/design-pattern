package GoF._01_creational_patterns._02_factory_method._02_after;

import GoF._01_creational_patterns._03_abstract_factory._02_after.Anchor;
import GoF._01_creational_patterns._03_abstract_factory._02_after.Wheel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Ship {

    private String name;
    private String color;
    private String logo;
    private Anchor anchor;
    private Wheel wheel;

    @Override
    public String toString() {
        return "Ship{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
