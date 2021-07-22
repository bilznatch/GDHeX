package com.lol.fraud;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GDHeX extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;
	Viewport viewport;
	HexUtils hU;
	Vector2 mouse = new Vector2();
	ShapeDrawer sd;
	Pixmap whitePixmap;
	@Override
	public void create() {
		Gdx.graphics.setUndecorated(true);
		Gdx.graphics.setWindowedMode(800,450);
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,450);
		viewport = new FitViewport(800,450,camera);
		viewport.update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		hU = new HexUtils(HexUtils.pointy,new Vector2(32,32),new Vector2(0,0));
		hU.generateGrid(100,100);
		whitePixmap = new Pixmap(1,1,Pixmap.Format.RGBA8888);
		whitePixmap.drawPixel(0,0,Color.WHITE.toIntBits());
		TextureRegion whitePixel = new TextureRegion(new Texture(whitePixmap));
		sd = new ShapeDrawer(batch,whitePixel);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(HexTile h: hU.grid ){
			sd.filledPolygon(hU.hexToPixel(h).x,hU.hexToPixel(h).y,6,30,30,30*MathUtils.degRad);
		}
		batch.end();
		camera.update();
	}
	public void setMouse(){
		mouse.set(Gdx.input.getX(),Gdx.input.getY());
		viewport.unproject(mouse);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width,height);
		sd.update();
	}
}