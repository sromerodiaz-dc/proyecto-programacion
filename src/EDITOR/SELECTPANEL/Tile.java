package EDITOR.SELECTPANEL;

public class Tile {
    int id;
    String path;

    public Tile(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public String getSprite() {
        return path;
    }
}
