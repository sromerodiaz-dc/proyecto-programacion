package com.editorv2.model;

public record MapEvent(String type, int row, int col, int targetX, int targetY, int sourceX, int sourceY) {}
