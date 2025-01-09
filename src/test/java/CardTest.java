
import Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardTest {

    private Card card;

    @BeforeEach
    public void setUp() {
        card = new Card("exampleWord", "exampleUrl");
    }

    @Test
    public void testConstructorAndGetters() {
        assertNotNull(card, "La carte ne doit pas être null.");
        assertEquals("exampleWord", card.getWord(), "Le mot de la carte doit être 'exampleWord'.");
        assertEquals("exampleUrl", card.getUrlImage(), "L'URL de l'image doit être 'exampleUrl'.");
        assertEquals(0, card.getCouleur(), "La couleur par défaut doit être 0 (white).");
        assertFalse(card.isSelected(), "La carte ne doit pas être sélectionnée par défaut.");
    }

    @Test
    public void testSetWord() {
        card.setWord("newWord");
        assertEquals("newWord", card.getWord(), "Le mot de la carte doit être 'newWord'.");
    }

    @Test
    public void testSetUrlImage() {
        card.setUrlImage("newUrl");
        assertEquals("newUrl", card.getUrlImage(), "L'URL de l'image doit être 'newUrl'.");
    }

    @Test
    public void testSetCouleur() {
        card.setCouleur(2); // 2 pour red
        assertEquals(2, card.getCouleur(), "La couleur de la carte doit être 2 (red).");
    }

    @Test
    public void testSetSelected() {
        card.setSelected(true);
        assertTrue(card.isSelected(), "La carte doit être sélectionnée.");
    }

    @Test
    public void testCouleurUpdate() {
        card.setCouleur(1); // Blue
        assertEquals(1, card.getCouleur(), "La couleur de la carte doit être 1 (blue).");

        card.setCouleur(3); // Black
        assertEquals(3, card.getCouleur(), "La couleur de la carte doit être 3 (black).");
    }
}