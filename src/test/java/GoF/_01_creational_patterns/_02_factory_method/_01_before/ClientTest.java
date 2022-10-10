package GoF._01_creational_patterns._02_factory_method._01_before;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClientTest {
    @Test
    void shipCreationTest() {
        String shipName1 = "Whiteship";
        Ship whiteship = ShipFactory.orderShip(shipName1, "keesun@mail.com");

        String shipName2 = "Blackship";
        Ship blackship = ShipFactory.orderShip(shipName2, "keesun@mail.com");

        assertThat(whiteship.getName()).isEqualTo("Whiteship");
        assertThat(whiteship.getColor()).isEqualTo("white");

        assertThat(blackship.getName()).isEqualTo("Blackship");
        assertThat(blackship.getColor()).isEqualTo("black");
    }
}
