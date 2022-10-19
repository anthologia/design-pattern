package GoF._01_creational_patterns._03_abstract_factory._02_after;

import GoF._01_creational_patterns._02_factory_method._02_after.Ship;
import GoF._01_creational_patterns._02_factory_method._02_after.ShipFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShipInventoryTest {

    @Test
    @DisplayName("추상 팩토리 적용 테스트")
    void abstractMethodTest() {
        ShipFactory shipFactory = new WhiteShipFactory(new WhiteShipPartsProFactory());
        Ship ship = shipFactory.createShip();

        Anchor anchor = ship.getAnchor();
        Wheel wheel = ship.getWheel();

        assertThat(anchor).isInstanceOf(WhiteAnchorPro.class);
        assertThat(wheel).isInstanceOf(WhiteWheelPro.class);
    }
}