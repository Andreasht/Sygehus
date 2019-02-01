public enum Status {
    LEDIG ("Ledig"),
    OPT ("Optaget"),
    FRAV ("Fraværende"),
    PAUSE ("Pause");

    private final String text;

    Status(String t) {
        text = t;
    }

    public String getText() {
        return text;
    }

}
