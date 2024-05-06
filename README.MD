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

### ¿De qué va el juego (Teis)?

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

### Por otra parte...

1. Se crea un ***Issue*** y se relaciona al `PROJECT` creado en GitHub.
2. Se crea un *WebHook* con *Discord* para automatizar las actualizaciones del codigo como Logs a un canal de discord.
3. Se comienza a implementar el **Game Loop** en la rama *``ISSUE-1``*.

### ¿Qué es un Game Loop?

En desarrollo de `videojuegos` se conoce al "bucle principal del juego" o "Game Loop" como algo **increiblemente** fundamental:
Imagina que estás jugando a tu videojuego favorito. `Corres, saltas, esquivas enemigos y completas misiones.` Bien, pues *todo* esto está siendo posible gracias al _**Game Loop**_.

Pues volviendo a la pregunta principal, **qué es un Game Loop?** 
Es la _base_ de la jugabilidad de un videojuego. Se trata de un ciclo que se repite constantemente mientras el juego está en funcionamiento, ``controlando el desarrollo de la partida y la experiencia del jugador``.

### ¿Cómo funciona el bucle de juego?
| Fases del bucle | Descripción                                                                                                                                                                                                                                                                          |                                                                                                                                                                                                                                                                        
|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ENTRADA         | El bucle comienza capturando las acciones del jugador, como *presionar botones, mover el joystick o usar la pantalla táctil*                                                                                                                                                         |
| PROCESAMIENTO   | Luego, el juego ``procesa`` estas entradas para determinar cómo *afectan el mundo del juego y el estado del jugador*. Por ejemplo, si presionas el botón de salto, tu personaje saltará en el juego.                                                                                 |
| ACTUALIZACIÓN   | Con base en las entradas procesadas, *el bucle actualiza el estado del juego*, incluyendo la posición de los objetos, la física del entorno y las variables del jugador ``(salud, munición, etc.)``.                                                                                 |
| RENDERIZADO     | Finalizando el ciclo, el bucle genera la `imagen` que se muestra en la pantalla, reflejando los cambios realizados en la etapa de actualización.                                                                                                                                     |
| REPETICIÓN      | El ciclo *vuelve* a comenzar, ``capturando nuevas entradas del jugador, procesándolas y actualizando el juego en consecuencia``. Este proceso se repite **incesantemente** mientras el juego esté en ejecución lo que consigue una experiencia fluida e interactiva para el jugador. |

### ¿Por qué es importante el bucle de juego?

Es fundamental por varias razones:

*Crea una experiencia fluida**: El bucle constante asegura que el juego responda instantáneamente a las acciones del jugador, proporcionando una sensación de control y realismo.

*Define la jugabilidad**: El bucle determina cómo se desarrolla la partida, estableciendo las reglas y la interacción del jugador con el mundo del juego.

*Mantiene el juego en marcha**: El ciclo repetitivo garantiza que el juego no se detenga ni se congele, manteniendo al jugador inmerso en la experiencia.

*Optimización**: El bucle permite optimizar el rendimiento del juego, ejecutando solo las tareas necesarias en cada ciclo y evitando procesos innecesarios.

En resumen, ``el bucle de juego`` es el corazón de un *videojuego, controlando la jugabilidad, la fluidez y la experiencia general del jugador*. Es un concepto fundamental para el diseño y desarrollo de videojuegos exitosos.

#### Definición de Game Loop:
Este metodo es llamado por el _GameLoop_ despues de otro llamado *"Actualiza"* (aun por definir).
**Instancia** un objeto para uso de gráficos de dos dimensiones y luego, a modo de prueba, ***defino un espacio en blanco situado
en las coordenadas ``x:100, y:100, alto y ancho: 48px``***.
```java
public void paintComponent(Graphics g){
    super.paintComponent(g);
    
    Graphics2D g2 = (Graphics2D)g;
    g2.setColor(Color.white);
    g2.fillRect(100,100,sizeFinal,sizeFinal);
    g2.dispose();
}
```

**Resultado:**
![teis](./images/componente2DCreado.png)

### Desarrollo de `Key Inputs`

Para desarrollar un _videojuego_ es **necesario** una serie de ``controles``. Esto es lo que estaré desarrollando en este apartado.
