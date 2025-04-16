package test.editor;

import com.editor.utils.SpriteUtils;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class TestSpriteUtils {

    @Test
    public void testGenerateSpriteMap() throws IOException {
        // Create a SpriteUtils instance
        SpriteUtils spriteUtils = new SpriteUtils();

        // Create ImageIcon objects
        ImageIcon sprite1 = new ImageIcon("Assets/sprites/sprite1.png");
        ImageIcon sprite2 = new ImageIcon("Assets/sprites/sprite2.png");
        ImageIcon sprite3 = new ImageIcon("Assets/sprites/sprite3.png");
        ImageIcon[] sprites = {sprite1, sprite2, sprite3};

        // Create ImageIcon objects for the format
        ImageIcon format1 = new ImageIcon("Assets/sprites/sprite1.png");
        ImageIcon format2 = new ImageIcon("Assets/sprites/sprite3.png");
        ImageIcon format3 = null;
        ImageIcon[][] formato = {
                {format1, format2},
                {format3, null}
        };

        // Test the generateSpriteMap method
        spriteUtils.generateSpriteMap(sprites, formato);

        // Check that the map file was created
        File mapFile = new File("maps/map.txt");
        assertTrue(mapFile.exists());

        // Check the contents of the map file
        StringBuilder expectedContents = new StringBuilder();
        for (ImageIcon[] row : formato) {
            for (ImageIcon format : row) {
                int spriteIndex = 0;
                if (format != null) {
                    if (format == sprite1) {
                        expectedContents.append(spriteIndex).append(" ");
                    } else if (format == sprite2) {
                        expectedContents.append(++spriteIndex).append(" ");
                    } else if (format == sprite3) {
                        expectedContents.append(++spriteIndex).append(" ");
                    }
                } else {
                    expectedContents.append("0 ");
                }
            }
            expectedContents.append(System.lineSeparator());
        }
        String actualContents = new String(Files.readAllBytes(mapFile.toPath()));
        assertEquals(expectedContents.toString(), actualContents);
    }

    @Test
    public void testMapName() {
        // Simula la entrada del usuario
        System.setIn(Test.class.getResourceAsStream("/test-input.txt"));

        // Crea una nueva instancia de SpriteUtils
        SpriteUtils spriteUtils = new SpriteUtils();

        // Llama al método mapName y comprueba el resultado
        String mapName = spriteUtils.mapName();
        assertEquals("TestMap", mapName);
    }
}