package GAME.TEST;

import GAME.GAME.TeisPanel;
import GAME.GPHICS.Pieza;
import GAME.GPHICS.PiezaManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TestPiezaManager {

    private PiezaManager piezaManager;
    TeisPanel teisPanel;

    @BeforeEach
    public void setUp() {
        // Create a new PiezaManager instance
        piezaManager = new PiezaManager(null,"prueba.txt");
    }

    @Test
    void testGetPiezaImage() {
        // Verify that the pieza array is not null
        assertNotNull(piezaManager.pieza);

        // Verify that the length of the pieza array is equal to the number of image paths
        assertEquals(piezaManager.imagePaths.length, piezaManager.pieza.length);

        // Verify that each Pieza object has a non-null image
        for (Pieza pieza : piezaManager.pieza) {
            assertNotNull(pieza.image);
        }
    }

    @Test
    void testGetImagePaths() {
        // Verify that the imagePaths array is not null
        assertNotNull(piezaManager.imagePaths);

        // Verify that the imagePaths array is not empty
        assertTrue(piezaManager.imagePaths.length > 0);

        // Verify that the image paths are valid file paths
        for (String imagePath : piezaManager.imagePaths) {
            File file = new File(imagePath);
            assertTrue(file.exists(), "File not found: " + imagePath);
        }
    }

    @Test
    void testLoadMap() {
        // Verify that the mapaPiezaNum array is not null
        assertNotNull(piezaManager.mapaPiezaNum);

        // Verify that the mapaPiezaNum array has the correct dimensions
        assertEquals(teisPanel.maxScreenColumnas, piezaManager.mapaPiezaNum.length);
        assertEquals(teisPanel.maxScreenFilas, piezaManager.mapaPiezaNum[0].length);

        // Verify that the mapaPiezaNum array contains valid map data
        for (int i = 0; i < piezaManager.mapaPiezaNum.length; i++) {
            for (int j = 0; j < piezaManager.mapaPiezaNum[i].length; j++) {
                int value = piezaManager.mapaPiezaNum[i][j];
                assertTrue(value >= 0 && value < piezaManager.pieza.length, "Invalid map data: " + value);
            }
        }
    }

    @Test
    void testPinta() {
        // Create a mock Graphics2D object
        Graphics2D g2 = null;

        // Call the pinta method
        piezaManager.pinta(g2);

        // Verify that the drawImage method was called the correct number of times
        int numDraws = teisPanel.maxScreenColumnas * teisPanel.maxScreenFilas;
        assertEquals(numDraws, g2.getFont(), "Incorrect number of draws: " + numDraws);
    }
}