package com.speedme.game;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import com.speedme.entity.VehicleEntity;
import com.speedme.entity.WorldEntity;
import com.speedme.util.GameDef;

public class SpeedMeGame {

	private int fps;
	
	private VehicleEntity vehicle;
	private List<WorldEntity> worldObjects;
	
	private int vehicleType = 1;
	private int worldType = 1;
	
	private long lastFrame;
	
	private long saveFPS;
	
	public SpeedMeGame() {
		saveFPS = getTime();

		initDisplay();
		initOpenGL();
		initEntities();

		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			vehicleIteration();
			updateFPS();

			update(getDelta());
			
			if (detectColision()) {
				//EXPLODE TUDO
				vehicle.setTexture("vehicleExplode");
				render();
				Display.update();
				Display.sync(60);
				
				initOpenGL();
				initEntities();
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else
				vehicle.setTexture("vehicle");

			
			render();

			Display.update();
			Display.sync(60);
		}

		
		
		
		Display.destroy();
		System.exit(0);
	}

	private void vehicleIteration() {
		
		//DR - Direção
		//DP - Velocidade
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			if (vehicle.getDP() < 0) {
				// Parando
				vehicle.setTexture("vehicleStopping");
				vehicle.setDR(GameDef.turnLeft);
				vehicle.setDP(vehicle.getDP() + GameDef.breakValue);
			} else {
				vehicle.setTexture("vehicle");
				vehicle.setDR(GameDef.turnLeft);
				vehicle.setDP(vehicle.getDP()+GameDef.accelerationValue);
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			if (vehicle.getDP() < 0) {
				// Parando
				vehicle.setTexture("vehicleStopping");
				vehicle.setDR(GameDef.turnLeft);
				vehicle.setDP(vehicle.getDP() + GameDef.breakValue);
			} else {
				vehicle.setTexture("vehicle");
				vehicle.setDR(GameDef.turnRight);
				vehicle.setDP(vehicle.getDP()+GameDef.accelerationValue);
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			if (vehicle.getDP() > 0) {
				// Parando
				vehicle.setTexture("vehicleStopping");
				vehicle.setDR(GameDef.turnLeft);
				vehicle.setDP(vehicle.getDP() - GameDef.breakValue);
			} else {
				// Ré
				vehicle.setTexture("vehicleReverse");
				vehicle.setDR(GameDef.turnLeft);
				vehicle.setDP(vehicle.getDP()-GameDef.accelerationValue);
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			if (vehicle.getDP() > 0) {
				// Parando
				vehicle.setTexture("vehicleStopping");
				vehicle.setDR(GameDef.turnRight);
				vehicle.setDP(vehicle.getDP() - GameDef.breakValue);
			} else {
				// Ré
				vehicle.setTexture("vehicleReverse");
				vehicle.setDR(GameDef.turnRight);
				vehicle.setDP(vehicle.getDX()-GameDef.accelerationValue);
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			vehicle.setDR(0);
			if (vehicle.getDP() < 0) {
				// Parando
				vehicle.setTexture("vehicleStopping");
				vehicle.setDP(vehicle.getDP() + GameDef.breakValue);
			} else {
				// Acelerando
				vehicle.setTexture("vehicle");
				vehicle.setDP(vehicle.getDP()+GameDef.accelerationValue);
			}
		}else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			vehicle.setDR(0);
			if (vehicle.getDP() > 0) {
				// Parando
				vehicle.setTexture("vehicleStopping");
				vehicle.setDP(vehicle.getDP() - GameDef.breakValue);
			} else {
				// Ré
				vehicle.setTexture("vehicleReverse");
				vehicle.setDP(vehicle.getDP()-GameDef.accelerationValue);
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			if (vehicle.getDP() > 0) {
				vehicle.setDR(GameDef.turnRight);
				brake(GameDef.engineBreakPower);
			}

			if (vehicle.getDP() < 0) {
				vehicle.setDR(GameDef.turnRight);
				brake(GameDef.engineBreakPower);
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			if (vehicle.getDP() > 0) {
				vehicle.setDR(GameDef.turnLeft);
				brake(GameDef.engineBreakPower);
			}

			if (vehicle.getDP() < 0) {
				vehicle.setDR(GameDef.turnLeft);
				brake(GameDef.engineBreakPower);
			}
		}  else {			
			vehicle.setDR(0);
			brake(GameDef.engineBreakPower);
		}
		
	}

	/**
	 * Freia veículo
	 * 
	 * Se estiver indo p/ frente 
	 * 
	 */
	private void brake(double breakPower) {		
		if (vehicle.getDP() > breakPower) {
			vehicle.setTexture("vehicle");
			vehicle.setDP(vehicle.getDP()-breakPower);
		} else if(vehicle.getDP() < -breakPower) {
			vehicle.setTexture("vehicleReverse");
			vehicle.setDP(vehicle.getDP()+breakPower);				
		} else if((vehicle.getDP() < breakPower && vehicle.getDP() > 0) || (vehicle.getDP() > -breakPower && vehicle.getDP() < 0)) {
			vehicle.setDP(0);
			vehicle.setTexture("vehicle");
		}
	}

	private void update(int delta) {
		vehicle.update(delta);

	}

	private void render() {
		//Desenha objetos do mundo
		for (WorldEntity worldE : worldObjects) {
			worldE.draw();
		}

		//Desenha veículo
		vehicle.draw();
		
	}

	//Criar entities - só desenha no draw
	private void initEntities() {
		initWorld();
		vehicle = new VehicleEntity(500,350,80,47, vehicleType);
	}

	//COnfigura o mundo
	private void initWorld() {

		worldObjects = new ArrayList<WorldEntity>();
		worldObjects.add(new WorldEntity(100,0,80,200, GameDef.TEXTURE_WOOD));
		worldObjects.add(new WorldEntity(300,0,80,200, GameDef.TEXTURE_WOOD));
		
	}
	

	private void initOpenGL() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
	}

	private void initDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.setInitialBackground(0.5f, 0.5f, 0.5f);
			Display.create();
		} catch (final LWJGLException e) {
			System.out.println("Couldn't load display");
			Display.destroy();
			System.exit(1);
		}
	}

	private void updateFPS() {
		if (getTime() - saveFPS > 1000) {
			Display.setTitle(String.format( "SpeedMe               Game FPS: " + fps + "             Velocidade: %.0f Km/h", vehicle.getDP()*130));
			fps = 0; //reset the FPS counter
			saveFPS += 1000; //add one second
		}
		fps++;
	}

	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private int getDelta() {
		int delta;
		final long time = getTime();
		delta = (int) (time - lastFrame);
		lastFrame = getTime();

		return delta;
	}
	

	private boolean detectColision() {
		
		//Se detectar colisão - retorna true
		for (WorldEntity worldE : worldObjects) {
			if (vehicle.intersects(worldE))
				return true;
			
		}
		
		return false;
		
	}
	
}
