package enums;

public enum Seniority {
    JUNIOR("Junior"),
    MID("Mid"),
    SENIOR("Senior");
    private final String name;

    Seniority(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public static Seniority getType(String name) {
        for (Seniority seniority : Seniority.values()) {
            if (seniority.name.equalsIgnoreCase(name)) {
                return seniority;
            }
        }
        System.out.println("no seniority found");

        return null;
    }

}
