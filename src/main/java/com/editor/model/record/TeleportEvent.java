package com.editor.model.record;

public record TeleportEvent (int row, int col, int targetRow, int targetCol) {}