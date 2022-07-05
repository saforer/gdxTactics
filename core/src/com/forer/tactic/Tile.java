package com.forer.tactic;

import com.badlogic.gdx.graphics.Texture;

public class Tile {
    public Texture img;
    public int tileHeight = 0;
    public static Texture grassImg;
    public static Texture waterImg;
    public static Texture roadImg;
    public boolean enabled = true;
    public Tile () {
        if (grassImg == null) grassImg = new Texture("grass.png");
        if (roadImg == null) roadImg = new Texture("road.png");
        if (waterImg == null) waterImg = new Texture("water.png");
        img = grassImg;
    }

    public void changeTile(String s) {
        switch (s) {
            case "Grass":
            default:
                img = grassImg;
                break;
            case "Road":
                img = roadImg;
                break;
            case "Water":
                img = waterImg;
                break;
        }
    }
}
