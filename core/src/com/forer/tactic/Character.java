package com.forer.tactic;

import com.badlogic.gdx.graphics.Texture;

public class Character {
    Coordinates pos;
    static Texture nw, sw, ne, se;
    public String facing = "NE";
    Texture img;

    public Character() {
        startup();
        img = ne;
        pos = new Coordinates();
    }

    public Character(int x, int y) {
        startup();
        img = ne;
        pos = new Coordinates(x,y);

    }

    public Character(int x, int y, String facing) {
        startup();
        facingUpdate(facing);
        pos = new Coordinates(x,y);

    }

    static void startup() {
        if (nw==null) {
            nw = new Texture("nw.png");
            sw = new Texture("sw.png");
            ne = new Texture("ne.png");
            se = new Texture("se.png");
        }
    }

    public void facingUpdate(String str) {
        switch (str) {
            case "NW":
            default:
                img = nw;
                facing = "NW";
                break;
            case "NE":
                img = ne;
                facing = "NE";
                break;
            case "SW":
                img = sw;
                facing = "SW";
                break;
            case "SE":
                img = se;
                facing = "SE";
                break;
        }
    }
}
