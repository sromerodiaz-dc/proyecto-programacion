# Índice

- [Introducción](#notas-de-desarrollo)
- [Registro por semanas](#registro-semanal)
- - [Semana 1](#semana-del-06052024)
    - [Game Loop](#qué-es-un-game-loop)
    - [¿Por qué es importante?](#por-qué-es-importante-el-bucle-de-juego)
    - [Animaciones de personaje](#implementación-de-sprites-hechos-con-pixel-studio)
- - [Semana 2](#semana-del-13052024)
- - [Semana 3](#semana-del-20052024)
- - [Semana 4](#semana-del-27052024)



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

# Registro semanal
## Semana del 06/05/2024

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
```java
@Override
public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    switch (code) {
        case KeyEvent.VK_W:
            up = true;
            break;
        case KeyEvent.VK_S:
            down = true;
            break;
        case KeyEvent.VK_A:
            left = true;
            break;
        case KeyEvent.VK_D:
            right = true;
            break;
    }
}
```

Al implementar este codigo en una clase llamada **GAME.FX.KeyManager** y luego programar que pasa al presionar cada tecla en el metodo **MOVE()**
````java
private void move() {
        if (key.up) {
            playerY -= speed;
        } else if (key.down) {
            playerY += speed;
        } else if (key.left) {
            playerX -= speed;
        } else if (key.right) {
            playerX += speed;
        }
    }
````

Lo que sucede es algo interesante y es que al iniciar la _app_ aparece la GAME.GUI.ventana en negro con el cuadrado blanco como se espera, ``PERO`` al presionar **WASD**,
este cuadrado parece _"desaparecer"_.
La explicación es la siguiente:
El GameLoop llama al metodo actualiza() que contiene el metodo move() lo *más rápido* que puede y **por ello** al actualizar el valor de la posicion
y luego hacer repaint, este valor se ve modificado todas las veces que el ordenador **es capaz de llamar a la función por segundo**.
Lo que haré para solucionar esto es ``CAPEARLO`` a _60 frames por segundo o menos_

### Definiendo el GameLoop a 24 fps...
El tiempo de ejecución es necesario capearlo para tener una sensación de fluidez y tener control sobre como se ejecutan los movimientos del juego.
Al buscar información sobre esto encontré un método popular sobre el **Game Loop llamado Delta Iterator**:
````java
@Override
public void run() {
    // Implementación Delta / Iterador Game Loop
    double tiempoEspera = (double) 1000000000/fps; // Calcula el tiempo de espera en nanosegundos por frame
    double delta = 0; // Variable para acumular el delta time
    long lastTime = System.nanoTime(); // Almacena el tiempo en nanosegundos del frame anterior
    long currentTime; // Variable para almacenar el tiempo en nanosegundos del frame actual

    // Implementación ShowFPS
    long timer = 0; // Variable para acumular el tiempo desde la última medición de FPS
    int espera = 0; // Contador de frames desde la última medición de FPS

    while (teisThread != null) { // Bucle principal del juego
        currentTime = System.nanoTime(); // Obtiene el tiempo en nanosegundos del frame actual
        delta += (currentTime - lastTime) / tiempoEspera; // Acumula el delta time (tiempo transcurrido desde el último frame)
        timer += (currentTime - lastTime); // Acumula el tiempo desde la última medición de FPS
        lastTime = currentTime; // Actualiza el tiempo del frame anterior con el tiempo actual

        if (delta >= 1) { // Si el delta time es mayor o igual a 1 (un frame completo)
            actualiza(); // Llama al método `actualiza()` para actualizar el estado del juego
            repaint(); // Solicita que el componente se repinte (actualice su visualización)
            delta--; // Resta 1 al delta time para que no se acumule en el siguiente frame
            espera ++; // Incrementa el contador de frames
        }

        if (timer >= 1000000000) { // Si ha pasado un segundo (1000000000 nanosegundos)
            System.out.println("FPS: "+espera); // Imprime el valor de FPS (frames por segundo)
            espera = 0; // Reinicia el contador de frames
            timer = 0; // Reinicia el temporizador de FPS
        }
    }
}
````
_El resultado de asociar movimiento con sprites (aún en fase temprana) es el siguiente..._

_Estos son los **sprites** creados:_

![moveGif](./images/muestraSprites.gif)

_Este es el resultado de su **implementación**:_

![moveGif](./images/moveTemprano.gif)

### Animación mejorada

Metodo move() de la clase GAME.ENTITY
Nueva implementación: 1 sprite cada 10 frames
-Ahora el metodo además de controlar la posición, tambien controla los sprites por movimiento.
-Este metodo está estructurado alrededor de la idea de ser ejecutado 30 veces POR CADA segundo.

### Implementación de Sprites hechos con Pixel Studio

He usado Google Maps para ver cosas caracteristicas como las baldosas del suelo o como son las tapas de las alcantarillas
y basandome en eso y en la ayuda de un IA que pasa imagenes a PixelArt he logrado algunas imagenes interesantes logradas
con un programa llamado Pixel Studio.

## Semana del 13/05/2024

Esta semana ha sido complicada debido a que _decidí_ pornerme un reto a mayores:

- *Crear un editor de mapas*
- - *Que el editor pudiese guardar mapas*
- - *Que te permitiese decidir el tamaño del mapa*
- - *Que te permitiese cambiar el fondo por el sprite que quisieras*


