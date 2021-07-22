package com.lol.fraud;

import com.badlogic.gdx.math.Vector2;
import java.util.*;

public class HexUtils {
    public final Orientation orientation;
    public final Vector2 size;
    public final Vector2 origin;
    static Orientation pointy = new Orientation(
            (float)Math.sqrt(3.0),
            (float)Math.sqrt(3.0) / 2.0f,
            0.0f, 3.0f / 2.0f,
            (float)Math.sqrt(3.0) / 3.0f,
            -1.0f / 3.0f, 0.0f,
            2.0f / 3.0f, 0.5f);
    static Orientation flat = new Orientation(
            3.0f / 2.0f,
            0.0f,
            (float)Math.sqrt(3.0) / 2.0f,
            (float)Math.sqrt(3.0),
            2.0f / 3.0f, 0.0f,
            -1.0f / 3.0f,
            (float)Math.sqrt(3.0) / 3.0f,
            0.0f);
    ArrayList<HexTile> grid = new ArrayList<>();
    HashMap<String, HexTile> gridMap = new HashMap<String, HexTile>();

    public HexUtils(Orientation orientation, Vector2 size, Vector2 origin){
            this.orientation = orientation;
            this.size = size;
            this.origin = origin;
    }
    public void generateGrid(int w, int h){
        for (int r = 0; r < h; r++) {
            int r_offset = (int)Math.floor(r/2f); // or r>>1
            for (int q = -r_offset; q < w - r_offset; q++) {
                HexTile hex = new HexTile(q, r, -q-r);
                String pos = hex.q + "," + hex.r + "," + hex.s;
                hex.y = hexToPixel(hex).y;
                grid.add(hex);
                gridMap.put(pos,hex);
            }
        }
        //Sort back to front so normal iterating gives us proper y-sorting for rendering
        Collections.sort( grid, new Comparator<HexTile>() {
            public int compare (HexTile h1, HexTile h2) {
                return (int)(h2.y - h1.y);
            }
        });
    }
    public ArrayList<Vector2> polygonCorners(HexTile h) {
        ArrayList<Vector2> corners = new ArrayList<>();
        Vector2 center = hexToPixel(h);
        for (int i = 0; i < 6; i++){
            Vector2 offset = hexCornerOffset(i);
            corners.add(new Vector2(center.x + offset.x, center.y + offset.y));
        }
        return corners;
    }
    public float[] polygonLocalCorners(HexTile h) {
        float[] corners = new float[12];
        for (int i = 0; i < 6; i+=2){
            Vector2 offset = hexCornerOffset(i);
            corners[i] = offset.x;
            corners[i+1] = offset.y;
        }
        return corners;
    }
    public Vector2 hexToPixel(HexTile h) {
        float x = (orientation.f0 * h.q + orientation.f1 * h.r) * size.x;
        float y = (orientation.f2 * h.q + orientation.f3 * h.r) * size.y;
        return new Vector2(x + origin.x, y + origin.y);
    }
    public Vector2 hexCornerOffset(int corner) {
        double angle = 2.0 * Math.PI * (orientation.start_angle - corner) / 6.0;
        return new Vector2(size.x * (float)Math.cos(angle), size.y * (float)Math.sin(angle));
    }
    public FractionalHexTile pixel_to_hex(Vector2 p) {
            Vector2 pt = new Vector2(
                    (p.x - origin.x) / size.x,
                    (p.y - origin.y) / size.y);
            double q = orientation.b0 * pt.x + orientation.b1 * pt.y;
            double r = orientation.b2 * pt.x + orientation.b3 * pt.y;
            return new FractionalHexTile(q, r, -q - r);
    }
    public ArrayList<HexTile> getHexesInRing(HexTile h, int r){
        ArrayList<HexTile> ring = new ArrayList<>();
        String key;
        int c = 0;
        for(int i=-r;i<=r;i++){
            for(int j=-r;j<=r;j++){
                for(int k=-r;k<=r;k++){
                    c = Math.max(Math.abs(i), Math.max(Math.abs(j), Math.abs(k)));
                    if(c!=r)continue;
                    int nq = h.q+i;
                    int nr = h.r+j;
                    int ns = h.s+k;
                    key = nq + "," + nr + "," + ns;
                    ring.add(gridMap.get(key));
                }
            }
        }
        return ring;
    }
    public ArrayList<HexTile> getHexesInRadius(HexTile h, int r){
        ArrayList<HexTile> radius = new ArrayList<>();
        String key;
        for(int i=-r;i<=r;i++){
            for(int j=-r;j<=r;j++){
                for(int k=-r;k<=r;k++){
                    if(i==0&&j==0&&k==0)continue;
                    int nq = h.q+i;
                    int nr = h.r+j;
                    int ns = h.s+k;
                    key = nq + "," + nr + "," + ns;
                    radius.add(gridMap.get(key));
                }
            }
        }
        return radius;
    }
}
class Orientation {
    //this is some weird voodoo math, don't touch it.
    public final float f0;
    public final float f1;
    public final float f2;
    public final float f3;
    public final float b0;
    public final float b1;
    public final float b2;
    public final float b3;
    public final float start_angle;
    public Orientation(float f0, float f1, float f2, float f3, float b0, float b1, float b2, float b3, float start_angle) {
        this.f0 = f0;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.b0 = b0;
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
        this.start_angle = start_angle;
    }
}