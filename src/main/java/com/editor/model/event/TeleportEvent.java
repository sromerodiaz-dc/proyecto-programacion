package com.editor.model.event;

public class TeleportEvent extends MapEvent {
    private final int row;
    private final int col;
    private final int targetRow;
    private final int targetCol;

    public TeleportEvent(int row, int col, int targetX, int targetY) {
        super("teleport");
        this.row = row;
        this.col = col;
        this.targetRow = targetX;
        this.targetCol = targetY;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getTargetRow() { return targetRow; }
    public int getTargetCol() { return targetCol; }
}