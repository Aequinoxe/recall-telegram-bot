package bot.application;

public class IdGenerator {

    private long nextId = 1;

    public long getNextId() {
        return nextId++;
    }
}
