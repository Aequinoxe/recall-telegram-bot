package bot.domain;

import java.time.Instant;

public class User {
    private final long id;
    private String name;
    private Instant lastReviewAt;
    private int streakDays;
    private boolean notificationsEnabled;

    public User(long id, String name) {
        this.id = id;
        this.name = name;
        this.notificationsEnabled = true;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getLastReviewAt() {
        return lastReviewAt;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastReviewAt(Instant lastReviewAt) {
        this.lastReviewAt = lastReviewAt;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
