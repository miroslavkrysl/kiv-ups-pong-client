package Pong.Game;

import Pong.Game.Exceptions.GameTypeException;
import Pong.Game.Exceptions.PlayerStateException;
import Pong.Game.Types.Side;
import javafx.beans.property.*;

public class Ball {
    public static final int RADIUS = 25;
    public static final int POSITION_MAX = (Game.HEIGHT / 2) - RADIUS;
    public static final int POSITION_MIN = -(Game.HEIGHT / 2) + RADIUS;
    public static final int ANGLE_MAX = 60;
    public static final int ANGLE_MIN = -60;
    public static final int SPEED_MAX = 1920;
    public static final int SPEED_MIN = 640;
    public static final Side INIT_SIDE = Side.LEFT;
    public static final int INIT_X = 0;
    public static final int INIT_Y = 0;
    public static final int INIT_ANGLE = 0;
    public static final int INIT_SPEED = 0;

    private LongProperty timestamp;
    private ObjectProperty<Side> side;
    private IntegerProperty position;
    private IntegerProperty angle;
    private IntegerProperty speed;

    public Ball(long timestamp, Side side, int position, int angle, int speed) {
        this.timestamp = new SimpleLongProperty(timestamp);
        this.side = new SimpleObjectProperty<>(side);
        this.position = new SimpleIntegerProperty(position);
        this.angle = new SimpleIntegerProperty(angle);
        this.speed = new SimpleIntegerProperty(speed);
    }

    public Ball(long timestamp) {
        this(timestamp, INIT_SIDE, INIT_Y, INIT_ANGLE, INIT_SPEED);
    }

    public Ball(String[] items) throws GameTypeException, PlayerStateException {
        if (items.length != 5) {
            throw new PlayerStateException("ball must have 5 items");
        }

        try {
            setTimestamp(Long.parseLong(items[0]));
        }
        catch (Exception exception) {
            throw new PlayerStateException("timestamp is in invalid format");
        }

        try {
            setSide(Side.fromString(items[1]));
        }
        catch (Exception exception) {
            throw new PlayerStateException("side is in invalid format");
        }

        try {
            setPosition(Integer.parseInt(items[2]));
        }
        catch (Exception exception) {
            throw new PlayerStateException("position is in invalid format");
        }

        try {
            setAngle(Integer.parseInt(items[3]));
        }
        catch (Exception exception) {
            throw new PlayerStateException("angle is in invalid format");
        }

        try {
            setSpeed(Integer.parseUnsignedInt(items[4]));
        }
        catch (Exception exception) {
            throw new PlayerStateException("speed is in invalid format");
        }
    }

    public int[] getPosition(long when, Game.Phase phase) {
        double hypotenuse = (when - getTimestamp())
                * (getSpeed() / 1000.0);

        double legY = Math.sin(getAngle()) * hypotenuse;
        double legX = Math.cos(getAngle()) * hypotenuse;

        double x = getSide() == Side.LEFT
                ? legX - Game.WIDTH / 2.0
                : Game.WIDTH / 2.0 - legX;

        if (phase == Game.Phase.START || phase == Game.Phase.WAITING) {
            x += getSide() == Side.LEFT
                    ? Game.WIDTH / 2.0
                    : - Game.WIDTH / 2.0;
        }

        double y = legY % Game.HEIGHT;
        if ((legY / Game.HEIGHT) % 2 == 0) {
            y *= -1;
        }
        y += getPosition();

        return new int[] {(int)x, (int)y};
    }

    public long getTimestamp() {
        return timestamp.get();
    }

    public LongProperty timestampProperty() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp.set(timestamp);
    }

    public Side getSide() {
        return side.get();
    }

    public ObjectProperty<Side> sideProperty() {
        return side;
    }

    public void setSide(Side side) {
        this.side.set(side);
    }

    public int getPosition() {
        return position.get();
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    public int getAngle() {
        return angle.get();
    }

    public IntegerProperty angleProperty() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle.set(angle);
    }

    public int getSpeed() {
        return speed.get();
    }

    public IntegerProperty speedProperty() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed.set(speed);
    }
}
