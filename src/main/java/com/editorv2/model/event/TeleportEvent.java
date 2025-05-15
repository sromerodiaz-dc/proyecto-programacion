package com.editorv2.model.event;

public class TeleportEvent extends MapEvent {
    private final int row;
    private final int col;
    private final int[] targetXY;

    public TeleportEvent(int row, int col, int[] targetXY) {
        super("teleport");
        this.row = row;
        this.col = col;
        this.targetXY = targetXY;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public int[] getTargetXY() { return targetXY; }
}