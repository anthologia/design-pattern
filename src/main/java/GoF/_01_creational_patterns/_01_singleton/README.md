# 객체 생성 관련 디자인 패턴
## Singleton pattern
- 인스턴스를 오직 한 개만 제공하는 클래스
  - Ex. 게임의 설정 화면은 딱 하나만 존재해야 한다.

---
## 싱글톤 패턴 적용하기
#### Q1. 생성자를 private으로 만든 이유❓
새로운 객체의 생성을 제한하기 위해서

#### Q2. `getInstance()` 메소드를 static으로 선언한 이유?❓
생성자를 private으로 제한했기 때문에 싱글톤 클래스 밖에서는 객체의 생성이 제한된다. 
그러므로, 전역적인 범위에서 객체를 생성하지 않고도 접근하기 위해서 static 으로 선언하였다.    

- 참고 : `getInstance()`를 호출시, 인스턴스를 생성하도록 코드를 작성하였다. 즉, eager initailzation X.

#### Q3. getInstance()가 멀티쓰레드 환경에서 안전하지 않은 이유?❓
나의 생각 : Race-condition이 발생했을 때, 그걸 막을 방법이 없어서?  
백기선님 생각 : 나의 생각이 맞다.  

해결을 위해서는
1. **`sychronized` 키워드 사용**
   ``` java
   public class Settings {
       private static Settings instance;
 
       private Settings() { }
   
       public static synchronized Settings getInstance() {
           if (instance == null) {
               instance = new Settings();
           }
   
           return instance;
       }
   }
   
   ```
   - 장점
     - thead-safe
   - 단점
     - Lock으로 인한 성능 저하 예상

   #### Q4. 자바의 동기화 블럭 처리 방법은?❓
   자바에서 동기화는 객체에 대한 동기화로 이뤄진다. 한 객체에 대한 모든 동기화 블록은 해당 시점에 오직 한 쓰레드만이 블록 안으로 접근할 수 있도록 한다. 블록에 접근을 시도하는 다른 쓰레드들은 블록 안의 쓰레드가 실행을 마치고 블록을 벗어날 때까지 블록(blocked) 상태가 된다.
   - 참고 : https://parkcheolu.tistory.com/15

   #### Q5. `getInstance()` 메소드 동기화시 사용하는 락(lock)은 인스턴스의 락인가 클래스의 락인가? 그 이유는?❓
   자바는 객체에 의한 동기화이므로, static한 `getInstance()` 메서드는 클래스 락이라고 생각이 든다.
   JVM 안에서 클래스 객체는 클래스당 하나만 존재할 수 있기 때문이다.


2. **eager initailzation 사용**
   ``` java
   public class Settings {
       private static final Settings INSTANCE = new Settings();
   
       private Settings() { }
   
       public static Settings getInstance() {
           return INSTANCE;
       }
   }
   
   ```
   - 장점
     - 성능에 조금 더 신경을 써보고 싶다면
     - 객체 생성 비용이 크지 않아, 나중에 만들어도 된다면
   - 단점
     - 객체 생성 비용이 큰 경우, 사용되지 않으면 낭비
   - 이 방법이 thead-safe한 이유❓
     - 여러 thead가 경쟁하더라도, 미리 만들어진 인스턴스를 return하기만 하면 되기 때문이다

   #### Q6. 이른 초기화가 단점이 될 수도 있는 이유❓
   만약, 객체 생성 비용이 큰 경우, 객체를 미리 생성해뒀는데 사용되지 않는 상황
   
   #### Q7. 만약에 생성자에서 checked 예외를 던진다면 이 코드를 어떻게 변경해야 할까요❓
   - 생성자 내부에서 try-catch를 적용시키거나
     ``` java
     public class Settings {
         private static final Settings INSTANCE = new Settings();
     
         private Settings() {
             try {
                 throw new FileNotFoundException();
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }
         }
        ...
     }
     ```
   - 생성자는 예외를 던지고, 생성자를 호출한 곳에서 try-catch를 감싸야 한다. 대신 이 경우에는 INSTANCE에 final을 적용할 수 없다.
     ``` java
     public class Settings {
         private static Settings INSTANCE;
     
         static {
             try {
                 INSTANCE = new Settings();
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }
         }
     
         private Settings() throws FileNotFoundException {
             throw new FileNotFoundException();
         }
         ...
     }
     ```


3. **double checked locking 사용**
   ``` java
   public class Settings {
       private static volatile Settings instance;
   
       private Settings() { }
   
       public static Settings getInstance() {
           if (instance == null) {
               synchronized (Settings.class) {
                   if (instance == null) {
                       instance = new Settings();
                   }
               }
           }
   
           return instance;
       }
   }
   ```
   - 장점
     - instance를 필요로 하는 시점에 생성 가능
     - 멀티 스레드가 빈번하게 일어나는 경우를 대비하기 때문에 1번 방법보다는 성능에 유리
   - 단점
     - 복잡함
     - JDK 1.5 이상에서만 동작

   #### Q8. double check locking이라고 부르는 이유❓
   `getInstance()` 메서드는 `synchronized`하지 않기 때문에 우선 여러 쓰레드가 접근하더라도 성능의 저하가 일어나지 않는다. 그러나 그 안에서, 새로운 객체를 생성해야 하는 경우에는 thead-safe 하게 만들기 위해 `synchronized` 블럭을 추가로 구성하였으므로 double check locking이라고 부른다.   
   
   #### Q9. instacne 변수는 어떻게 정의해야 하는가? 그 이유는❓
   `volatile`하게 정의해야 한다. 그 이유는 `volatile` 키워드가 변수를 main memory에 저장하겠다는 뜻이기 때문이다. 그 덕분에 캐시 불일치 이슈를 방지 할 수 있다.
   `volatile`을 사용하지 않은 변수는 성능향상을 위해 그 값을 CPU 캐시에 저장한다. 이 경우, 쓰레드가 변수값을 읽어올 때 각각의 CPU 캐시에서 가져오기 때문에 값이 달라 값의 불일치가 발생한다. 추가적으로, 자바 메모리 모델은 부분적으로 초기화 된 객체의 발행을 허용하기 때문에 파악하기 어려운 버그를 만들어 낼 수도 있다.
   - 참고
     - https://letyarch.blogspot.com/2019/04/singleton-synchronized_8.html
     - https://rkdals213.tistory.com/39

   - 추가적으로 공부할 내용
     - volatile : https://nesoy.github.io/articles/2018-06/Java-volatile
     - double-checked-locking broken : https://www.cs.cornell.edu/courses/cs6120/2019fa/blog/double-checked-locking/

5. **static inner 클래스 사용**
   ``` java
   public class Settings {
       private Settings() { }
   
       private static class SettingsHolder {
           private static final Settings INSTANCE = new Settings();
       }
   
       public static synchronized Settings getInstance() {
           return SettingsHolder.INSTANCE;
       }
   }
   ```
   - 장점
     - thread-safe
     - `getInstance()` 호출 시점에 객체 생성 가능

   #### Q10. 이 방법은 static final를 썼는데도 왜 지연 초기화 (lazy initialization)라고 볼 수 있는가❓
   SettingsHolder 클래스가 로딩되는 시점은 getInstance()를 호출할 때 로딩되기 때문에 lazy-initialization이라고 볼 수 있다.


## 싱글톤 패턴 구현 깨트리기
### 리플렉션을 사용한 경우
``` java
    @Test
    @DisplayName("리플렉션을 사용한 싱글톤 깨트리기")
    public void reflection_breaks_singleton() throws Exception {
        // given
        Settings settings1 = Settings.getInstance();

        // when
        Constructor<Settings> constructor = Settings.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Settings settings2 = constructor.newInstance();

        // then
        assertThat(settings1).isNotEqualTo(settings2);
    }
```
#### Q11. 리플렉션에 대해 설명하세요❓ 
구체적인 클래스 타입을 알지 못해도, 해단 클래스의 정보(메서드, 타입, 변수, etc)에 접근할 수 있도록 해주는 Java API.
 
``` java
class ReflectionTest {

    @Test
    @DisplayName("리플렉션 aged() 테스트")
    public void reflectionTest() throws Exception {
        // given
        Person obj = new Person("Jooyoung", 26);
        Class<Person> personClass = Person.class;
        Method aged = personClass.getMethod("aged");

        // when
        aged.invoke(obj, null);

        // then
        Method getAge = personClass.getMethod("getAge");
        int age = (int) getAge.invoke(obj, null);
        assertThat(age).isEqualTo(27);
    }

    static class Person {
        private final String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void aged() {
            age++;
        }

        public int getAge() {
            return age;
        }
    }
}
```


- https://tecoble.techcourse.co.kr/post/2020-07-16-reflection-api/

#### Q12. `setAccessible(true)`를 사용하는 이유는❓
기본적으로 타 클래스에서 private한 필드 혹은 메서드에 접근하는 것은 불가능하다.  
``` java
    @Test
    @DisplayName("리플렉션 private 필드 접근 : setAccessible() 적용 X")
    public void reflection_private_field_no_setAccessible() throws Exception {
        // given
        Person obj = new Person("Jooyoung", 26);
        Class<Person> personClass = Person.class;

        // when
        Field field = personClass.getDeclaredField("name");

        // then
        Assertions.assertThrows(IllegalAccessException.class, () -> {
            field.set(obj, "test");
        });
    }
```
그러나 `setAccessible(true)`을 적용함으로써, private한 필드에 접근하는 것이 가능해진다.
``` java
    @Test
    @DisplayName("리플렉션 private 필드 접근 : setAccessible() 적용 O")
    public void reflection_private_field_with_setAccessible() throws Exception {
        // given
        Person obj = new Person("Jooyoung", 26);
        Class<Person> personClass = Person.class;

        // when
        Field field = personClass.getDeclaredField("name");
        field.setAccessible(true);
        field.set(obj, "test");
        String name = (String) field.get(obj);

        // then
        assertThat(name).isEqualTo("test");
    }
```