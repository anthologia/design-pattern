# 객체 생성 관련 디자인 패턴
## Abstract factory pattern
- 서로 관련있는 여러 객체를 만들어주는 인터페이스
- 구체적인 factory에서 구체적인 instance를 만드는 것은 factory method와 비슷하다. 그러나 클라이언트에 초점이 맞춰져 있다는 점이 다르다.
  - 추상 팩토리에서 어떤 구체 클래스의 인스턴스(concrete product)를 사용하는지 감출 수 있다.

---
## 추상 팩토리 패턴 적용 전
아래와 같은 ShipFactory는 사용할 부품(Anchor, Wheel)이 바뀌면, 코드에 변경이 이뤄져야 한다.


``` java
public class WhiteShipFactory {

    @Override
    public Ship createShip() {
        Ship ship = new WhiteShip();
        ship.setAnchor(new WhiteAnchor());
        ship.setWheel(new WhiteWheel());
        return ship;
    }
}
```

## 추상 팩토리 패턴 적용 후
구체적인 클래스는 클라이언트에서 주입하여 사용하도록 설계하였다.
``` java
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
```

그 덕분에 WhiteShipFactory 클래스는 OCP를 지킬 수 있게 되었다.

``` java
public class WhiteShipFactory {

    private ShipPartsFactory shipPartsFactory;

    public WhiteShipFactory(ShipPartsFactory shipPartsFactory) {
        this.shipPartsFactory = shipPartsFactory;
    }

    @Override
    public Ship createShip() {
        Ship ship = new WhiteShip();
        ship.setAnchor(shipPartsFactory.createAnchor());
        ship.setWheel(shipPartsFactory.createWheel());
        return ship;
    }
}
```

``` java
public interface ShipPartsFactory {

    Anchor createAnchor();

    Wheel createWheel();
}
```

``` java
public class WhiteShipPartsFactory implements ShipPartsFactory {
    @Override
    public Anchor createAnchor() {
        return new WhiteAnchor();
    }

    @Override
    public Wheel createWheel() {
        return new WhiteWheel();
    }
}
```

``` java
public class WhiteShipPartsProFactory implements ShipPartsFactory {
    @Override
    public Anchor createAnchor() {
        return new WhiteAnchorPro();
    }

    @Override
    public Wheel createWheel() {
        return new WhiteWheelPro();
    }
}
```

#### Q1. 팩토리 메소드 패턴과 추상 팩토리 패턴의 차이점은❓
- 관점의 차이
  - 팩토리 메서드 패턴은 **팩토리를 구현(inheritance)하는 방법**에 초점을 둔다
  - 추상 팩토리 패턴은 **팩토리를 사용(composition)하는 방법**에 초점을 둔다
- 목적의 차이
  - 팩토리 메서드 패턴은 구체적인 객체 생성 과정을 하위 또는 구체적인 클래스로 옮기는 것이 목적이다
  - 추상 팩토리 패턴은 관련있는 여러 객체를 구체적인 클래스에 의존하지 않고 만들 수 있게 해주는 것이 목적이다


## 실무에서 쓰이는 추상 팩토리 패턴
Spring에서 주로 쓰이는 ApplicationContext 역시 추상 팩토리 패턴이 적용된 형태이다.

``` java
@Test
@DisplayName("어노테이션 기반 ac에 적용된 추상 팩토리 패턴")
void test() {
    ApplicationContext ac = new AnnotationConfigApplicationContext(FactoryBeanConfig.class);
    Ship whiteship = ac.getBean(Ship.class);

    Assertions.assertThat(whiteship.getName()).isEqualTo("whiteship");
}
```
