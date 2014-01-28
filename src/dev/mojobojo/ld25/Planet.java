package dev.mojobojo.ld25;

import java.util.Random;

public class Planet {

	public Texture texture;
	public Texture destroyedTexture;
	public Vector2d position;
	private Random random;
	private float scale;
	public String planetName;
	public double distanceFromUfo = 0.0f;
	public double chancesToWin = 0.0f;
	public int health = 0;
	public boolean destroyed = false;
	public boolean invaded = false;
	public int percentInvaded = 0;
	
	public Planet(Texture texture, double x, double y) {
		this.texture = texture;
		random = new Random();
		position = new Vector2d(x, y);
		scale = Math.abs(0.5f - random.nextFloat());
		position.y -= texture.height;
		chancesToWin = 100 - (random.nextFloat() * 100) + 1;
		health = (int)(scale * 1000);
	}
	
	public void update() {
	}
	
	public void draw(float x, float y) {
		if (destroyed) {
			destroyedTexture.draw(x, y, 0.3f + scale);
		}
		else {
			texture.draw(x, y, 0.3f + scale);
		}
	}
	
	public double getScaledWidth() {
		return (texture.width * (scale + 0.3f));
	}
	
	public double getScaledHeight() {
		return (texture.height * (scale + 0.3f));
	}
}
