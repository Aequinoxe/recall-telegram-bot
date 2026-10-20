package bot.infrastructure.repository;

import bot.application.repository.CardRepository;
import bot.domain.Card;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCardRepository implements CardRepository {

    private final List<Card> cards;

    public InMemoryCardRepository(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    @Override
    public List<Card> findByDeckId(long deckId) {
        List<Card> result = new ArrayList<>();

        for (Card card : cards) {
            if (card.getDeckId() == deckId) {
                result.add(card);
            }
        }
        return result;
    }
}
