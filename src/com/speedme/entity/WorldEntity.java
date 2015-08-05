package com.speedme.entity;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


/**
 * Entidade para desenhar o mapa.
 */

public class WorldEntity extends AbstractIterativeEntity {

	
	
	private Texture texture;
	
	public WorldEntity(double x, double y, double width, double height, String textureType) {
		super(x, y, width, height);

		
		setTexture(textureType);
		
		
	}

	
	@Override
	public void draw() {

		//Desenha o mapa 
		GL11.glPushMatrix();

		GL11.glColor3f(1, 1, 1);
		
		if (texture!=null) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			//glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		}
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(0,0);
		GL11.glVertex2d(x,y+height); //esquerda superior
		//System.out.println("Desenhando mundo(0,0): x= " + x  + " y = " + y);
		GL11.glTexCoord2d(1,0);
		GL11.glVertex2d(x+width, y+height); //Direita superior
		//System.out.println("Desenhando mundo(1,0): x= " + x+width  + " y = " + y);
		GL11.glTexCoord2d(1,1); 
		GL11.glVertex2d(x+width, y); //DIreita inferior
		//System.out.println("Desenhando mundo(1,1): x= " + x+width  + " y = " + y+height);
		GL11.glTexCoord2d(0,1);
		GL11.glVertex2d(x,y); //esquerda inferior
		//System.out.println("Desenhando mundo(0,1): x= " + x  + " y = " + y+height);
		GL11.glEnd();
	
		
		
		GL11.glPopMatrix();;

		
		
	}
	
	
	/**
	 * Seta textura do entity - usa biblioteca do lwjgl para carregar textura
	 *  
	 */

	public void setTexture(String file) {
		if (texture==null) {
			try {
				texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/world/" + file + ".png")));
			} catch (final FileNotFoundException e) {
				System.out.println(file + " texture not found");
			} catch (final IOException e) {
				System.out.println("IOException error");
				Display.destroy();
				System.exit(1);
			}
		} else {
			if (!texture.toString().equals(file)) {
				try {
					texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/world/" + file + ".png")));
				} catch (final FileNotFoundException e) {
					System.out.println(file + " texture not found");
				} catch (final IOException e) {
					System.out.println("IOException error");
					Display.destroy();
					System.exit(1);
				}
			}
		}
	}

	
	
	
	
	
	

}
