package com.lol.fraud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Vector2;

public class THexTile extends HexTile implements TiledMapTile {
    private int id;

    private BlendMode blendMode = BlendMode.ALPHA;

    private MapProperties properties;

    private MapObjects objects;

    private TextureRegion textureRegion;

    private float offsetX;

    private float offsetY;

    private int layer = 0;
    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public BlendMode getBlendMode() {
        return this.blendMode;
    }

    @Override
    public void setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
    }

    @Override
    public TextureRegion getTextureRegion() {
        return this.textureRegion;
    }

    @Override
    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public float getOffsetX() {
        return this.offsetX;
    }

    @Override
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    @Override
    public float getOffsetY() {
        return this.offsetY;
    }

    @Override
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public MapProperties getProperties() {
        return this.properties;
    }

    @Override
    public MapObjects getObjects() {
        return this.objects;
    }

    public int getLayer(){
        return this.layer;
    }

    public THexTile(int layer, TiledMapTile t, int q, int r, int s, float x, float y){
        this.layer = layer;
        this.q = q;
        this.r = r;
        this.s = s;
        this.pos = new Vector2(x,y);
        this.textureRegion = t.getTextureRegion();
        this.blendMode = t.getBlendMode();
        this.id = t.getId();
        this.objects = t.getObjects();
        this.properties = t.getProperties();
    }
}
