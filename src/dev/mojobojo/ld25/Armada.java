package dev.mojobojo.ld25;

import java.util.ArrayList;
import java.util.Random;

public class Armada {
	public int armadaCount;
	public ArrayList<Vector2d> ufos;
	public Texture texture;
	private Random random = new Random();
	private float scale = 0.1f;
	
	public Armada(int amountToSpawn) {
		armadaCount = amountToSpawn;
		ufos = new ArrayList<Vector2d>();
		
		for (int i = 0; i < amountToSpawn; i++) {
			ufos.add(new Vector2d(-100 + random.nextInt(100), -100 + random.nextInt(100)));
		}
	}
	
	public void update() {
	}
	
	public void draw() {
		for (Vector2d vec : ufos) {
			texture.draw((float)vec.x, (float)vec.y, scale);
		}
	}
	
	public double getScaledWidth() {
		return (texture.width * (scale + 0.3f));
	}
	
	public double getScaledHeight() {
		return (texture.height * (scale + 0.3f));
	}
}
