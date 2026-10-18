package bot.application.repository;

import bot.domain.Review;

public interface ReviewRepository {

    void save(Review review);
}