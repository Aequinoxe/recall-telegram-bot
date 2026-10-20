package bot.infrastructure.repository;

import bot.application.repository.ReviewRepository;
import bot.domain.Review;

import java.util.ArrayList;
import java.util.List;

public class InMemoryReviewRepository implements ReviewRepository {

    private final List<Review> reviews;

    public InMemoryReviewRepository() {
        this.reviews = new ArrayList<>();
    }

    @Override
    public void save(Review review) {
        reviews.add(review);
    }
}
