package bot;

public enum Command {
    START("/start"),
    HELP("/help"),
    GET_CODE("/getcode"),
    GET_LAST("/getlast"),
    GET_WEEK("/getweek"),
    GET_MONTH("/getweek"),
    UNKNOWN_COMMAND("");

    Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name;
}
