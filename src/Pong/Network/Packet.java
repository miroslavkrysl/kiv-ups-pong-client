package Pong.Network;

import Pong.Network.Exceptions.MalformedPacketException;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The class Packet represents a network message.
 */
public class Packet {
    /**
     * The packet items delimiter.
     */
    public static final String DELIMITER = ";";
    /**
     * The packet termination symbol.
     */
    public static final String TERMINATOR = "#";
    /**
     * The packet max size.
     */
    public static final int MAX_SIZE = 1024;

    private String type;
    private List<String> items;

    /**
     * Instantiates a new Packet.
     *
     * @param type  the type of the packet
     * @param items the items of the packet
     */
    public Packet(String type, String ...items) {
        this.type = type;
        this.items = new LinkedList<>();
        Collections.addAll(this.items, items);
    }

    /**
     * Parse tcp stream serialized packet string and create a Packet instance.
     *
     * @param data the data
     * @return the resulting packet
     */
    public static Packet parse(String data) {
        data.replaceAll(TERMINATOR, "");

        String tokens[] = data.split(DELIMITER);

        if (tokens.length == 1) {
            return new Packet(tokens[0]);
        }

        return new Packet(tokens[0], Arrays.copyOfRange(tokens, 1, tokens.length));
    }

    /**
     * Gets the type of the packet.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Get items of the packet.
     *
     * @return the string array containing the packet items
     */
    public String[] getItems() {
        return items.toArray(new String[0]);
    }

    /**
     * Serialize packet into the tcp stream string.
     *
     * @return the tcp stream serialized packet string
     */
    public String serialize() {
        StringBuilder serialized = new StringBuilder();

        serialized.append(type);

        for (String item : items) {
            serialized.append(DELIMITER);
            serialized.append(item);
        }

        serialized.append(TERMINATOR);

        return serialized.toString();
    }

    /**
     * Check whether the packet has a given number of items, else throw an exception.
     *
     * @param count items count
     * @throws MalformedPacketException is thrown if the packet does not have the required number of items
     */
    public void validateItemsCount(int count) throws MalformedPacketException {
        if (items.size() != count) {
            throw new MalformedPacketException("packet does not have a required number of items");
        }
    }
}
