package dev.mojobojo.ld25;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;


public class Space {
	
	private Random random = new Random();
	private Vector2d[] stars;
	private Vector2d worldPos = new Vector2d(-100, -100);

	public float starSpeed = 2.0f;
	private UFO ufo;
	private Planet[] planets;
	private Texture[] planetTexures;
	private Texture destoryedPlanet;
	private Font font;
	private float spaceBounds;
	private PlanetNameGen planNameGen;
	private Texture hudTexture;
	private ArrayList<Planet> planetsInSight;
	private int chosenPlanet = 0;
	private boolean qKeyDown = false;
	private boolean drawingStats = false;
	private boolean drawingPlanetInfo = false;
	private Planet currentPlanet;
	private boolean destroyPlanet = false;
	private boolean invadePlanet = false;
	private Armada armada;
	private double counter = 0.0;
	private Explosions explosions;
	private ArrayList<AnimatedText> animatedTexts;
	private ArrayList<AnimatedText> animatedTextsScreen;
	private Texture flagTexture;
	//private ArrayList<Vector2d> placedFlags;
	private boolean drawMiniMap = false;
	private Texture miniMapTexure;
	private boolean takingBackPlanet = false;
	private Planet planetTakingBack;
	private int planetTakingBackIndex = 0;
	private float colorSin = 0.0f;
	private boolean zDown = false;
	private boolean xDown = false;
	private boolean spaceDown = false;
	private Sound menuSwitchSound;
	private Sound armadaAttackSound;
	@SuppressWarnings("unused")
	private Sound explosionSound;
	private Sound takenPlanetSound;
	private Sound music;
	private Sound boostSound;
	private Sound underAttackSound;
	
	public Space(int maxStarsOnScreen, float spaceBounds) {
		this.spaceBounds = spaceBounds;
		ufo = new UFO();
		stars = new Vector2d[maxStarsOnScreen];
		planets = new Planet[100];
		planetsInSight = new ArrayList<Planet>();
		animatedTexts = new ArrayList<AnimatedText>();
		animatedTextsScreen = new ArrayList<AnimatedText>();
		//placedFlags = new ArrayList<Vector2d>();
		hudTexture = new Texture();
		hudTexture.loadFile("/panel.png");
		
		destoryedPlanet = new Texture();
		destoryedPlanet.loadFile("/destroyed.png");
		
		planetTexures = new Texture[5];
		
		planetTexures[0] = new Texture();
		planetTexures[0].loadFile("/moon.png");
		
		planetTexures[1] = new Texture();
		planetTexures[1].loadFile("/PlanetA.png");
		
		planetTexures[2] = new Texture();
		planetTexures[2].loadFile("/ringplanet.png");
		
		planetTexures[3] = new Texture();
		planetTexures[3].loadFile("/rocknlavaplanet.png");
		
		planetTexures[4] = new Texture();
		planetTexures[4].loadFile("/sun.png");
		
		flagTexture = new Texture();
		flagTexture.loadFile("/flag.png");
		
		font = new Font(0.2f, 0xFFFF00);
		planNameGen = new PlanetNameGen();
		
		for (int i = 0; i < planets.length; i++) {
			planets[i] = new Planet(planetTexures[random.nextInt(planetTexures.length)], random.nextDouble() * spaceBounds, random.nextDouble() * spaceBounds);
			planets[i].destroyedTexture = destoryedPlanet;
			planets[i].planetName = planNameGen.getRandomName();
		}
		
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Vector2d(random.nextDouble() * spaceBounds, random.nextDouble() * spaceBounds);
		}
		
		armada = new Armada(Globals.armadaSize);
		armada.texture = ufo.texture;
		miniMapTexure = new Texture();
		miniMapTexure.loadFile("/minimapback.png");
		
		try {
			AL.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		explosions = new Explosions(new Sound("explosion.wav"));
		menuSwitchSound = new Sound("select.wav");
		armadaAttackSound = new Sound("attack.wav");
		takenPlanetSound = new Sound("takenplanet.wav");
		music = new Sound("music.wav");
		boostSound = new Sound("boost.wav");
		underAttackSound = new Sound("underattack.wav");
	}
	
	public void update() {
		
		if (GameMain.keys[Keyboard.KEY_SPACE] && !spaceDown) {
			starSpeed = 5.0f;
			spaceDown = true;
			boostSound.play();
		}
		if (!GameMain.keys[Keyboard.KEY_SPACE]) {
			starSpeed = 2.0f;
			spaceDown = false;
		}
		
		if (GameMain.keys[Keyboard.KEY_W] || GameMain.keys[Keyboard.KEY_UP]) {
			worldPos.y += starSpeed;
		}
		if (GameMain.keys[Keyboard.KEY_S] || GameMain.keys[Keyboard.KEY_DOWN]) {
			worldPos.y -= starSpeed;
		}
		if (GameMain.keys[Keyboard.KEY_A] || GameMain.keys[Keyboard.KEY_LEFT]) {
			worldPos.x += starSpeed;
		}
		if (GameMain.keys[Keyboard.KEY_D] || GameMain.keys[Keyboard.KEY_RIGHT]) {
			worldPos.x -= starSpeed;
		}
		
		if (GameMain.keys[Keyboard.KEY_Q] && !qKeyDown) {
			menuSwitchSound.play();
			chosenPlanet++;
			qKeyDown = true;
		}
		if (!GameMain.keys[Keyboard.KEY_Q]) {
			qKeyDown = false;
		}
		
		if (GameMain.keys[Keyboard.KEY_E] && !drawingStats) {
			drawingStats = true;
		}
		if (!GameMain.keys[Keyboard.KEY_E]) {
			drawingStats = false;
		}
		
		if (GameMain.keys[Keyboard.KEY_C] && !drawingPlanetInfo) {
			drawingPlanetInfo = true;
		}
		if (!GameMain.keys[Keyboard.KEY_C]) {
			drawingPlanetInfo = false;
		}
		
		if (currentPlanet != null) {
			if (GameMain.keys[Keyboard.KEY_Z] && !destroyPlanet && !invadePlanet && !zDown) {
				zDown = true;
				if (currentPlanet.destroyed || currentPlanet.invaded && !takingBackPlanet) {
					animatedTextsScreen.add(new AnimatedText(400, 200, "Planet already yours", font, 0.005f));
				}
				else {
					double scaledX = currentPlanet.getScaledWidth();
					double scaledY = currentPlanet.getScaledHeight();
					explosions.createExplosions(10, (int)scaledX, (int)scaledY);
					destroyPlanet = true;
				}
			}
			if (!GameMain.keys[Keyboard.KEY_Z]) {
				zDown = false;
			}
			
			if (GameMain.keys[Keyboard.KEY_X] && !destroyPlanet && !xDown) {
				xDown = true;
				if (currentPlanet.destroyed || currentPlanet.invaded && !takingBackPlanet) {
					animatedTextsScreen.add(new AnimatedText(400, 200, "Planet already yours", font, 0.005f));
				}
				else {
					invadePlanet = true;
				}
			}
			if (!GameMain.keys[Keyboard.KEY_X]) {
				xDown = false;
			}
		}
		
		if (GameMain.keys[Keyboard.KEY_M]) {
			drawMiniMap = true;
		}
		if (!GameMain.keys[Keyboard.KEY_M]) {
			drawMiniMap = false;
		}
		
		if (worldPos.y > 0) {
			worldPos.y = 0;
		}
		
		if (worldPos.y < -(spaceBounds - GameMain.height)) {
			worldPos.y = -(spaceBounds - GameMain.height);
		}
		
		if (worldPos.x > 0) {
			worldPos.x = 0;
		}
		
		if (worldPos.x < -(spaceBounds - GameMain.width)) {
			worldPos.x = -(spaceBounds - GameMain.width);
		}
		
		ufo.update();

		planetsInSight.clear();
		Globals.planetsLeft = 0;
		for (int i = 0; i < planets.length; i++) {
			double x = planets[i].position.x + worldPos.x;
			double y = planets[i].position.y + worldPos.y;
			double width = planets[i].texture.width;
			double height = planets[i].texture.height;
			double xTest = ufo.position.x;
			double yTest = ufo.position.y;
			double distanceFromPlanet = Math.sqrt(Math.pow(x - xTest, 2) + Math.pow(y - yTest, 2));
			
			planets[i].distanceFromUfo = Math.round(distanceFromPlanet);
			
			if (x > 0 - width && x < GameMain.width + width && y > 0 - height && y < GameMain.height + height) {
				planetsInSight.add(planets[i]);
			}
			
			if (!planets[i].destroyed && !planets[i].invaded) {
				Globals.planetsLeft++;
			}
		}
		
		if (invadePlanet) {
			double widthScaled = currentPlanet.getScaledWidth() / 2;
			double heightScaled = currentPlanet.getScaledHeight() / 2;
			
			int i = 0;
			for (Vector2d vec : armada.ufos) {
				// Rotate the armada in some weird fashion
				double sinIn = Math.sin(counter / 40) * 50;
				double sin = (Math.sin((counter + i * 20) / 50) * (widthScaled + sinIn));
				double cos = (Math.cos((counter + i * 20) / 50) * (heightScaled + sinIn));

				vec.x = currentPlanet.position.x + widthScaled + worldPos.x + sin;
				vec.y = currentPlanet.position.y + heightScaled + worldPos.y + cos;
				i++;
			}
			

			if (random.nextInt(120 - (int)currentPlanet.chancesToWin - Globals.armadaSize) == 0) {
				currentPlanet.percentInvaded++;
				animatedTexts.add(new AnimatedText((float)currentPlanet.position.x + (float)currentPlanet.getScaledWidth() / 2, (float)currentPlanet.position.y + (float)currentPlanet.getScaledHeight() / 2, "" + currentPlanet.percentInvaded, font, 0.02f));
			
				if (currentPlanet.percentInvaded >= 100) {
					//placedFlags.add(new Vector2d(currentPlanet.position.x + widthScaled, currentPlanet.position.y + heightScaled));
					invadePlanet = false;
					//if (!takingBackPlanet) {
						Globals.planetsInvaded++;
						armada.armadaCount++;
						Globals.armadaSize = armada.armadaCount;
						armada.ufos.add(new Vector2d(0, 0));
					//}
					currentPlanet.invaded = true;
					
					if (!takingBackPlanet) {
						animatedTextsScreen.add(new AnimatedText(400, 200, "Invaded Planet", font, 0.005f));
						takenPlanetSound.play();
					}
				}
			}
		}
		
		if (destroyPlanet) {
			int i = 0;
			for (Vector2d vec : armada.ufos) {
				double widthScaled = currentPlanet.getScaledWidth() / 2;
				double heightScaled = currentPlanet.getScaledHeight() / 2;
				
				// Rotate the armada around the planet
				double sin = (Math.sin((counter + i * 20) / 50) * (widthScaled));
				double cos = (Math.cos((counter + i * 20) / 50) * (heightScaled));

				vec.x = currentPlanet.position.x + widthScaled + worldPos.x + sin;
				vec.y = currentPlanet.position.y + heightScaled + worldPos.y + cos;
				i++;
			}
			
			explosions.update();
			
			if (random.nextInt(100 - (int)currentPlanet.chancesToWin) == 0) {
				int damage = random.nextInt(Globals.armadaSize + Globals.armadaPower);
				currentPlanet.health -= damage;
				
				if (damage != 0) {
					animatedTexts.add(new AnimatedText((float)currentPlanet.position.x + (float)currentPlanet.getScaledWidth() / 2, (float)currentPlanet.position.y + (float)currentPlanet.getScaledHeight() / 2, "" + damage, font, 0.02f));
				}
				
				if (currentPlanet.health < 0) {
					currentPlanet.health = 0;
					destroyPlanet = false;
					currentPlanet.destroyed = true;
					Globals.planetsCouqured++;
					Globals.armadaPower += 1;
					animatedTextsScreen.add(new AnimatedText(400, 200, "Destroyed Planet", font, 0.005f));
					takenPlanetSound.play();
				}
			}
		}
		
		ArrayList<Integer> removeAts = new ArrayList<Integer>();
		int removeAtCounter = 0;
		for (AnimatedText at : animatedTexts) {
			at.update();
			
			if (at.drawing) {
				removeAts.add(removeAtCounter);
			}
			
			removeAtCounter++;
		}
		
		for (Integer i : removeAts) {
			animatedTexts.remove(i);
		}
		
		for (AnimatedText at : animatedTextsScreen) {
			at.update();
			
			if (at.drawing) {
				removeAts.add(removeAtCounter);
			}
			
			removeAtCounter++;
		}
		
		for (Integer i : removeAts) {
			animatedTextsScreen.remove(i);
		}
		
		if (!takingBackPlanet) {
			if (random.nextInt(100) == 99) {
				int randIndex = random.nextInt(planets.length);
				if (planets[randIndex].invaded) {
					planetTakingBack = planets[randIndex];
					planetTakingBackIndex = randIndex;
					animatedTextsScreen.add(new AnimatedText(400, 200, "One of your planets is being taken back", font, 0.005f));
					takingBackPlanet = true;
					planetTakingBack.percentInvaded--;
					underAttackSound.play();
				}
			}
		}
		
		if (takingBackPlanet) {
			planetTakingBack.destroyed = false;
			
			if (planetTakingBack.percentInvaded >= 100) {
				takingBackPlanet = false;
				animatedTextsScreen.add(new AnimatedText(400, 200, "Taken back planet", font, 0.005f));
				takenPlanetSound.play();
			}
			
			if (random.nextInt(500) == 232) {
				planetTakingBack.percentInvaded--;
				animatedTexts.add(new AnimatedText((float)planetTakingBack.position.x + (float)planetTakingBack.getScaledWidth() / 2, (float)planetTakingBack.position.y + (float)planetTakingBack.getScaledHeight() / 2, "" + planetTakingBack.percentInvaded, font, 0.02f));
			}
			
			if (planetTakingBack.percentInvaded <= 0) {
				planetTakingBack.percentInvaded = 0;
				planetTakingBack.invaded = false;
				takingBackPlanet = false;
				
				Globals.planetsInvaded--;
				armada.armadaCount--;
				Globals.armadaSize = armada.armadaCount;
				if (armada.ufos.size() > 0) {
					armada.ufos.remove(0);
				}
				
				animatedTextsScreen.add(new AnimatedText(400, 200, "You lost a planet", font, 0.005f));
				underAttackSound.play();
			}
		}
		
		colorSin = (float)Math.abs(Math.sin(counter / 7) / 2);
		counter++;
		
		if (destroyPlanet || invadePlanet) {
			if (!armadaAttackSound.playing()) {
				armadaAttackSound.play();
			}
		}
		
		if (!music.playing()) {
			music.play();
		}
		
		Globals.score = (Globals.armadaPower * 563) + (Globals.armadaSize * 88) + (Globals.planetsCouqured * 354) + (Globals.planetsInvaded * 135);
	}
	
	public void draw() {
		glPointSize(2.0f);
		glColor3f(1.0f, 1.0f, 1.0f);
		
		glBegin(GL_POINTS);
		{
			for (int i = 0; i < stars.length; i++) {
				double x = stars[i].x + worldPos.x;
				double y = stars[i].y + worldPos.y;
				
				if (x > 0 && x < GameMain.width && y > 0 && y < GameMain.height) {
					glVertex2d(x, y);
				}
			}
		}
		glEnd();
		
		glPointSize(7.0f);
		
		for (int i = 0; i < planets.length; i++) {
			double x = planets[i].position.x + worldPos.x;
			double y = planets[i].position.y + worldPos.y;
			double width = planets[i].getScaledWidth();
			double height = planets[i].getScaledHeight();
			
			if (x > 0 - width && x < GameMain.width + width && y > 0 - height && y < GameMain.height + height) {

				planets[i].destroyedTexture.width = planets[i].texture.width;
				planets[i].destroyedTexture.height = planets[i].texture.height;
				planets[i].draw((float)x, (float)y);
				
				glLineWidth(3.0f);
				
				glBegin(GL_LINES);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x, y);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x + width, y);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x + width, y);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x + width, y + height);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x + width, y + height);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x, y + height);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x, y + height);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x, y);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x + width / 2, y);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x + width / 2, y + height);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x, y + height / 2);
				
				glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				glVertex2d(x + width, y + height / 2);
				
				glEnd();

				if (planets[i].invaded) {
					flagTexture.draw((float)(x + width / 2) - 10, (float)(y + height / 2) - 20, 0.17f);
				}
				
				font.drawText(x, y, planets[i].planetName, 0.4f);
			}
		}
		
		//for (Vector2d positions : placedFlags) {
		//	flagTexture.draw((float)(positions.x + worldPos.x) - 5, (float)(positions.y + worldPos.y) - 20, 0.2f);
		//}
		
		if (destroyPlanet) {
			explosions.draw(currentPlanet.position.x + worldPos.x, currentPlanet.position.y + worldPos.y);
		}
		
		if (destroyPlanet || invadePlanet) {
			armada.draw();
		}
		
		for (AnimatedText at : animatedTexts) {
			at.draw((float)worldPos.x, (float)worldPos.y);
		}
		
		for (AnimatedText at : animatedTextsScreen) {
			at.draw(0, 0);
		}
		
		ufo.draw();
		
		float hudX = 5.0f;
		float hudY = GameMain.height - (hudTexture.height * hudTexture.scale);
		font.drawText(hudX, hudY - 10, "Z to destroy X to invade C for info", 0.3f);
		hudTexture.draw(hudX, hudY, 0.5f);
		
		hudX += 10;
		hudY += 10;
		font.drawText(hudX, hudY, "nearby planets", 0.5f);
	
		float namePos = hudY + 20;
		int counter = 0;
		for (Planet p : planetsInSight) {
			font.drawText(hudX, namePos, p.planetName, 0.4f);
			
			if (counter == (chosenPlanet % planetsInSight.size()) && planetsInSight.size() != 0 && !destroyPlanet && !invadePlanet) {
				float nameH = (32 * 0.4f);
				float nameLen = p.planetName.length() * (32 * 0.4f);
				currentPlanet = p;
				
				float overlapping = 2;
				glLineWidth(1.5f);
				glBegin(GL_LINES);
				{
					glColor3f(1.0f, 0.0f, 0.0f);
					glVertex2d(hudX - overlapping, namePos - overlapping);
					glColor3f(1.0f, 0.0f, 0.0f);
					glVertex2d(hudX + nameLen + overlapping, namePos - overlapping);
					glColor3f(1.0f, 0.0f, 0.0f);
					glVertex2d(hudX + nameLen + overlapping, namePos - overlapping);
					glColor3f(1.0f, 0.0f, 0.0f);
					glVertex2d(hudX + nameLen + overlapping, namePos + nameH + overlapping);
					glColor3f(1.0f, 0.0f, 0.0f);
					glVertex2d(hudX + nameLen + overlapping, namePos + nameH + overlapping);
					glColor3f(1.0f, 0.0f, 0.0f);
					glVertex2d(hudX - overlapping, namePos + nameH + overlapping);
					glColor3f(1.0f, 0.0f, 0.0f);
					glVertex2d(hudX - overlapping, namePos + nameH + overlapping);
					glColor3f(1.0f, 0.0f, 0.0f);
					glVertex2d(hudX - overlapping, namePos - overlapping);
				}
				glEnd();
			}
			
			namePos += 20;
			counter++;
		}
		
		if (drawingStats) {
			hudX = GameMain.width / 2 - (hudTexture.width * hudTexture.scale);
			hudY = GameMain.height / 2 - (hudTexture.height * hudTexture.scale);
			double center = hudX + hudTexture.width / 2 - ((32 * 0.5f) * (5.0 / 2.0));
			hudTexture.draw(hudX, hudY, 1.0f);
			
			font.drawText(center, hudY, "stats", 0.5f);
			font.drawText(hudX + 20, hudY + 20, "Armada Size        " + Globals.armadaSize, 0.4f);
			font.drawText(hudX + 20, hudY + 40, "Planets Destroyed  " + Globals.planetsCouqured, 0.4f);
			font.drawText(hudX + 20, hudY + 60, "Planets Invaded    " + Globals.planetsInvaded, 0.4f);
			font.drawText(hudX + 20, hudY + 80, "Planets Untouched  " + Globals.planetsLeft, 0.4f);
			font.drawText(hudX + 20, hudY + 100,"Armada Power       " + Globals.armadaPower, 0.4f);
			font.drawText(hudX + 20, hudY + 120,"Total Score        " + Globals.score, 0.4f);
		}
		
		if (drawingPlanetInfo) {
			if (currentPlanet != null) {
				hudX = GameMain.width / 2 - (hudTexture.width * hudTexture.scale);
				hudY = GameMain.height / 2 - (hudTexture.height * hudTexture.scale);
				double center = hudX + hudTexture.width / 2 - ((32 * 0.5f) * ((currentPlanet.planetName.length() + 7) / 2.0));
				hudTexture.draw(hudX, hudY, 1.0f);
				
				font.drawText(center, hudY, "Planet " + currentPlanet.planetName, 0.5f);
				font.drawText(hudX + 20, hudY + 20, "Chances to score   " + (int)currentPlanet.chancesToWin, 0.4f);
				font.drawText(hudX + 20, hudY + 40, "Distance           " + (int)currentPlanet.distanceFromUfo, 0.4f);
				font.drawText(hudX + 20, hudY + 60, "Health             " + (int)currentPlanet.health, 0.4f);
				font.drawText(hudX + 20, hudY + 80, "Percent Invaded    " + (int)currentPlanet.percentInvaded, 0.4f);
				font.drawText(hudX + 20, hudY + 100, currentPlanet.destroyed ? "Destroyed" : "Not Desroyed", 0.4f);
				font.drawText(hudX + 20, hudY + 120, currentPlanet.invaded ? "Invaded" : "Not Invaded", 0.4f);
			}
		}
		
		if (drawMiniMap) {
			miniMapTexure.draw(GameMain.width - miniMapTexure.width, GameMain.height / 2, 1.0f);
			
			float x = GameMain.width - miniMapTexure.width + 15;
			float y = GameMain.height / 2 + 10;
			
			glPointSize(3.0f);
			glBegin(GL_POINTS);
			{
				glColor3f(1.0f, 1.0f, 1.0f);
				for (int i = 0; i < planets.length; i++) {
					glVertex2d(planets[i].position.x / 46 + x, planets[i].position.y / 46 + y);
				}
				
				glColor3f(1.0f, 0.0f, 0.0f);
				glVertex2d(-(worldPos.x - ufo.position.x) / 46 + x, -(worldPos.y - ufo.position.y) / 46 + y);
			}
			glEnd();
			
			for (int i = 0; i < planets.length; i++) {
				float xFlag = (float)(planets[i].position.x) / 46 + x;
				float yFlag = (float)(planets[i].position.y) / 46 + y;
				
				if (planetTakingBackIndex == i && takingBackPlanet) {
					glPointSize(5.0f);
					glBegin(GL_POINTS);
					{
						glColor3f(1.0f, colorSin, colorSin);
						glVertex2d(xFlag, yFlag);
					}
					glEnd();
				}
				
				if (planets[i].invaded || planets[i].destroyed) {
					flagTexture.draw(xFlag - 3, yFlag - 7, 0.05f);
				}
			}
		}
	}
}
