package example;

public enum ClassifierEnum {
    NAIVE_BAYES("naive_bayes", "bayes.NaiveBayes"), //
    OZA_BAG_ASHT("oza_bag_asht", "meta.OzaBagASHT -u"), //
    ;

    private String name;
    private String command;

    private ClassifierEnum(String name, String command) {
        this.name = name;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }
}
