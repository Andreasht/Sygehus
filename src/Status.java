public enum Status {
    LEDIG ("Ledig"),
    OPTAGET ("\\u001b[31m Optaget"),
    FRAVÆR ("Fraværende"),
    PAUSE ("Pause");

    private final String text;

    Status(String t) {
        text = t;
    }

    public String getText() {
        return text;
    }

}
