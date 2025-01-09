
import linguacrypt.model.game.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {

    private Grid grid;
    private int size;
    @BeforeEach
    public void setUp() {
        size = 5;
        grid = new Grid(size, null, size);
    }

    @Test
    public void testGridInitialization() {
        assertNotNull(grid.getGrid(), "La grille ne doit pas être nulle.");
        assertEquals(size, grid.getGrid().length, "La grille doit avoir une taille de "+ size +"x"+size+".");
        assertEquals(size, grid.getGrid()[0].length, "La grille doit avoir une taille de " + size +"x"+size+".");
    }

    @Test
    public void testGridColorAssignment() {
        grid.initGrid(0);

        int white = 0;
        int blue = 0;
        int red = 0;
        int black = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int color = grid.getCard(i, j).getCouleur();
                switch (color) {
                    case 0: white++; break;
                    case 1: blue++; break;
                    case 2: red++; break;
                    case 3: black++; break;
                    default: fail("Couleur inconnue: " + color); break;
                }
            }
        }

        assertTrue(white >= 0, "Il doit y avoir un nombre de cartes blanches valide.");
        assertTrue(blue <= size*size/3+1, "Le nombre de cartes bleues ne doit pas dépasser " + size*size/3+1 +".");
        assertTrue(red <= size*size/3+1, "Le nombre de cartes rouges ne doit pas dépasser "+ size*size/3+1 +".");
        assertTrue(black == 1, "Il doit y avoir exactement 1 carte noire.");
    }

    // @Test
    // public void testGridWordsAssignment() {
    //     grid.initGrid(0);
    //     for (int i = 0; i < size; i++) {
    //         for (int j = 0; j < size; j++) {
    //             assertNotNull(grid.getCard(i, j).getWord(), "Le mot de la carte à (" + i + ", " + j + ") ne doit pas être nul.");
    //             assertFalse(grid.getCard(i, j).getWord().isEmpty(), "Le mot de la carte à (" + i + ", " + j + ") ne doit pas être vide.");
    //         }
    //     }
    // }

    @Test
    public void testCardColorLimits() {
        grid.initGrid(1);

        int totalCards = 0;
        int white = 0;
        int blue = 0;
        int red = 0;
        int black = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                totalCards++;
                int color = grid.getCard(i, j).getCouleur();
                if (color == 0) {
                    white++;
                } else if (color == 1) {
                    blue++;
                } else if (color == 2) {
                    red++;
                } else if (color == 3) {
                    black++;
                }
            }
        }

        assertEquals(size*size, totalCards, "Le nombre total de cartes doit être égal à " + size +"x"+size+".");
        assertTrue(white >= 0, "Le nombre de cartes blanches doit être valide.");
        assertTrue(blue <= size*size/3+1, "Le nombre de cartes bleues ne doit pas dépasser "+ size*size/3+1 +".");
        assertTrue(red <= size*size/3+1, "Le nombre de cartes rouges ne doit pas dépasser "+ size*size/3+1 +".");
        assertTrue(black == 1, "Le nombre de cartes noires doit être exactement 1.");
    }
}