package Pong.Network;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Packet {
    public static final String DELIMITER = ";";
    public static final String TERMINATOR = "\r\n";
    public static final int MAX_SIZE = 1024;

    private String type;
    private List<String> items;

    public Packet(String type, String ...items) {
        this.type = type;
        this.items = new LinkedList<>();
        Collections.addAll(this.items, items);
    }

    public static Packet parse(String data) {
        data.replaceAll("\r\n", "");

        String tokens[] = data.split(DELIMITER);

        if (tokens.length == 1) {
            return new Packet(tokens[0]);
        }

        return new Packet(tokens[0], Arrays.copyOfRange(tokens, 1, tokens.length));
    }

    public String getType() {
        return type;
    }

    public String[] getItems() {
        String[] array = items.toArray(new String[items.size()]);
        return array;
    }

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
}
