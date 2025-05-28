package com.editor.model.record;

import com.editor.model.event.EntitySpawnEvent;
import com.editor.model.event.TeleportEvent;

import java.util.List;
import java.util.Set;

public record MapData(int rows, int cols, int[][] matrix, Set<CeldaCoord> collisions,CeldaCoord playerSpawn, List<EntitySpawnEvent> spawnEvent, List<TeleportEvent> teleports) {}