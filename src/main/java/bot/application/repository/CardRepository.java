package bot.application.repository;

import bot.domain.Card;

import java.util.List;

public interface CardRepository {

    List<Card> findByDeckId(long deckId);
}
