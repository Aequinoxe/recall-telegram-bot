package bot.application.repository;

import bot.domain.Card;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class InMemoryCardRepository implements CardRepository {

    private final Map<Long, Card> cards = new HashMap<>();

    public InMemoryCardRepository() {
        cards.put(1L, new Card(1L, 1L, "apple", "яблоко"));
        cards.put(2L, new Card(2L, 1L, "book", "книга"));
        cards.put(3L, new Card(3L, 1L, "house", "дом"));
    }

    @Override
    public List<Card> findByDeckId(long deckId) {
        return cards.values().stream()
                .filter(card -> card.getDeckId() == deckId)
                .toList();
    }
}