# 객체 생성 관련 디자인 패턴
## Factory Method pattern
- 구체적으로 어떤 인스턴스를 만들지 서브 클래스가 정하는 것
  - 인스턴스를 생성하는 책임을 인터페이스의 메서드로 감싸는 것
  - 팩토리 메서드를 사용하여 OCP를 만족시킬 수 있다.

---
## 팩토리 메서드 패턴 적용 전
- ShipFactory 클래스
  ``` java
  public class ShipFactory {

      public static Ship orderShip(String name, String email) {
          if (name == null || name.isBlank()) {
              throw new IllegalArgumentException("배 이름을 지어주세요.");
          }
          if (email == null || email.isBlank()) {
              throw new IllegalArgumentException("연락처를 남겨주세요.");
          }

          prepareFor(name);

          Ship ship = new Ship();
          ship.setName(name);

          if (name.equalsIgnoreCase("whiteship")) {
              ship.setLogo("\uD83D\uDEE5️");
          } else if (name.equalsIgnoreCase("blackship")) {
              ship.setLogo("⚓");
          }

          if (name.equalsIgnoreCase("whiteship")) {
              ship.setColor("white");
          } else if (name.equalsIgnoreCase("blackship")) {
              ship.setColor("black");
          }

          sendEmailTo(email, ship);

          return ship;
      }

      private static void sendEmailTo(String email, Ship ship) {
          ...
      }

      private static void prepareFor(String name) {
          ...
      }
  }
  ```

- Ship 클래스
  ``` java
  @Getter @Setter
  public class Ship {
      private String name;
      private String color;
      private String logo;
  }
  ```

- 클라이언트
  ``` java
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
  ```

## 팩토리 메서드 패턴 적용하기
- ShipFactory 인터페이스
  ``` java
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
  
      private static void prepareFor(String name) {
          ...
      }
  
      private void validate(String name, String email) {
          if (name == null || name.isBlank()) {
              throw new IllegalArgumentException("배 이름을 지어주세요.");
          }
          if (email == null || email.isBlank()) {
              throw new IllegalArgumentException("연락처를 남겨주세요.");
          }
  
      }
  
      private static void sendEmailTo(String email, Ship ship) {
          ...
      }
  }
  ```

- WhiteShip 구현체
  ``` java
  public class WhiteShipFactory implements ShipFactory {
  
      @Override
      public Ship createShip() {
          return new WhiteShip();
      }
  }
  ```

- WhiteShip 클래스
  ``` java
  public class WhiteShip extends Ship {
  
      public WhiteShip() {
          setName("whiteship");
          setLogo("\uD83D\uDEE5️");
          setColor("white");
      }
  }
  ```


- 클라이언트
  ``` java
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
  ```

#### Q1. 팩토리 메소드 패턴을 적용했을 때의 장점과 단점은❓
- 장점
  - 새로운 Ship이 추가되어도 ShipFactory의 코드를 수정하지 않아도 된다.
  - 즉, OCP를 지킬 수 있게 된다.
    - Product와 Creator가 느슨한 결합(Loosely Coupling)을 갖기 때문에 가능하다.
- 단점
  - 그만큼 클래스가 많이 생기기 때문에 구조의 복잡성이 증가한다.
  
#### Q2. "확장에 열려있고 변경에 닫혀있는 객체 지향 원칙" OCP란❓
- 다형성을 활용하여 확장에는 열려 있어야 하지만, 변경에는 닫혀 있어야 한다.
- 역할과 구현의 분리를 생각해보자.
  - 역할 인터페이스를 구현한 구현체가 존재하면, 구현체가 확장되어도 클라이언트는 구현을 변경하지 않아도 된다.

#### Q3. OCP를 만족한다면서 위의 코드는 클라이언트 코드가 변경되었다❓
- 맞다. 그래서 외부에서 의존성 주입(DI)을 해주면 클라이언트 코드를 변경하지 않도록 하는 것이 가능하다.

#### Q4. 자바 8에 추가된 default 메소드란❓
- 스터디에서 답변한 내용으로 대체 (인터페이스에서 다이아몬드 문제가 발생하는 경우 - 1)
  - https://github.com/Java-Chip4/StudyingRecord/issues/6
  - 추가적으로, Java 9 이후부터 인터페이스 내에서 private (static) method를 사용할 수 있게 되었다.
  - 그 덕분에 default method 내에 존재하는 중복된 코드를 추출하여 코드 재사용성을 높일 수 있다.

#### Q5. 유니코드와 variant form, variant selector❓
- 테스트 코드를 작성하다가 알 수 없는 테스트 실패를 겪었고, 해당 문제를 블로그에 기록하였다.
  - https://codecpr.tistory.com/49

## 실무에서 쓰이는 팩토리 메서드
- 단순한 팩토리 패턴
  ``` java
  public class SimpleFactory {
  
      public Object createProduct(String name) {
          if (name.equals("whiteship")) {
              return new WhiteShip();
          } else if (name.equals("blackship")) {
              return new BlackShip();
          }
  
          throw new IllegalArgumentException();
      }
  }
  ```

- java.lang.Calendar
  ``` java
  System.out.println(Calendar.getInstance().getClass());
  System.out.println(Calendar.getInstance(Locale.forLanguageTag("th-TH-x-lvariant-TH")).getClass());
  System.out.println(Calendar.getInstance(Locale.forLanguageTag("jp-JP-x-lvariant-JP")).getClass());
  ```

- Spring framework의 BeanFactory
  ``` java
  BeanFactory xmlFactory = new ClassPathXmlApplicationContext("config.xml");
  String hello = xmlFactory.getBean("hello", String.class);
  
  BeanFactory javaFactory = new AnnotationConfigApplicationContext();
  String hi = javaFactory.getBean("hi", String.class);
  ```


#### 정리 ❗️
- Creator와 Product에 각각 계층 구조가 있어서 구체적인 Factory 안에서 구체적인 Product를 만들어낸다는 개념이 중요하다 
