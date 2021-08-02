package com.lol.fraud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import javafx.util.Pair;

import java.util.*;

public class HexUtils {
    public final Orientation orientation;
    public final Vector2 size;
    public final Vector2 origin;
    enum TYPE {EVENR,ODDR,EVENQ,ODDQ}
    enum STRETCH {HORIZONTAL,VERTICAL}
    enum SHAPE {RECTANGLE, TRIANGLE, HEXAGON, RHOMBUS}
    static final Orientation pointy = new Orientation(
            (float)Math.sqrt(3.0),
            (float)Math.sqrt(3.0) / 2.0f,
            0.0f, 3.0f / 2.0f,
            (float)Math.sqrt(3.0) / 3.0f,
            -1.0f / 3.0f, 0.0f,
            2.0f / 3.0f, 0.5f);
    static final Orientation flat = new Orientation(
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
    boolean tiledMapLoaded = false;
    int maxLayers = 0;
    float minX = 0, maxX = 0, minY = 0, maxY = 0;
    Vector2 temp = new Vector2();
    public HexUtils(Orientation orientation, Vector2 size, Vector2 origin){
            this.orientation = orientation;
            this.size = size;
            this.origin = origin;
    }
    public HexUtils(Orientation orientation, Vector2 size, Vector2 origin, STRETCH stretch){
        this.orientation = orientation;
        if(stretch==STRETCH.VERTICAL){
            this.size = size.set((float)Math.sqrt(Math.pow(size.x/Math.sqrt(3),2) + Math.pow(size.x,2)),size.y);
        }else if(stretch==STRETCH.HORIZONTAL){
            this.size = size.set(size.x,(float)Math.sqrt(Math.pow(size.y/Math.sqrt(3),2) + Math.pow(size.y,2)));
        }else{
            throw new IllegalArgumentException("Stretch type is invalid.");
        }
        this.origin = origin;
    }
    public void generateGrid(int w, int h, SHAPE shape, TYPE type){
        if(shape==SHAPE.RECTANGLE){
            generateRectangularGrid(w,h,type);
        }else if(shape==SHAPE.TRIANGLE){
            generateTriangularGrid(w,false);
        }else if(shape==SHAPE.RHOMBUS){
            generateRhomboidGrid(w,h,false);
        }else if(shape==SHAPE.HEXAGON){
            generateHexagonalGrid(w);
        }
        getOnScreen(0,0,1920,1080);
    }
    public void generateRectangularGrid(int w, int h, TYPE type){
        clearGrid();
        //Column (q) based types to be added... at some point perhaps.
        if(type == TYPE.ODDR){
            for (int r = 0; r < h; r++) {
                int r_offset = (int)Math.floor(r/2f); // or r>>1
                for (int q = -r_offset; q < w - r_offset; q++) {
                    HexTile hex = new HexTile(q, r, -q-r);
                    hex.pos = hexToPixel(hex);
                    setBounds(hex.pos);
                    gridMap.put(hex.q+","+hex.r+","+hex.s,hex);
                }
            }
        }else if(type == TYPE.EVENR){
            for (int r = 0; r < h; r++) {
                int offset = (int)Math.floor(r/2f); // or r>>1
                for (int s= - offset; s < w - offset; s++) {
                    HexTile hex = new HexTile(-r-s+w, r, s-w);
                    hex.pos = hexToPixel(hex);
                    setBounds(hex.pos);
                    gridMap.put(hex.q+","+hex.r+","+hex.s,hex);
                }
            }
        }
        getOnScreen(0,0,1920,1080);
    }
    public void generateHexagonalGrid(int radius){
        clearGrid();
        for (int q = -radius; q <= radius; q++) {
            int r1 = Math.max(-radius, -q - radius);
            int r2 = Math.min(radius, -q + radius);
            for (int r = r1; r <= r2; r++) {
                HexTile hex = new HexTile(q, r, -q-r);
                hex.pos = hexToPixel(hex);
                setBounds(hex.pos);
                gridMap.put(hex.q+","+hex.r+","+hex.s,hex);
            }
        }
        getOnScreen(0,0,1920,1080);
    }
    public void generateTriangularGrid(int maxwidth, boolean flipy) {
        clearGrid();
        if (flipy) {
            for (int q = 0; q <= maxwidth; q++) {
                for (int r = maxwidth - q; r <= maxwidth; r++) {
                    HexTile hex = new HexTile(q, r, -q - r);
                    hex.pos = hexToPixel(hex);
                    setBounds(hex.pos);
                    gridMap.put(hex.q+","+hex.r+","+hex.s, hex);
                }
            }
        } else {
            for (int q = 0; q <= maxwidth; q++) {
                for (int r = 0; r <= maxwidth - q; r++) {
                    HexTile hex = new HexTile(q, r, -q - r);
                    hex.pos = hexToPixel(hex);
                    setBounds(hex.pos);
                    gridMap.put(hex.q+","+hex.r+","+hex.s, hex);
                }
            }
        }
        getOnScreen(0,0,1920,1080);
    }
    public void generateRhomboidGrid(int w, int h, boolean reverseSkew){
        clearGrid();
        if(reverseSkew){
            for (int s = 0; s < h; s++) {
                for (int r = 0; r < w; r++) {
                    HexTile hex = new HexTile(-r-s+w, r, s-w);
                    hex.pos = hexToPixel(hex);
                    setBounds(hex.pos);
                    gridMap.put(hex.q+","+hex.r+","+hex.s,hex);
                }
            }
        }else{
            for (int r = 0; r < h; r++) {
                for (int q = 0; q < w; q++) {
                    HexTile hex = new HexTile(q, r, -q-r);
                    hex.pos = hexToPixel(hex);
                    setBounds(hex.pos);
                    gridMap.put(hex.q+","+hex.r+","+hex.s,hex);
                }
            }
        }
        getOnScreen(0,0,1920,1080);
    }
    public float[] polygonCorners(HexTile h) {
        float[] corners = new float[12];
        for (int i = 0; i < 12; i+=2){
            Vector2 offset = hexCornerOffset(i);
            corners[i] = h.pos.x + offset.x;
            corners[i+1] = h.pos.y + offset.y;
        }
        return corners;
    }
    public float[] polygonLocalCorners(HexTile h) {
        float[] corners = new float[12];
        for (int i = 0; i < 12; i+=2){
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
        double angle = Math.PI * (orientation.start_angle - corner+0.5f) / 6.0;
        return new Vector2(size.x * (float)Math.cos(angle), size.y * (float)Math.sin(angle));
    }
    public HexTile pixelToHex(Vector2 p) {
        Vector2 pt = new Vector2(
                (p.x - origin.x) / size.x,
                (p.y - origin.y) / size.y);
        double q = orientation.b0 * pt.x + orientation.b1 * pt.y;
        double r = orientation.b2 * pt.x + orientation.b3 * pt.y;
        return new FractionalHexTile(q, r, -q - r).hexRound();
    }
    public HexTile pixelToGridHex(Vector2 p) {
        Vector2 pt = new Vector2(
                (p.x - origin.x) / size.x,
                (p.y - origin.y) / size.y);
        int q = Math.round(orientation.b0 * pt.x + orientation.b1 * pt.y);
        int r = Math.round(orientation.b2 * pt.x + orientation.b3 * pt.y);
        int s = -q-r;
        return getTile(q,r,s);
    }
    public THexTile pixelToGridTHex(int l, Vector2 p){
        Vector2 pt = new Vector2(
                (p.x - origin.x) / size.x,
                (p.y - origin.y) / size.y);
        int q = Math.round(orientation.b0 * pt.x + orientation.b1 * pt.y);
        int r = Math.round(orientation.b2 * pt.x + orientation.b3 * pt.y);
        int s = -q-r;
        return getTile(l,q,r,s);
    }
    public FractionalHexTile pixelToFractionalHex(Vector2 p) {
            Vector2 pt = new Vector2(
                    (p.x - origin.x) / size.x,
                    (p.y - origin.y) / size.y);
            double q = orientation.b0 * pt.x + orientation.b1 * pt.y;
            double r = orientation.b2 * pt.x + orientation.b3 * pt.y;
            return new FractionalHexTile(q, r, -q - r);
    }
    public int[] getIntegerQRSfromXY(float x, float y){
        x = (x-origin.x)/size.x;
        y = (y-origin.y)/size.y;
        int q = Math.round(orientation.b0 * x + orientation.b1 * y);
        int r = Math.round(orientation.b2 * x + orientation.b3 * y);
        int s = -q-r;
        return new int[]{q,r,s};
    }
    public Vector2 pixelToOffsetHex(Vector2 mouse, TYPE type){
        return(getOffsetCoordinate(pixelToHex(mouse),type));
    }
    public ArrayList<HexTile> getHexesInRing(HexTile h, int r){
        ArrayList<HexTile> ring = new ArrayList<>();
        for(int i=-r;i<=r;i++){
            for(int j=-r;j<=r;j++){
                for(int k=-r;k<=r;k++){
                    if(Math.max(Math.abs(i), Math.max(Math.abs(j), Math.abs(k)))!=r)continue;
                    ring.add(getTile((h.q+i) + "," + (h.r+j) + "," + (h.s+k)));
                }
            }
        }
        return ring;
    }
    public ArrayList<HexTile> getHexesInRadius(HexTile h, int r){
        ArrayList<HexTile> radius = new ArrayList<>();
        for(int i=-r;i<=r;i++){
            for(int j=-r;j<=r;j++){
                for(int k=-r;k<=r;k++){
                    if(i==0&&j==0&&k==0)continue;
                    radius.add(getTile((h.q+i) + "," + (h.r+j) + "," + (h.s+k)));
                }
            }
        }
        return radius;
    }
    public HexTile getTile(String pos){
        return gridMap.get(pos);
    }
    public HexTile getTile(int q, int r, int s){
        return gridMap.get(q+","+r+","+s);
    }
    public THexTile getTile(int l, int q, int r, int s){
        return (THexTile) gridMap.get(l+","+q+","+r+","+s);
    }
    public Vector2 getOffsetCoordinate(HexTile h, TYPE type){
        //0 is odd, 1 is even (probably)
        if(type == TYPE.ODDR){
            int col = h.q + (h.r - (h.r&1)) / 2;
            int row = h.r;
            return new Vector2(col,row);
        }else if(type==TYPE.EVENR){
            int col = h.q + (h.r + (h.r&1)) / 2;
            int row = h.r;
            return new Vector2(col,row);
        }else if(type==TYPE.ODDQ){
            int col = h.q + (h.r + (h.r&1)) / 2;
            int row = h.r;
            return new Vector2(col,row);
        }else if(type==TYPE.EVENQ){
            int col = h.q + (h.r + (h.r&1)) / 2;
            int row = h.r;
            return new Vector2(col,row);
        }else{
            return new Vector2(0,0);
        }
    }
    public void clearGrid(){
        grid.clear();
        gridMap.clear();
    }
    public void getOnScreen(Vector2 cameraPosition, Vector2 screenSize){
        float x = Math.max(cameraPosition.x - screenSize.x/2,minX);
        float y = Math.max(cameraPosition.y - screenSize.y/2,minY);
        grid.clear();
        if(tiledMapLoaded){
            for(int k = maxLayers; k > 0; k--) {
                for (float j = y + screenSize.y + size.y; j >= 0; j -= size.y * 1.5f) {
                    for (float i = x; i < x + screenSize.x + size.x; i += size.x * 1.5f) {
                        temp.set(i, j);
                        HexTile h = pixelToGridTHex(k,temp);
                        if (h == null) continue;
                        grid.add(h);
                    }
                }
            }
        }else{
            for (float j = y + screenSize.y + size.y; j >= 0; j -= size.y * 1.5f) {
                for (float i = x; i < x + screenSize.x + size.x; i += size.x * 1.5f) {
                    temp.set(i, j);
                    HexTile h = pixelToGridHex(temp);
                    if (h == null) continue;
                    grid.add(h);
                }
            }
        }
    }
    public void getOnScreen(float cameraX, float cameraY, float screenWidth, float screenHeight){
        float x = Math.max(cameraX - screenWidth/2,minX);
        float y = Math.max(cameraY - screenHeight/2,minY);
        float endX = Math.min(cameraX + screenWidth/2,maxX);
        float endY = Math.min(cameraY + screenHeight/2,maxY);
        if(x-size.x<=minX&&endX+size.x>=maxX&&y-size.y<=minY&&endY+size.y>=maxY&&grid.size()==gridMap.size())return;
        grid.clear();
        if(tiledMapLoaded){
            for(int k = maxLayers; k >= 0; k--) {
                for (float j = endY + size.y; j >= y-size.y*3; j -= size.y * 1.2f) {
                    for (float i = x-size.x; i < endX + size.x; i += size.x * 1.5f) {
                        temp.set(i, j);
                        HexTile h = pixelToGridTHex(k,temp);
                        if (h == null) continue;
                        grid.add(h);
                    }
                }
            }
        }else{
            for (float j = endY+size.y; j >= y-size.y*2; j -= size.y * 1.5f) {
                for (float i = x-size.x; i < endX + size.x*2; i += size.x * 1.6f) {
                    temp.set(i, j);
                    HexTile h = pixelToGridHex(temp);
                    if (h == null) continue;
                    grid.add(h);
                }
            }
        }
    }
    public void getFullGrid(){
        for(Map.Entry<String,HexTile> entry: gridMap.entrySet()){
            grid.add(entry.getValue());
        }
    }
    public HashMap<HexTile,HexTile> getPath(HexTile start, HexTile end){
        PathfindingQueue<HexTile> frontier = new PathfindingQueue<>();
        frontier.enqueue(start,0);
        HashMap<HexTile, HexTile> cameFrom = new HashMap<>();
        HashMap<HexTile, Integer> costSoFar = new HashMap<>();
        cameFrom.put(start,start);
        costSoFar.put(start,0);
        while(frontier.count()>0){
            HexTile current = frontier.dequeue();
            if(current.equalHex(end)){
                break;
            }
            for(int i = 0; i < 6;i++){
                HexTile next = getTile(current.neighbor(i).q+","+current.neighbor(i).r+","+current.neighbor(i).s);
                if(next==null || next.weight > 100)continue;
                int newCost = costSoFar.get(current) + next.weight;
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next,newCost);
                    int priority = newCost + next.distance(end);
                    frontier.enqueue(next, priority);
                    cameFrom.put(next,current);
                }
            }

        }
        return cameFrom;
    }
    public void setRandomWeights(){
        for(Map.Entry<String, HexTile> entry: gridMap.entrySet()){
                entry.getValue().weight=MathUtils.random(0,10);
        }
    }
    public void parseTiledMap(TiledMap map){
        tiledMapLoaded = true;
        clearGrid();
        int offset = 0, tilew = 0, tileh = 0, layer = 0;
        int[] qrs = new int[]{0,0,0};
        float x = 0, y = 0;
        boolean staggerAxisX = true, staggerIndexEven = false;
        String axis = map.getProperties().get("staggeraxis", String.class);
        if (axis != null) {
            if (axis.equals("x")) {
                staggerAxisX = true;
            } else {
                staggerAxisX = false;
            }
        }

        String index = map.getProperties().get("staggerindex", String.class);
        if (index != null) {
            if (index.equals("even")) {
                staggerIndexEven = true;
            } else {
                staggerIndexEven = false;
            }
        }
        tilew = map.getProperties().get("tilewidth", Integer.class);
        tileh = map.getProperties().get("tileheight", Integer.class);

        for(MapLayer ml: map.getLayers()){
            if(ml instanceof TiledMapTileLayer){
                TiledMapTileLayer t = (TiledMapTileLayer)ml;
                for(int i = 0; i < t.getWidth();i++){
                    for(int j = 0; j < t.getHeight();j++){
                        if(t.getCell(i,j)==null)continue;
                        if(staggerAxisX){
                            //because we're staggering along X axis, the width remains constant at 0.75x
                            x = i*(tilew*0.75f);
                            y = j*tileh;
                            //stagger even or odd
                            if(staggerIndexEven){
                                if(i%2==0){
                                    y-=tileh/2f;
                                }
                            }else{
                                if((i+1)%2==0){
                                    y-=tileh/2f;
                                }
                            }
                        }else{
                            //because we're staggering along Y axis, the height remains constant at 0.75x
                            x = i*tilew;
                            y = j*(tileh*0.75f);
                            //stagger even or odd
                            if(staggerIndexEven){
                                if(j%2==0){
                                    x-=tilew/2f;
                                }
                            }else{
                                if((j+1)%2==0){
                                    x-=tilew/2f;
                                }
                            }
                        }
                        //center the tiles.
                        x-=tilew/2f;
                        y-=tileh/2f;
                        if(x < minX)minX=x;
                        if(x>maxX)maxX=x;
                        if(y<minY)minY=y;
                        if(y>maxY)maxY=y;
                        qrs=getIntegerQRSfromXY(x,y);
                        THexTile tHex = new THexTile(layer,
                                t.getCell(i,j).getTile(),
                                qrs[0],
                                qrs[1],
                                qrs[2],
                                x,
                                y);
                        gridMap.put(tHex.getLayer()+","+tHex.q+","+tHex.r+","+tHex.s,tHex);
                    }
                }
                layer++;
            }
        }
        this.maxLayers = layer;
        getFullGrid();
    }
    public void setBounds(Vector2 pos){
        if(pos.x>maxX)maxX=pos.x;
        if(pos.x<minX)minX=pos.x;
        if(pos.y>maxY)maxY=pos.y;
        if(pos.y<minY)minY=pos.y;
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
class PathfindingQueue<T> {
    private List<Pair<T, Integer>> elements = new ArrayList<Pair<T, Integer>>();

    public int count(){
        return elements.size();
    }

    public void enqueue(T item, int priority){
        elements.add(new Pair(item, priority));
    }

    public T dequeue()
    {
        int bestIndex = 0;

        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getValue() < elements.get(bestIndex).getValue()) {
                bestIndex = i;
            }
        }

        T bestItem = elements.get(bestIndex).getKey();
        elements.remove(bestIndex);
        return bestItem;
    }
}
