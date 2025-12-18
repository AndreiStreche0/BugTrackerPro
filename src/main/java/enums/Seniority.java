package enums;

public enum Seniority {
    JUNIOR("Junior"),
    MID("Mid"),
    SENIOR("Senior");
    private final String name;

    Seniority(final String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    /**
     * @param name string form of seniority
     * @return the seniority type
     */
    public static Seniority getType(final String name) {
        for (Seniority seniority : Seniority.values()) {
            if (seniority.name.equalsIgnoreCase(name)) {
                return seniority;
            }
        }
        System.out.println("no seniority found");

        return null;
    }

}
