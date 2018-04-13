package bot;

public enum Command {
    START("/start"),
    HELP("/help"),
    SILPO_ATB("/silpoatb"),     SILPO_ATB_BUTTON("/Silpo&ATB"),
    KOSHIK_SAMMARKET("/koshiksammarket"),   KOSHIK_SAMMARKET_BUTTON("/Koshik&SAM-Market"),
    EDIT_LAST("/edit"),              EDIT_LAST_BUTTON("/Edit Last"),
    GET_LAST("/getlast"),       GET_LAST_BUTTON("/Get Last"),
    GET_WEEK("/getweek"),       GET_WEEK_BUTTON("/Get Week"),
    GET_MONTH("/getweek"),      GET_MONTH_BUTTON("/Get Month"),
    UNKNOWN_COMMAND("");

    Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name;
}
