package GAME.FX;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class MapSelector {

    public String selectMap() {
        File[] mapFiles = getFiles();

        String[] options = new String[mapFiles.length];
        for (int i = 0; i < mapFiles.length; i++) {
            options[i] = (i + 1) + ". " + mapFiles[i].getName();
        }

        int choice = JOptionPane.showOptionDialog(
                null,
                "Select a map:",
                "Map Selector",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice < 0 || choice >= options.length) {
            throw new RuntimeException("Invalid map selection.");
        }

        return mapFiles[choice].getPath().replace("\\", "/").replace("Assets/", "");
    }

    private File[] getFiles() {
        File mapsFolder = new File("Assets/maps");

        if (!mapsFolder.exists() || !mapsFolder.isDirectory()) {
            throw new RuntimeException("The maps folder does not exist or is not a directory.");
        }

        File[] mapFiles = mapsFolder.listFiles((dir, name) -> Objects.requireNonNull(name).toLowerCase().endsWith(".txt"));
        if (mapFiles == null || mapFiles.length == 0) {
            throw new RuntimeException("No text files found in the maps folder.");
        }
        return mapFiles;
    }
}