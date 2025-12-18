package enums;

public enum ExpertiseArea {
    DESIGN("Design"),
    DB("Db"),
    FULLSTACK("FullStack"),
    BACKEND("Backend"),
    FRONTEND("Frontend"),
    DEVOPS("DevOps");

    private final String name;
    ExpertiseArea(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @param name string form of expertise
     * @return the expertise type
     */
    public static ExpertiseArea getType(final String name) {
        for (ExpertiseArea expertise : ExpertiseArea.values()) {
            if (expertise.name.equalsIgnoreCase(name)) {
                return expertise;
            }
        }
        System.out.println("no expertise found");
        return null;
    }
}
