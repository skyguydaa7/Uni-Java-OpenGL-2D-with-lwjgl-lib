package com.speedme.entity;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 * Entidade para desenhar o carro
 */


public class VehicleEntity extends AbstractIterativeEntity {

	private double dp;
	private Texture texture;
	private boolean textureChanged=true;
	private int vehicleType;

	public VehicleEntity(double x, double y, double width, double height, int vehicleType) {
		super(x, y, width, height);

		//Seta textura do veículo
		
		setVehicleType(vehicleType);
		setTexture("vehicle");
	}

	/**
	 * Desenha componente - utiliza variáveis x, y , width e height de controle
	 *  
	 */
	
	@Override
	public void draw() {		
		dx=dp;
		dy=dp;

		GL11.glPushMatrix();


		GL11.glTranslated(x, y-height/2, 0);
		GL11.glRotated(rotation, 0f, 0f, 1f);
		GL11.glTranslated(-x, -y-height/2, 0);

		
		GL11.glColor3f(1, 1, 1);
		
		if (texture!=null) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		}
		
        glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0.0f, (float)(y+height));
        GL11.glVertex3f(-1.0f, (float)(y+height),  1.0f);   // Bottom Left Of The Texture and Quad
        GL11.glTexCoord2f((float)(x+width), (float)(y+height));
        GL11.glVertex3f( (float)(x+width), (float)(y+height),  1.0f);   // Bottom Right Of The Texture and Quad
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
		glEnd();

        /*
        glBegin(GL11.GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex2d(x, y+height); // superior  esquerdo
		glTexCoord2d(1, 0);
		glVertex2d(x+width, y+height); // SUperior direito
		glTexCoord2d(1, 1);
		glVertex2d(x+width, y); // inferior direito
		glTexCoord2d(0, 1);
		glVertex2d(x, y); // inferior esquerdo
		glEnd();
		*/
        
		GL11.glPopMatrix();
		
	}

	
	/**
	 * Seta textura do entity - usa biblioteca do lwjgl para carregar textura
	 *  
	 */

	public void setTexture(String file) {
		if (texture==null) {
			textureChanged=true;
			try {
				texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/vehicle" + String.valueOf(getVehicleType()) + "/" + file + ".png")));
			} catch (final FileNotFoundException e) {
				System.out.println(file + " texture not found");
			} catch (final IOException e) {
				System.out.println("IOException error");
				Display.destroy();
				System.exit(1);
			}
		} else {
			if (!texture.toString().equals(file)) {
				textureChanged=true;
				try {
					texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/vehicle" + String.valueOf(getVehicleType()) + "/" + file + ".png")));
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

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double r) {
		rotation=r;
	}

	public void setDP(double dp) {
		this.dp=dp;
	}

	public double getDP() {
		return dp;
	}

	public int getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(int vehicleType) {
		this.vehicleType = vehicleType;
	}
	
}
