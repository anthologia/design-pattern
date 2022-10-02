package GoF._01_creational_patterns._01_singleton;

public class App {
    public static void main(String[] args) {
        Settings settings = Settings.getInstance();

        System.out.println(settings == Settings.getInstance());
    }
}
