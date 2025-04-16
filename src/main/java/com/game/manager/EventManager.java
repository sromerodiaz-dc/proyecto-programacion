package com.game.manager;

import com.game.controller.TeisPanel;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Clase muy importante, ya que maneja todos los eventos del juego.
 * Gracias a esta clase se pueden manejar cambios de mapa, de puntos de vida, cambios de sprites, etc.
 */
public class EventManager {
    /**
     * Referencia al panel de juego.
     */
    TeisPanel teisPanel;

    /**
     * Matriz de rectángulos de eventos.
     */
    EventRectangle[][] eventRectangle;

    /**
     * Constructor de la clase EventManager.
     *
     * @param teisPanel panel de juego
     */
    public EventManager(TeisPanel teisPanel) {
        this.teisPanel = teisPanel;

        // Inicializa la matriz de rectángulos de eventos con el tamaño del mundo
        eventRectangle = new EventRectangle[teisPanel.maxWorldCol][teisPanel.maxWorldRow];

        int col = 0;
        int row = 0;
        while (col < teisPanel.maxWorldCol && row < teisPanel.maxWorldRow) {
            // Crea un nuevo rectángulo de evento en la posición (col, row)
            eventRectangle[col][row] = new EventRectangle();
            // Define la posición y tamaño del rectángulo de evento
            eventRectangle[col][row].x = 23;
            eventRectangle[col][row].y = 23;
            eventRectangle[col][row].width = 2;
            eventRectangle[col][row].height = 2;

            // Guarda la posición original del rectángulo de evento
            eventRectangle[col][row].defaultX = eventRectangle[col][row].x;
            eventRectangle[col][row].defaultY = eventRectangle[col][row].y;

            // Incrementa la columna y, si es necesario, la fila
            col++;
            if (col == teisPanel.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Verifica si se ha producido un evento en el mapa.
     */
    public void checkEvent(){
        // Verifica si se ha encontrado un periódico en la posición (10, 12) con sentido 'a'
        if (hit(10, 12, 'a')) {
            // Si se ha encontrado, aplica daño y muestra un diálogo
            damage(10, 12, teisPanel.controller.dialogoState, "\"Encontras tirado no chan un periódico...\nO Celta volveu perder, non che sorprende,\nsó entrischécete\"");
        }

        // Verifica si se ha encontrado un objeto de curación en la posición (14, 13) con sentido ''
        if (hit(14, 13, 's')) {
            // Si se ha encontrado, aplica curación
            heal(14, 13, teisPanel.controller.dialogoState);
        }

        // Resetea el cooldown del evento en la posición (10, 12) a 30 segundos
        eventRectangle[10][12].resetCooldowns(30);
    }

    /**
     * Verifica si el personaje ha golpeado un evento en la posición (col, row) con sentido sentido.
     *
     * @param col columna del evento
     * @param row fila del evento
     * @param sentido sentido del personaje (a, s, d, w)
     * @return true si se ha golpeado el evento, false en caso contrario
     */
    public boolean hit (int col, int row, char sentido){
        boolean doesHit = false;

        // Actualiza la posición del área sólida del personaje en el mundo
        teisPanel.model.solidArea.x = teisPanel.model.worldX + teisPanel.model.solidArea.x;
        teisPanel.model.solidArea.y = teisPanel.model.worldY + teisPanel.model.solidArea.y;

        // Actualiza la posición del evento en el mundo
        eventRectangle[col][row].x = col*teisPanel.sizeFinal + eventRectangle[col][row].x;
        eventRectangle[col][row].y = row*teisPanel.sizeFinal + eventRectangle[col][row].y;

        // Verifica si el área sólida del personaje intersecta con el evento y no se ha realizado antes
        if (teisPanel.model.solidArea.intersects(eventRectangle[col][row]) &&!eventRectangle[col][row].done){
            // Verifica si el sentido del personaje coincide con el sentido del evento o si no se especificó sentido
            if (teisPanel.model.sentido == sentido || sentido!= '0') {
                doesHit = true;
            }
        }

        // Restaura la posición original del área sólida del personaje y del evento
        teisPanel.model.solidArea.x = teisPanel.model.defaultSolidAreaX;
        teisPanel.model.solidArea.y = teisPanel.model.defaultSolidAreaY;
        eventRectangle[col][row].x = eventRectangle[col][row].defaultX;
        eventRectangle[col][row].y = eventRectangle[col][row].defaultY;

        return doesHit;
    }

    /**
     * Método que aplica daño al jugador.
     *
     * @param col La columna del evento que causa daño.
     * @param row La fila del evento que causa daño.
     * @param estado El estado del juego después de aplicar daño.
     * @param mensaje El mensaje que se muestra al jugador después de aplicar daño.
     */
    public void damage(int col, int row, int estado, String mensaje) {
        // Cambia el estado del juego
        teisPanel.controller.estado = estado;
        // Muestra el mensaje al jugador
        teisPanel.controller.ui.dialogo = mensaje;
        // Aplica daño al jugador
        teisPanel.model.life -= 5;
        // Marca el evento como completado
        eventRectangle[col][row].done = true;
    }

    /**
     * Método que cura al jugador.
     *
     * @param col La columna del evento que cura al jugador.
     * @param row La fila del evento que cura al jugador.
     * @param estado El estado del juego después de curar al jugador.
     */
    public void heal(int col, int row, int estado) {
        // Verifica si se ha presionado una tecla
        if (teisPanel.model.keyManager.isPressed) {
            // Desactiva la animación de ataque del jugador
            teisPanel.model.attack = false;
            // Cambia el estado del juego
            teisPanel.controller.estado = estado;
            // Muestra el mensaje al jugador
            teisPanel.controller.ui.dialogo = "\"Bebiches unha estrela.\nSíntese coma se o Vialia nunca fora edificado\"";
            // Curar al jugador, pero no más allá de la vida máxima
            if (teisPanel.model.life <= 7) {
                teisPanel.model.life += 3;
            } else {
                teisPanel.model.life += (teisPanel.model.maxLife - teisPanel.model.life);
            }
            // Marca el evento como completado
            eventRectangle[col][row].done = true;
        }
    }
}
