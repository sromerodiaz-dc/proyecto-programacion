package com.editor;
import com.editor.utils.SpriteLoader;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestSpriteLoader {

    @Test
    public void testLoadSprites() throws IOException, URISyntaxException {
        // Create a SpriteLoader instance
        SpriteLoader spriteLoader = new SpriteLoader();

        // Test the loadSprites method
        List<ImageIcon> sprites = List.of(spriteLoader.loadSprites());
        assertEquals(5, sprites.size());

        for (int i = 0; i < 5; i++) {
            assertNotNull(sprites.get(i));
        }

        // Test the writeToFile method
        File file = new File("maps_correspondencia/c_assets.txt");
        assertTrue(file.exists());

        List<String> imagePaths = new ArrayList<>();
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            int i=0;
            for (ImageIcon sprite : sprites) {
                if (sprite != null) {
                    imagePaths.add(sprite.toString().substring(sprite.toString().indexOf("image=") + 6));
                } else {
                    imagePaths.add(null);
                }
                writer.println(i + ": " + imagePaths.get(i));
                i++;
            }
        }

        List<String> actualImagePaths = new ArrayList<>();
        for (String line : Files.readAllLines(file.toPath())) {
            actualImagePaths.add(line.substring(line.indexOf(": ") + 2));
        }

        assertEquals(imagePaths, actualImagePaths);
    }
}