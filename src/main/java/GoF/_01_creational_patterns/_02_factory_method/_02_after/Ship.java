package GoF._01_creational_patterns._02_factory_method._02_after;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Ship {

    private String name;
    private String color;
    private String logo;

    @Override
    public String toString() {
        return "Ship{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
