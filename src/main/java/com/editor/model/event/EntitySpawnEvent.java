package com.editor.model.event;

import com.editor.model.record.CeldaCoord;

public class EntitySpawnEvent extends MapEvent {
    private final int row;
    private final int col;
    private String id;

    public EntitySpawnEvent(int row, int col) {
        super("spawn");
        this.row = row;
        this.col = col;
    }

    public EntitySpawnEvent(String id, int row, int col) {
        super("spawn");
        this.id = id;
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public String getId() { return id; }
    public CeldaCoord getSpawn() {return new CeldaCoord(row, col);}
}