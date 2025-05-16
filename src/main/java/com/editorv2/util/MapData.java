package com.editorv2.util;

import com.editorv2.model.CeldaCoord;
import com.editorv2.model.event.SpawnEvent;
import com.editorv2.model.event.TeleportEvent;

import java.util.List;
import java.util.Set;

public record MapData(int rows, int cols, int[][] matrix, Set<CeldaCoord> collisions, SpawnEvent spawnEvent, List<TeleportEvent> teleports) {}