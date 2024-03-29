package model;

import java.awt.*;

public enum TeamColor {
    BLACK("黑", Color.BLACK), RED("红", Color.RED);
    private final String name;
    private final Color color;
    TeamColor(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
