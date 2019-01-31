public enum Status {
    LEDIG ("Ledig"),
    OPTAGET ("\\u001b[31m Optaget"),
    FRAVAER ("Fraværende"),
    PAUSE ("Pause");

    private final String text;

    Status(String t) {
        text = t;
    }

    public String getText() {
        return text;
    }

}
