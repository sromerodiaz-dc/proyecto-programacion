package GAME.OBJECT;

import GAME.GAME.TeisPanel;
import GAME.OBJECT.OBJS.BusVitrasa;
import GAME.OBJECT.OBJS.Container;
import GAME.OBJECT.OBJS.Passvigo;
import GAME.OBJECT.OBJS.Puerta;

/**
 * Clase que se encarga de colocar objetos en el panel de juego.
 */
public class ObjectPlacer {
    /**
     * Referencia al panel de juego donde se colocarán los objetos.
     */
    private final TeisPanel teisPanel;

    /**
     * Constructor que inicializa la referencia al panel de juego.
     *
     * @param teisPanel el panel de juego donde se colocarán los objetos
     */
    public ObjectPlacer(TeisPanel teisPanel) {
        this.teisPanel = teisPanel;
    }

    /**
     * Coloca objetos en el panel de juego.
     */
    public void setObject() {
    }
}
