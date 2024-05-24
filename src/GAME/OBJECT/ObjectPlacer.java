package GAME.OBJECT;

import GAME.GAME.TeisPanel;
import GAME.OBJECT.OBJS.Passvigo;

public class ObjectPlacer {
    TeisPanel teisPanel;
    public ObjectPlacer (TeisPanel teisPanel) {
        this.teisPanel = teisPanel;
    }

    public void setObject() {
        teisPanel.obj[0] = new Passvigo();
        teisPanel.obj[0].worldX = 23 * teisPanel.sizeFinal;
        teisPanel.obj[0].worldY = 23 * teisPanel.sizeFinal;
    }
}
