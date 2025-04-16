package com.game;

import com.game.manager.MapSelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestMapSelector {

    private MapSelector mapSelector;
    private File tempDirectory;

    @BeforeEach
    public void setUp() throws IOException {
        mapSelector = new MapSelector();
        tempDirectory = Files.createTempDirectory("map_selector_test").toFile();
        tempDirectory.deleteOnExit();

        // Create test map files
        Path mapFile1 = Paths.get(tempDirectory.getPath(), "map1.txt");
        Files.createFile(mapFile1);

        Path mapFile2 = Paths.get(tempDirectory.getPath(), "map2.txt");
        Files.createFile(mapFile2);
    }

    @Test
    void testGetFiles() {
        // Call the getFiles() method
        File[] files = mapSelector.getFiles();

        // Verify the expected behavior
        assertEquals(2, files.length);
        List<String> fileNames = Arrays.stream(files).map(File::getName).toList();
        assertEquals(List.of("map1.txt", "map2.txt"), fileNames);
    }

    @Test
    void testGetFilesNoFilesFound() {
        // Delete the test map files
        File[] files = tempDirectory.listFiles();
        for (File file : files) {
            file.delete();
        }

        // Call the getFiles() method
        assertThrows(RuntimeException.class, mapSelector::getFiles);
    }

    @AfterEach
    public void tearDown() {
        // Delete the temp directory
        tempDirectory.delete();
    }
}