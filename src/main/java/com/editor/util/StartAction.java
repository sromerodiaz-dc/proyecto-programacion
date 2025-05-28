// StartAction.java (nuevo archivo)
package com.editor.util;

public class StartAction {
    public enum ActionType {CREATE, LOAD}
    public final ActionType type;
    public final int rows;
    public final int cols;
    public final String mapName;

    public StartAction(ActionType type, int rows, int cols) {
        this.type = type;
        this.rows = rows;
        this.cols = cols;
        this.mapName = null;
    }

    public StartAction(ActionType type, String mapName) {
        this.type = type;
        this.mapName = mapName;
        this.rows = 0;
        this.cols = 0;
    }
}