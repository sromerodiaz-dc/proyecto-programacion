# Nuevo editor de mapas
## Estructura del nuevo editor
Se aplica el modelo Observer para mantener los datos sincronizados. La sincronización de los datos es lo más importante.
```text
model      -> datos del mapa. Guarda los listeners y contiene la lógica de notificación de los datos
view       -> vistas dadas por los componentes de la librería Java Swing.
controller -> lógica de interacción entre las vistas y los datos.

src/
├── main/
│   ├── java/com/editor/
│   │   ├── controller/
│   │   │   └── TextureController.java 
│   │   ├── model/
│   │   │   ├── MapModel.java
│   │   │   └── IModelChangeListener.java
│   │   └── view/
│   │       ├── MapEditorPanel.java
│   │       ├── MiniMapView.java
│   │       └── TilePalettePanel.java
│   └── resources/
│       ├── textures.json
│       └── background/
└── test/
    └── java/editor
```

---
### CONTROLLER/`TextureController`
Se encarga de gestionar la *carga*, *mapeo* y *configuración* de texturas para el editor de mapas 2D. 
Es intermediario entre los recursos gráficos guardados en `resources root` y la lógica de la aplicación.

Sus funcionalidades más importantes son:
1. `Gestión de texturas`: carga automática de archivos PNG desde `resources/background` y asigna IDs únicos a cada una de estas texturas.
2. `Configuración del JSON`: crea el archivo de mapeo de texturas `tiles.json`.

```text
1. Verificar existencia de tiles.json
  → Crear nuevo si no existe (con diálogo de confirmación)
  → Permitir selección manual con JFileChooser
2. Cargar texturas desde recursos
3. Sincronizar con archivo de configuración:
  → Añadir nuevas texturas detectadas
  → Actualizar JSON manteniendo IDs existentes
4. Proporcionar acceso a texturas mediante:
  - getTexture(id)
  - getTextureId(nombre_archivo)
  - getAllTextures()
```

---
### Uso de HashMap para el mapeo de texturas
```java
private final Map<Integer, BufferedImage> textures = new HashMap<>();
private final Map<String, Integer> fileToIdMap = new HashMap<>();
```
a) **textures** (Mapa: ID → Imagen)
- Clave: Entero (Integer) que representa el ID único de la textura.
- Valor: Objeto BufferedImage con los píxeles de la textura cargada.
- Propósito: Permite acceder rápidamente a una textura usando su ID (ejemplo: getTexture(5) retorna la imagen asociada al ID 5).

b) **fileToIdMap** (Mapa: Nombre de archivo → ID)
- Clave: String (String) con el nombre del archivo PNG (ejemplo: "grass.png"). 
- Valor: Entero (Integer) que representa el ID asignado a esa textura. 
- Propósito: Permite obtener el ID de una textura usando su nombre de archivo (ejemplo: getTextureId("water.png") retorna 3 si ese es su ID).
    
### Funcionamiento conjunto 
Ambos mapas trabajan de forma complementaria para:

1. Carga inicial de texturas:
- Cuando se detecta un archivo PNG en resources/background, se genera un nuevo ID (nextId).
- Se añade al mapa textures la relación ID → BufferedImage.
- Se registra en fileToIdMap la relación nombre_archivo → ID.

2. Carga desde configuración (tiles.json):
- Al leer el JSON, se reconstruyen ambos mapas usando los IDs y rutas almacenadas.

3. Detección de nuevas texturas:
- Si hay archivos PNG no registrados en el JSON, se asignan nuevos IDs y se actualizan ambos mapas.

---
### MODEL/`MapModel`
1. Almacenamiento del mapa:
- Matriz bidimensional de enteros (int[][] matrix), donde cada valor representa el ID de una textura.
- Dimensiones configurables (rows y cols).

2. Gestión de modificaciones:
- Rastrea celdas modificadas mediante Set<Point> modifiedCells. 
- Optimiza el renderizado al procesar solo cambios recientes. 
- Método clearModifiedCells() para reiniciar el seguimiento después de actualizar las vistas.

```java
public void setTile(int row, int col, int value) {
    matrix[row][col] = value;
    modifiedCells.add(new Point(col, row));
    notifyListeners(); // Dispara actualizaciones en todas las vistas
}
```

---
### MODEL/`IModelChangeListener`
Interfaz clave para implementar el patrón Observer entre el modelo y las vistas.
Las vistas se registran con `mapModel.addListener(this)`.

Cuando el modelo es actualizado entocnes: `setTile() → notifyListeners() → onModelChanged() en todas las vistas registradas`.
```text
                    ┌──────────────┐
                    │  MapModel    │
                    ├──────────────┤
                    │ - matrix[][] │
                    │ - listeners  │
                    └──────┬───────┘
                           │
         ┌─────────────────┼─────────────────┐
         ▼                 ▼                 ▼
┌────────────────┐ ┌────────────────┐ ┌────────────────┐
│ MapEditorPanel │ │  MiniMapView   │ │TilePalettePanel│
└────────────────┘ └────────────────┘ └────────────────┘
```
Ventajas del diseño:

    1. Desacoplamiento: Las vistas no dependen de implementaciones concretas.

    2. Eficiencia: Solo se reprocesan áreas modificadas del mapa.

    3. Consistencia: Todas las vistas reciben la misma versión de los datos.

    4. Extensibilidad: Nuevas vistas pueden añadirse fácilmente registrándose como listeners.

---
### VIEW/`MapEditorPanel`
Panel principal de edición de mapas que implementa la interfaz gráfica para manipular el diseño del mapa 2D. Actúa como vista central del editor, integrando interacción del usuario y renderizado optimizado.

1. Renderizado del mapa:
- Dibuja una cuadrícula de tiles usando texturas del TextureController. 
- Cada celda se representa como un cuadrado de 32x32 píxeles (tileSize). 
- Borde blanco para delimitar celdas y fondo negro base.

2. Interacción del usuario:
- Clic izquierdo: Pinta con la textura actualmente seleccionada.
- Clic derecho: Borra la celda (textura negra por defecto).
- Arrastre: Permite pintado continuo.

3. Optimizaciones:
- Doble Buffer para evitar parpadeos.
- Repintado parcial: solo actualiza áreas modificadas usando `modifiedCells` del modelo.
```java
@Override
public void onModelChanged() {
    Set<Point> modified = model.getModifiedCells();
    modified.forEach(p -> repaint(p.x * tileSize, p.y * tileSize, tileSize, tileSize));
    model.clearModifiedCells();
}
```

4. Integración con componentes:
- Recibe actualizaciones del MapModel vía patrón Observer. 
- Usa textureController.getTexture(id) para obtener imágenes. 
- Coordina con TilePalettePanel para cambiar la textura seleccionada (setSelectedTexture).

---
### VIEW/`MiniMapView`
Flujo de trabajo:

    El MiniMapView no necesita conocer directamente el JScrollPane.
    Se usa el scrollRectToVisible en el panel principal (MapEditorPanel).

    El JScrollPane detectará automáticamente el cambio.

    Sincronización con el modelo:
        Implementa IModelChangeListener para recibir actualizaciones del MapModel.
        Método onModelChanged() dispara un repaint() completo al detectar modificaciones.

---
### VIEW/`TilePalettePanel`
Panel lateral que muestra las texturas disponibles para selección, actuando como paleta de herramientas gráficas.

Funcionalidades clave (inferidas):

    1. Visualización de texturas:
        Muestra miniaturas de todas las texturas cargadas por el TextureController.
        Organiza las texturas en una cuadrícula interactiva.

    2. Selección de herramientas:
        Clic en textura: Actualiza la selectedTextureId en el MapEditorPanel.
        Resalta visualmente la textura seleccionada.

---
### /`GUI`
Clase principal que define la interfaz gráfica del editor de mapas. Actúa como contenedor de todos los componentes y gestiona la disposición visual y las interacciones globales.
```text
1. Crea Modelo y Controladores
   → MapModel(70x70)
   → TextureController()
2. Construye componentes principales
   → MapEditorPanel (con JScrollPane)
   → MiniMapView
   → TilePalettePanel
3. Configura layout y añade componentes
4. Establece listeners de sincronización
```

El `json` generado por el método `saveMap()` está formado por la siguiente estructura:
````json
{
  "maps" : {
    "mi_mapa" : {
      "width" : 70,
      "height" : 70,
      "layers" : [ {
        "name" : "ground",
        "data" : [ [0,1,2], [3,4,5] ]
      }]
    }
  }
}
````