# Notas de desarrollo

### En este documento estaré escribiendo los avances y registrandolos en una linea de tiempo

De esta manera tendré una visión más clara de como está avanzando el proyecto, intentaré estilizar este documento de la mejor manera posible para que sea comprensible, útil y simple.
Dicho esto, los objetivos de estas primeras semanas de desarrollo será investigar y programar los siguientes objetivos:
```
1. Un game loop.
2. Mostrar por pantalla al jugador.
3. Mostrar por pantalla el entorno.
4. Mostrar por pantalla objetos.
5. Controlar al jugador.
6. Programar las interacciones.
7. Implementar una base de datos
```
Para esto emplearé ***assets ya creados*** y, de ser necesario, modificaré los assets para adaptarlos a mi juego.

### De qué va el juego (Teis)?

Mi idea es intentar desarrollar un juego en el que se pueda interactuar con varios objetos y personajes de un mismo mapa, dependiendo de como vaya el desarrollo,
implementaré otro mapa más allá del básico de inicio.
Este juego quiero que esté en Gallego y satirice un poco de los **"memes"** gallegos y de Vigo. 

# Semana del 06/05/2024

*Definición de las propiedades de la pantalla mostrada al iniciar el juego:*
```java
//Propiedades pantalla
    private static final int maxScreenColumnas = 18;
    private static final int maxScreenFilas = 12;
    //Ancho y alto (uso valores mínimos por facilidad)
    private static final int screenWidth = TamañoFinal * maxScreenColumnas;
    private static final int screenHeight =  TamañoFinal * maxScreenFilas;


    public TeisPanel() {
        setPreferredSize(new Dimension(screenWidth,screenHeight));
        setBackground(Color.black);
        setDoubleBuffered(true);
    }
```
**Resultado:**
![teis](./images/panelHecho.png)

#### Se crea un Issue y se relaciona al PROJECT creado en Github, además se crea un WebHook con Discord para automatizar las actualizaciones del codigo como Logs a un canal de discord.
