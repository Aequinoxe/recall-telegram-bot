package bot.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class CardTest {
    @Test
    @DisplayName("Should successfully create a card and verify its fields via getters")
    void checkCardCreation()
    {
        long id = 1;
        long deckId = 5;
        String front = "Apple";
        String back= "Яблоко";

        Card myCard = new Card(id, deckId, front, back);

     //переписать после доп ген id

        //assertEquals(id, myCard.getId(), "Card id does not match");
        //assertEquals(deckId, myCard.getId(), "Deck id does not match");
        assertEquals(front, myCard.getFront(), "Front side text does not match");
        assertEquals(back, myCard.getBack(), "Back side text does not match");
    }

    @Test
    @DisplayName("Should automatically set the initial state to NEW upon creation")
    void checkDefaultCardState() {
        Card myCard = new Card(1, 5, "Apple", "Яблоко");

        assertEquals(Card.CardState.NEW, myCard.getCardState(), "Initial card state must be NEW");
    }

    @Test
    @DisplayName("Should generate unique IDs for two different cards")
    void checkUniqueIdsForMultipleCards() {

        //посмотреть, как допишем генерацию id и поменять
        Card firstCard = new Card(1L, 5L,"Apple", "Яблоко");
        Card secondCard = new Card(2L, 5L, "Banana", "Банан");

        assertNotEquals(firstCard.getId(), secondCard.getId(), "Each card must have a unique auto-generated ID");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when front text is empty or blank")
    void shouldThrowExceptionWhenFrontIsEmpty() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Card(1L, 10L, "", "Яблоко"); // Empty string
        }, "Should fail because front text is an empty string");

        assertThrows(IllegalArgumentException.class, () -> {
            new Card(1L, 10L, "   ", "Яблоко"); // Only spaces
        }, "Should fail because front text contains only spaces");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when back text is null")
    void shouldThrowExceptionWhenBackIsNull() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Card(1L, 10L, "Apple", null); // Null value
        }, "Should fail because back text is null");
    }

}