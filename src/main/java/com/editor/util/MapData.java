package com.editor.util;

import com.editor.model.CeldaCoord;
import com.editor.model.event.SpawnEvent;
import com.editor.model.event.TeleportEvent;

import java.util.List;
import java.util.Set;

public record MapData(int rows, int cols, int[][] matrix, Set<CeldaCoord> collisions, SpawnEvent spawnEvent, List<TeleportEvent> teleports) {}