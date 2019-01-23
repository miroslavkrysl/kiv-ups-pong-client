package Pong.Gui;

import Pong.Game.Types.Action;
import Pong.Game.Types.Side;

import java.util.HashMap;

public class PlayerAction {

    private static final HashMap<Side, HashMap<Action, PlayerAction>> playerActions = new HashMap<>();
    static {
        playerActions.put(Side.LEFT, new HashMap<>());
        playerActions.put(Side.RIGHT, new HashMap<>());

        playerActions.get(Side.LEFT).put(Action.UP, new PlayerAction(Side.LEFT, Action.UP));
        playerActions.get(Side.LEFT).put(Action.DOWN, new PlayerAction(Side.LEFT, Action.DOWN));
        playerActions.get(Side.LEFT).put(Action.READY, new PlayerAction(Side.LEFT, Action.READY));
        playerActions.get(Side.RIGHT).put(Action.UP, new PlayerAction(Side.RIGHT, Action.UP));
        playerActions.get(Side.RIGHT).put(Action.DOWN, new PlayerAction(Side.RIGHT, Action.DOWN));
        playerActions.get(Side.RIGHT).put(Action.READY, new PlayerAction(Side.RIGHT, Action.READY));
    }

    public final Side side;
    public final Action action;

    private PlayerAction(Side side, Action action) {
        this.side = side;
        this.action = action;
    }

    public static PlayerAction get(Side side, Action action) {
        return playerActions.get(side).get(action);
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != PlayerAction.class) {
            return false;
        }
        PlayerAction playerAction = (PlayerAction)o;
        return playerAction.action.equals(this.action) && playerAction.side.equals(this.side);
    }
}