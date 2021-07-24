package com.lol.fraud;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSorter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GDHeX extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	OrthographicCamera camera;
	Viewport viewport;
	HexUtils hU;
	Vector2 mouse = new Vector2();
	DistanceFieldShader fontShader;
	ShapeDrawer sd;
	Texture fontTex;
	BitmapFont font;
	Pixmap whitePixmap;
	GlyphLayout layout = new GlyphLayout();
	ArrayList<HexTile> radius = new ArrayList<>();
	ArrayList<HexTile> ring = new ArrayList<>();
	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);
		Gdx.graphics.setUndecorated(true);
		Gdx.graphics.setWindowedMode(800,450);
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,450);
		camera.position.x-=100;
		camera.position.y-=100;
		viewport = new FitViewport(800,450,camera);
		viewport.update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		hU = new HexUtils(HexUtils.pointy,new Vector2(32,32),new Vector2(0,0));
		hU.generateRectangularGrid(10,10, HexUtils.TYPE.EVENR);
		whitePixmap = new Pixmap(1,1,Pixmap.Format.RGBA8888);
		whitePixmap.drawPixel(0,0,Color.WHITE.toIntBits());
		TextureRegion whitePixel = new TextureRegion(new Texture(whitePixmap));
		sd = new ShapeDrawer(batch,whitePixel);
		fontShader = new DistanceFieldShader();
		fontTex = new Texture(Gdx.files.internal("textures/newfont.png"),true);
		fontTex.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("newfont.fnt"), new TextureRegion(fontTex), false);
		font.setUseIntegerPositions(false);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		radius = hU.getHexesInRadius(hU.pixelToHex(mouse),2);
		ring = hU.getHexesInRing(hU.pixelToHex(mouse),4);
		setMouse();
		batch.begin();
		for(HexTile h: hU.grid ){
			sd.setColor(Color.WHITE);
			sd.filledPolygon(hU.hexToPixel(h).x,hU.hexToPixel(h).y,6,30,30,30*MathUtils.degRad);
		}
		for(HexTile h: radius){
			if(h==null)continue;
			sd.setColor(Color.FIREBRICK);
			sd.filledPolygon(hU.hexToPixel(h).x,hU.hexToPixel(h).y,6,30,30,30*MathUtils.degRad);
		}
		for(HexTile h: ring){
			if(h==null)continue;
			sd.setColor(Color.GOLD);
			sd.filledPolygon(hU.hexToPixel(h).x,hU.hexToPixel(h).y,6,30,30,30*MathUtils.degRad);
		}
		batch.setShader(fontShader);
		font.getData().setScale(0.5f);
		layout.setText(font,hU.getOffsetCoordinate(hU.pixelToHex(mouse), HexUtils.TYPE.ODDR).toString());
		fontShader.setSmoothing(1/8f);
		font.draw(batch,layout,camera.position.x-390,camera.position.y-180);
		batch.setShader(null);
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
		whitePixmap.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width,height);
		sd.update();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		if(amountY > 0){
			camera.zoom+=0.5f;
		}else if(amountY<0){
			camera.zoom-=0.5f;
		}
		return false;
	}
}