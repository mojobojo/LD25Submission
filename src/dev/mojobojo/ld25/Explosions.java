package dev.mojobojo.ld25;

import java.util.Random;

public class Explosions {
	
	private int explosionAmount;
	private Texture smokeCloud;
	private Vector2d[] cloudLocations;
	private int[] cloudCounters;
	private float[] cloudAnimators;
	private Random random = new Random();
	private int xBounds;
	private int yBounds;
	private Sound sound;
	
	public Explosions(Sound sound) {
		smokeCloud = new Texture();
		smokeCloud.loadFile("/smoke.png");
		this.sound = sound;
	}
	
	public void createExplosions(int amount, int xBounds, int yBounds) {
		this.xBounds = xBounds;
		this.yBounds = yBounds;
		explosionAmount = amount;
		cloudLocations = new Vector2d[explosionAmount];
		cloudCounters = new int[explosionAmount];
		cloudAnimators = new float[explosionAmount];

		for (int i = 0; i < cloudLocations.length; i++) {
			int x = random.nextInt(xBounds);
			int y = random.nextInt(yBounds);
			cloudLocations[i] = new Vector2d(x, y);
			cloudCounters[i] = random.nextInt(1000);
		}
	}
	
	public void update() {
		for (int i = 0; i < cloudLocations.length; i++) {
			cloudCounters[i]--;
			
			if (cloudCounters[i] <= 0) {
				cloudCounters[i] = 0;
				cloudAnimators[i] += 0.03f;
				
				if (cloudAnimators[i] >= 1.0f) {
					cloudAnimators[i] = 1.0f;
				}
			}
			
			if (cloudAnimators[i] >= 1.0f) {
				cloudLocations[i].x = random.nextInt(xBounds);
				cloudLocations[i].y = random.nextInt(yBounds);
				cloudCounters[i] = random.nextInt(1000);
				cloudAnimators[i] = 0.0f;
			}
		}
	}
	
	public void draw(double x, double y) {
		for (int i = 0; i < cloudLocations.length; i++) {
			if (cloudCounters[i] <= 0 && cloudAnimators[i] != 1.0f) {
				if (!sound.playing()) {
					sound.play();
				}
				
				smokeCloud.draw((float)(cloudLocations[i].x + x) - ((smokeCloud.width * cloudAnimators[i]) / 2), (float)(cloudLocations[i].y + y) - ((smokeCloud.height * cloudAnimators[i]) / 2), cloudAnimators[i]);
			}
		}
	}
}
