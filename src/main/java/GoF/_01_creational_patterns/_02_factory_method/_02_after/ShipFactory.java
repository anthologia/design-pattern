package GoF._01_creational_patterns._02_factory_method._02_after;

public interface ShipFactory {

    default Ship orderShip(String name, String email) {
        validate(name, email);
        prepareFor(name);
        Ship ship = createShip();
        ship.setName(name);
        sendEmailTo(email, ship);
        return ship;
    }

    Ship createShip();

    void sendEmailTo(String email, Ship ship);

    private static void prepareFor(String name) {
        System.out.println(name + " 만들 준비 중");
    }


    private void validate(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("배 이름을 지어주세요.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("연락처를 남겨주세요.");
        }

    }

}
