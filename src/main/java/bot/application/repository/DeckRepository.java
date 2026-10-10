package bot.application.repository;

import bot.domain.Deck;

import java.util.List;
import java.util.Optional;

public interface DeckRepository {

    Optional<Deck> findById(long id);

    List<Deck> findByUserId(long userId);
}
