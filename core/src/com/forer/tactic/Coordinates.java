package com.forer.tactic;

public class Coordinates {
    public int x;
    public int y;

    public Coordinates () {

    }
    public Coordinates (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinates moveUp (Coordinates tileIn) {
        Coordinates tileOut = new Coordinates(tileIn.x, tileIn.y);
        tileOut.y++;
        if (tileIn.y % 2 != 0) {
            tileOut.x++;
        }
        return tileOut;
    }

    public static Coordinates moveDown (Coordinates tileIn) {
        Coordinates tileOut = new Coordinates(tileIn.x, tileIn.y);
        tileOut.y--;
        if (tileIn.y % 2 == 0) {
            tileOut.x--;
        }
        return tileOut;
    }

    public static Coordinates moveLeft (Coordinates tileIn) {
        Coordinates tileOut = new Coordinates(tileIn.x, tileIn.y);
        if (tileIn.y % 2 == 0) {
            tileOut.x--;
        }
        tileOut.y++;
        return tileOut;
    }

    public static Coordinates moveRight (Coordinates tileIn) {
        Coordinates tileOut = new Coordinates(tileIn.x, tileIn.y);
        if (tileIn.y % 2 != 0) {
            tileOut.x++;
        }
        tileOut.y--;
        return tileOut;
    }
}
