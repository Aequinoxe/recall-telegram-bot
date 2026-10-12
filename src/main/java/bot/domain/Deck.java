package bot.domain;

public class Deck {
    private final long id;
    private String name;
    private final long userId;

    public Deck(long id, String name, long userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
