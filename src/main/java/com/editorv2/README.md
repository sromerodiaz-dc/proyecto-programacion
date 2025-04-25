# Nuevo editor de mapas
//TODO TERMINAR ESTE README
Creo que es más fácil empezar de cero que tener que modificar las cosas del editor ya creado.

## Estructura del nuevo editor
Se aplica el modelo Observer para mantener los datos sincronizados. La sincronización de los datos es lo más importante.
```text
model      -> datos del mapa. Guarda los listeners y contiene la lógica de notificación de los datos
view       -> vistas dadas por los componentes de la librería Java Swing.
controller -> lógica de interacción entre las vistas y los datos.
```

---
### CONTROLLER/ `TextureController`

---
### MODEL/ `MapModel`
1. Almacenará la matriz de números que representa el mapa.
2. Notificará los cambios realizados entre las vistas.

### MODEL/ `IModelChangeListener`

---
### VIEW/ `MapEditorPanel`

### VIEW/ `MiniMapView`
Flujo de trabajo:
    El MiniMapView no necesita conocer directamente el JScrollPane

    Se usa el scrollRectToVisible en el panel principal (MapEditorPanel)

    El JScrollPane detectará automáticamente el cambio

### VIEW/ `TilePalettePanel`

---
### /`GUI`
