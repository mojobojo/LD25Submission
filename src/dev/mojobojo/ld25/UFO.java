package dev.mojobojo.ld25;

import static org.lwjgl.opengl.GL11.*;

public class UFO {
	
	public Texture texture;
	public Vector2d position;
	private float counter;
	private float scale = 0.3f;

	public UFO() {
		texture = new Texture();
		texture.loadFile("/ufo.png");
		position = new Vector2d(330, GameMain.height - texture.height);
	}
	
	public void update() {
		counter++;
	}
	
	public void draw() {
		glPushMatrix();
		
		glRotatef((float)(Math.sin(counter / 10.0) / 3.0), 0.0f, 0.0f, 1.0f);
		
		texture.draw((float)position.x, (float)position.y, 0.3f);
		
		glPopMatrix();
	}
	
	public double getScaledWidth() {
		return (texture.width * (scale + 0.3f));
	}
	
	public double getScaledHeight() {
		return (texture.height * (scale + 0.3f));
	}
}
