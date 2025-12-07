package enums;

public enum ExpertiseArea {
    DESIGN("Design"),
    DB("Db"),
    FULLSTACK("FullStack"),
    BACKEND("Backend"),
    FRONTEND("Frontend"),
    DEVOPS("DevOps");

    private final String name;
    ExpertiseArea(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public static ExpertiseArea getType(String name) {
        for (ExpertiseArea expertise : ExpertiseArea.values()) {
            if (expertise.name.equalsIgnoreCase(name)) {
                return expertise;
            }
        }
        System.out.println("no expertise found");
        return null;
    }
}
