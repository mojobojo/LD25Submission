package dev.mojobojo.ld25;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DITHER;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glShadeModel;

//import java.io.BufferedOutputStream;
//import java.io.FileOutputStream;
//import java.io.ObjectOutput;
//import java.io.ObjectOutputStream;
//import java.io.OutputStream;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class GameMain {
	
	public static final int width = 800;
	public static final int height = 450;
	
	public int timer = 0;
	private long time;
	@SuppressWarnings("unused")
	private int fps = 0;
	private Random random = new Random();
	private Space space;
	private Font font;
	public static boolean[] keys = new boolean[256];
	public static int mouseX;
	public static int mouseY;
	private boolean drawFindPlanet = true;
	private String stringToDraw = "";
	private boolean paused = false;
	private boolean pIsDown = false;
	private double counter = 0.0;
	private float pausePos = 0.0f;
	//private boolean yIsDown = false;
	private boolean enterIsDown = false;
	
	private String[] info = new String[] 
			{
			"find a planet",
			"e to view stats",
			"m to view mini map"
			};
	
	private boolean showingStory = true;
	
	private String[] story = new String[] 
			{
			"The year is 5966. Francium has been used as a",
			"",
			" super efficent fuel for deep space vessels",
			"",
			"      for thousands of years until...",
			"",
			" the mines in the eastern sector of Renalbor",
			"",
			"                  ran dry.",
			"",
			"      Now your race is in a rush to find",
			"",
			"     a new place to mine. You command your",
			"",
			"      fleet into an uncharted part of the",
			"",
			"   Yainet nebula and discover a small system",
			"",
			"               of 100 planets.",
			"",
			" Destroy and invade the planets in preparation",
			"",
			"            to mine out the area.",
			"",
			"Hit Enter..."
			};
	
	private String[] gameInfo = new String[] 
			{
			"Destroy a planet and your armada gains power.",
			"",
			"Invade a planet and your armada gets bigger.",
			"",
			"If a planet is invaded it can be taken back so",
			"",
			"Look for a blinking blip on your map for it.",
			"",
			"Look at the planets info before you attack it.",
			"",
			"Attack planets that are easier to attack first.",
			"",
			"When you do damage it will show on the planet.",
			"",
			"Percentage shows on the planet as you",
			"",
			"take it over.",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"Hit Enter..."
			};
	
	private String[] controlsInfo = new String[] 
			{
			"W S A D and Arrow Keys for movement.",
			"",
			"Q to select planet.",
			"",
			"E to view stats.",
			"",
			"Z to destroy planet.",
			"",
			"X to invade planet.",
			"",
			"C to view planet info.",
			"",
			"M to view map",
			"",
			"P to pause",
			"",
			"SPACE to boost",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"Hit Enter..."
			};

	private int storyIndex = 0;

	void start() {
		try {
			Display.setTitle("LD25");
			Display.setFullscreen(false);
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// setup opengl
		glShadeModel(GL_SMOOTH);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0.0f, width, height, 0.0f, 0.0f, 1);
		glMatrixMode(GL_MODELVIEW);
	    glDisable(GL_LIGHTING);
	    glDisable(GL_DITHER);
	    glDisable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);

		// cornflower blue
		//glClearColor(100.0f / 255.0f, 149.0f / 255.0f, 237.0f / 255.0f, 1.0f);
		
		// black
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		//lets set up some class vars
		space = new Space(20000, 10000);
		font = new Font(0.5f, 0xFFFF00);
		
		// this is for updating the game at 60 ups but continue rendering
		// at the max fps
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		
		while (!Display.isCloseRequested()) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			// update only at 60 times per second
			while (delta >= 1) {
				update();
				delta--;
			}
			
			// render as fast as possible
			render();
			
			if (System.currentTimeMillis() - time > 1000) {
				time = System.currentTimeMillis();

				//System.out.println("FPS " + fps);
				fps = 0;
				
				drawFindPlanet = !drawFindPlanet;
				stringToDraw = info[random.nextInt(info.length)];
			}
			
			fps++;
			
			Display.update();
		}
		
		Display.destroy();
	}
	

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		if (!showingStory) {
			if (!paused) {
				space.draw();
	
				if (drawFindPlanet ) {
					font.drawText(10,  10, stringToDraw);
				}
			}
			else {
				font.drawText(width / 2 - (3 * 32), height / 2 + pausePos, "Paused", 1.0f);
			}
		}
		else {
			if (storyIndex == 0) {
				for (int i = 0; i < story.length; i++) {
					font.drawText(35, 10 + i * 17, story[i], 0.5f);
				}
			}
			if (storyIndex == 1) {
				for (int i = 0; i < gameInfo.length; i++) {
					font.drawText(35, 10 + i * 17, gameInfo[i], 0.5f);
				}
			}
			if (storyIndex == 2) {
				for (int i = 0; i < controlsInfo.length; i++) {
					font.drawText(35, 10 + i * 17, controlsInfo[i], 0.5f);
				}
			}
		}
	}


	private void update() {
		for (int i = 0; i < 256; i++) {
			keys[i] = Keyboard.isKeyDown(i);
		}
		
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		
		if (keys[Keyboard.KEY_RETURN] && !enterIsDown) {
			storyIndex++;
			
			if (storyIndex >= 3) {
				showingStory = false;
			}
			
			enterIsDown = true;
		}
		if (!keys[Keyboard.KEY_RETURN]) {
			enterIsDown = false;
		}
		
		if (!showingStory) {
			if (keys[Keyboard.KEY_P] && !pIsDown) {
				paused = !paused;
				pIsDown = true;
			}
			if (!keys[Keyboard.KEY_P]) {
				pIsDown = false;
			}
			
			if (!paused)
			{
				space.update();
			}
			else {
				pausePos = (float)Math.sin(counter / 10) * 15;
				counter++;
			}
		}
		else {
			
		}
	}
}
