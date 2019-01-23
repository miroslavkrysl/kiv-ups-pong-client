package Pong.Gui.Controllers;

import Pong.Game.Types.Action;
import Pong.Game.Types.Side;
import javafx.scene.input.KeyCode;
import java.util.HashMap;


public class Controls {
    private HashMap<KeyCode, PlayerAction> playerActions;
    private HashMap<PlayerAction, KeyCode> keyCodes;

    public Controls() {
        this.playerActions = new HashMap<>();
        this.keyCodes = new HashMap<>();
        setDefault();
    }

    public void setDefault() {
        add(KeyCode.W, PlayerAction.get(Side.LEFT, Action.UP));
        add(KeyCode.S, PlayerAction.get(Side.LEFT, Action.DOWN));
        add(KeyCode.O, PlayerAction.get(Side.RIGHT, Action.UP));
        add(KeyCode.K, PlayerAction.get(Side.RIGHT, Action.DOWN));
    }

    private void add(KeyCode keyCode, PlayerAction playerAction) {
        playerActions.put(keyCode, playerAction);
        keyCodes.put(playerAction, keyCode);
    }

    public PlayerAction getPlayerAction(KeyCode code) {
        return playerActions.get(code);
    }

    public KeyCode getKeyCode(PlayerAction playerAction) {
        return keyCodes.get(playerAction);
    }

    public KeyCode getKeyCode(Side side, Action action) {
        return keyCodes.get(PlayerAction.get(side, action));
    }
}
