package ca.krasnay.panelized.testapp;

import java.io.Serializable;

public class Widget implements Serializable {

    public enum Color {
        RED,
        YELLOW,
        BLUE,
        ORANGE,
        GREEN,
        PURPLE;
    }

    private int id;
    private String name;
    private Color color;

    public Widget() {
    }

    public Widget(int id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
