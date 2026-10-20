package bot.infrastructure.repository;

import bot.application.repository.DeckRepository;
import bot.domain.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryDeckRepository implements DeckRepository {

    private final List<Deck> decks;

    public InMemoryDeckRepository(List<Deck> decks) {
     this.decks = new ArrayList<>(decks);
    }

    @Override
    public Optional<Deck> findById(long id) {

        for (Deck deck : decks) {
            if (deck.getId() == id) {
                return Optional.of(deck);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Deck> findByUserId(long userId) {
        List<Deck> result = new ArrayList<>();

        for (Deck deck : decks) {
            if (deck.getUserId() == userId) {
                result.add(deck);
            }
        }

        return result;
    }
}
