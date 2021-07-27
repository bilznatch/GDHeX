package com.lol.fraud;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class HexTile {
    /*_________________________________________________________________________________________
     *
     * Before editing this object, consider extending it instead.
     * If you want to add an ID, textures, give it a sprite, etc.,
     * I really recommend making a separate class to reduce clutter.
     * If you need to add a directly hex-related function, please request it be added to the github repo.
     * This allows everyone to get the benefit from whatever work is put in.
     *
     __________________________________________________________________________________________*/
    static final int NW=2,NE=1,W=3,E=0,SW=4,SE=5;
    int q, r, s, weight = 1;
    Vector2 pos;
    HexTile(int q, int r, int s) {
        this.q = q;
        this.r = r;
        this.s = s;
        if (q + r + s != 0) throw new IllegalArgumentException("q + r + s must be 0");
    }
    HexTile(){

    }

    public HexTile add(HexTile b) {
        return new HexTile(q + b.q, r + b.r, s + b.s);
    }


    public HexTile subtract(HexTile b) {
        return new HexTile(q - b.q, r - b.r, s - b.s);
    }


    public HexTile scale(int k) {
        return new HexTile(q * k, r * k, s * k);
    }


    public HexTile rotateLeft() {
        return new HexTile(-s, -q, -r);
    }


    public HexTile rotateRight() {
        return new HexTile(-r, -s, -q);
    }

    static public ArrayList<HexTile> directions = new ArrayList<HexTile>(){
        {
        add(new HexTile(1, 0, -1));
        add(new HexTile(1, -1, 0));
        add(new HexTile(0, -1, 1));
        add(new HexTile(-1, 0, 1));
        add(new HexTile(-1, 1, 0));
        add(new HexTile(0, 1, -1));
        }
    };

    static public HexTile direction(int direction) {
        return HexTile.directions.get(direction);
    }


    public HexTile neighbor(int direction) {
        return add(HexTile.direction(direction));
    }

    static public ArrayList<HexTile> diagonals = new ArrayList<HexTile>(){
        {
            add(new HexTile(2, -1, -1));
            add(new HexTile(1, -2, 1));
            add(new HexTile(-1, -1, 2));
            add(new HexTile(-2, 1, 1));
            add(new HexTile(-1, 2, -1));
            add(new HexTile(1, 1, -2));
        }
    };

    public HexTile diagonalNeighbor(int direction) {
        return add(HexTile.diagonals.get(direction));
    }

    public int length() {
        return (int)((Math.abs(q) + Math.abs(r) + Math.abs(s)) / 2);
    }

    public int distance(HexTile b) {
        return subtract(b).length();
    }

    public boolean equalHex(HexTile h) {
        if(h==null)return false;
        return (q == h.q && s == h.s && r == h.r);
    }
}
