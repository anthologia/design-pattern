package GoF._01_creational_patterns._01_singleton;

public enum EnumSettings {
    INSTANCE;

    private EnumSettings() { }

    private Integer number;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
