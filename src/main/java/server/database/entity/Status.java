package server.database.entity;

public enum Status {
    ACTIVE("ACTIVE"),
    NOT_ACTIVE("NOT_ACTIVE"),
    DELETED("DELETED");

    private String title;

    Status(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


}


