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
        // Crea un objeto Passvigo y lo coloca en la posición (23, 23)
        teisPanel.obj[0] = new Passvigo();
        teisPanel.obj[0].worldX = 20 * teisPanel.sizeFinal;
        teisPanel.obj[0].worldY = 15 * teisPanel.sizeFinal;

        // Crea un objeto Puerta y lo coloca en la posición (20, 21)
        teisPanel.obj[1] = new Puerta();
        teisPanel.obj[1].worldX = 10 * teisPanel.sizeFinal;
        teisPanel.obj[1].worldY = 15 * teisPanel.sizeFinal;

        // Crea un objeto Cofre y lo coloca en la posición (20, 21)
        teisPanel.obj[2] = new Container();
        teisPanel.obj[2].worldX = 15 * teisPanel.sizeFinal;
        teisPanel.obj[2].worldY = 15 * teisPanel.sizeFinal;

        // Crea un objeto Cofre y lo coloca en la posición (20, 21)
        teisPanel.obj[3] = new BusVitrasa();
        teisPanel.obj[3].worldX = 20 * teisPanel.sizeFinal;
        teisPanel.obj[3].worldY = 10 * teisPanel.sizeFinal;
    }
}
