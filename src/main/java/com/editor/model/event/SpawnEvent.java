package com.editor.model.event;

public class SpawnEvent extends MapEvent {
    private final int row;
    private final int col;

    public SpawnEvent(int row, int col) {
        super("spawn");
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
}