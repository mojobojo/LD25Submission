package dev.mojobojo.ld25;

public class Font {
	
	float size = 1.0f;
	Texture[] alphabet;
	Texture[] numbers;

	public Font(float size, int color) {
		this.size = size;
		Texture fontMap = new Texture();
		fontMap.loadFile("/font.png");
		
		fontMap.swapColor(0xFFFFFF, color);
		
		alphabet = new Texture[26];
		for (int i = 0; i < 26; i++) {
			alphabet[i] = new Texture(fontMap.getTexture(i, 0), size, 32, 32);
		}
		
		numbers = new Texture[11];
		for (int i = 0; i < 11; i++) {
			numbers[i] = new Texture(fontMap.getTexture(i, 1), size, 32, 32);
		}
	}
	
	public void drawText(double x, double y, String str, float scale) {
		drawText((float)x, (float)y, str, scale);
	}
	
	public void drawText(float x, float y, String str, float scale) {
		char[] chars = str.toLowerCase().toCharArray();

		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			
			if (c >= 'a' && c <= 'z') {
				alphabet[c - 'a'].draw(x + (i * (32 * scale)), y, scale);
			}
			else if (c >= '0' && c <= '9') {
				numbers[c - '0'].draw(x + (i * (32 * scale)), y, scale);
			}
			else if (c == '.') {
				numbers[10].draw(x + (i * (32 * scale)), y, scale);
			}
			else {
				// do nothing
			}
		}
	}
	
	public void drawText(float x, float y, String str) {
		drawText(x, y, str, size);
	}
}

