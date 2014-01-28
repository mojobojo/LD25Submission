package dev.mojobojo.ld25;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
	
	//private String path;
	public int width, height;
	public int[] pixels;
	public final int SIZE = 32;
	
	private int textureId;
	float scale = 0.5f;
	
	public Texture() {
	}
	
	public Texture(int id, float scale, int width, int height) {
		this.textureId = id;
		this.scale = scale;
		this.width = width;
		this.height = height;
	}
	
	public void loadFile(String path) {
		try {
			//this.path = path;
			BufferedImage image = ImageIO.read(Texture.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
			
			textureId = glGenTextures();
		    glBindTexture(GL_TEXTURE_2D, textureId);

			
			ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = pixels[x + y * width];
					
					int r = ((pixel >> 16) & 0xFF);
	                int g = ((pixel >> 8) & 0xFF);
	                int b = (pixel & 0xFF);
	                int a = ((pixel >> 24) & 0xFF);
	                
	                buffer.put((byte)r);
	                buffer.put((byte)g);
	                buffer.put((byte)b);
	                buffer.put((byte)a);
				}
			}
			
			buffer.flip();
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int getTexture(int xIndex, int yIndex) {
		int textureID = glGenTextures();
	    glBindTexture(GL_TEXTURE_2D, textureID);

	    ByteBuffer buffer = BufferUtils.createByteBuffer(32 * 32 * 4); // 64 * 64 * 4
	    
	    for(int y = 0; y < SIZE; y++) {    	
	    	int yp = yIndex * 32 + y;
	    	
            for(int x = 0; x < SIZE; x++) {
            	int xp = xIndex * 32 + x;
            	
                int pixel = pixels[xp + yp * width];
                
                int r = ((pixel >> 16) & 0xFF);
                int g = ((pixel >> 8) & 0xFF);
                int b = (pixel & 0xFF);
                int a = ((pixel >> 24) & 0xFF);
                
                buffer.put((byte)r);
                buffer.put((byte)g);
                buffer.put((byte)b);
                buffer.put((byte)a);
            }
        }

	    // buffer gets flipped because everything was put backwards
        buffer.flip();
       
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 32, 32, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		return textureID;
	}
	
	void swapColor(int colorOrig, int colorNew) {
		for (int i = 0; i < pixels.length; i++) {
			if ((pixels[i] & 0xFFFFFF) == colorOrig) {
				pixels[i] = 0xFF000000 | colorNew;
			}
		}
	}
	
	public void draw(float x, float y, float scale) {
		glBindTexture(GL_TEXTURE_2D, textureId);
		
		float widthScaled = width * scale;
		float heightScaled = height * scale;
		
		glBegin(GL_QUADS);
		{
			glColor3f(1.0f, 1.0f, 1.0f);

			glTexCoord2d(0.0, 1.0);
			glVertex3f(x, y + heightScaled, 0);

			glTexCoord2d(1.0, 1.0);
			glVertex3f(x + widthScaled, y + heightScaled, 0);

			glTexCoord2d(1.0, 0.0);
			glVertex3f(x + widthScaled, y, 0);

			glTexCoord2d(0.0, 0.0);
			glVertex3f(x, y, 0);
		}
		glEnd();
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
