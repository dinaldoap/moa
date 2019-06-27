package example;

public enum ClassifierEnum {
    OZA_BAG_ASHT("oza_bag_asht", "meta.OzaBagASHT -u"), //
    NAIVE_BAYES("naive_bayes", "bayes.NaiveBayes");

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
