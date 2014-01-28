package dev.mojobojo.ld25;


public class AnimatedText {
	
	private Vector2d position;
	private Font font;
	private String text;
	private float size = 0.0f;
	public boolean drawing = true;
	public float speed = 1.0f;

	public AnimatedText(float x, float y, String text, Font font, float speed) {
		position = new Vector2d(x, y);
		this.font = font;
		this.text = text;
		this.speed = speed;
	}
	
	public void update() {
		size += speed;
		position.y -= 1;
		
		if (size > 1.0f) {
			drawing = false;
		}
	}
	
	public void draw(float x, float y) {
		if (drawing) {
			float offset = (size * 32 * text.length()) / 2;
			font.drawText((float)position.x + x - offset, (float)position.y + y, text, size);
		}
	}
}
