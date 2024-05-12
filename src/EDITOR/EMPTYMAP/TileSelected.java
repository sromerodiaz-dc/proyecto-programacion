package EDITOR.EMPTYMAP;

public class TileSelected {
    int id;
    String path;

    public TileSelected(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSprite() {
        return path;
    }
}
