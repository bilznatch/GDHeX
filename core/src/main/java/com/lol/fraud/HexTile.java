package com.lol.fraud;

import java.util.ArrayList;

public class HexTile {
    int q, r, s;
    float y;
    HexTile(int q, int r, int s) {
        this.q = q;
        this.r = r;
        this.s = s;
        if (q + r + s != 0) throw new IllegalArgumentException("q + r + s must be 0");
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
