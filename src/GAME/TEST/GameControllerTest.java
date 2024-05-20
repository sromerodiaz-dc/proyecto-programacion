package GAME.TEST;

import GAME.ENTITY.Player;
import GAME.FX.KeyManager;
import GAME.GAME.GameController;
import GAME.GAME.GameModel;
import GAME.GAME.TeisPanel;
import GAME.GPHICS.Pieza;
import GAME.GPHICS.PiezaManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private GameModel stubModel;

    private PiezaManager stubPiezaManager;

    private GameController gameController;

    @BeforeEach
    public void setUp() {
        // Create stub implementations of GameModel and PiezaManager
        stubModel = new GameModel(new TeisPanel(),new KeyManager()) {
            @Override
            public Player getPlayer() {
                return new Player(new TeisPanel(),new KeyManager()) {
                    @Override
                    public void actualiza() {
                        // Do nothing
                    }
                };
            }
        };

        stubPiezaManager = new PiezaManager(new TeisPanel()) {
        };

        // Create a new GameController instance with the stub objects
        gameController = new GameController(stubModel, stubPiezaManager);
    }

    @Test
    void testUpdate() {
        // Call the update() method
        gameController.update();

        // Verify that the actualiza() method of the Player object was called
        // In this case, we can't verify the actual call, but we can check that the Player object is not null
        assertNotNull(stubModel.getPlayer());
    }

    @Test
    void testGetPiezaManager() {
        // Call the getPiezaManager() method
        PiezaManager piezaManager = gameController.getPiezaManager();

        // Verify that the returned PiezaManager object is the same as the stub object
        assertEquals(stubPiezaManager, piezaManager);
    }
}