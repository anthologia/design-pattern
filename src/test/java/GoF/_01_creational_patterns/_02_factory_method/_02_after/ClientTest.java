package GoF._01_creational_patterns._02_factory_method._02_after;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    @Test
    void shipCreationTest() {
        String shipName1 = "Whiteship";
        Ship whiteship = new WhiteShipFactory().orderShip(shipName1, "keesun@mail.com");

        String shipName2 = "Blackship";
        Ship blackship = new BlackShipFactory().orderShip(shipName2, "keesun@mail.com");

        assertThat(whiteship.getName()).isEqualTo("Whiteship");
        assertThat(whiteship.getColor()).isEqualTo("white");

        assertThat(blackship.getName()).isEqualTo("Blackship");
        assertThat(blackship.getColor()).isEqualTo("black");
    }
}
