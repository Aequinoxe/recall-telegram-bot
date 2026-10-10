package bot.application.repository;

import bot.domain.Review;

public interface ReviewRepository {

    Review save(Review review);
}