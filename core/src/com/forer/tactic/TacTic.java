package com.forer.tactic;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class TacTic extends ApplicationAdapter {
	SpriteBatch batch;
	WorldTiles worldTiles;
	int width = 5;
	int height = 10;


	@Override
	public void create () {
		batch = new SpriteBatch();
		worldTiles = new WorldTiles(width, height);

	}

	void Update() {
		worldTiles.update();
	}

	@Override
	public void render () {
		Update();

		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		worldTiles.draw(batch);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		worldTiles.dispose();
	}
}
