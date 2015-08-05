package com.speedme.entity;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

import com.speedme.interfaces.IterativeEntityInterface;

/**
 * Implementação da classe abstrata de uma MOveableEntity- conceito de padronização de iteração com uma entity manipulável.
 * Toda entity criada deverá extender AbstractMoveableEntity, onde a mesma contém os métodos que devem ser implementados e as variáveis de controle
 * necessárias para controlar o objeto no display. 
 * 
 *  
 */

public abstract class AbstractIterativeEntity implements IterativeEntityInterface {
	
	protected double x,y,width,height,rotation;
	protected Rectangle hitbox = new Rectangle();
	
	protected double dx,dy,dr;
	
	public AbstractIterativeEntity(double x, double y, double width, double height) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;

		this.dx=0;
		this.dy=0;
	}
	
	@Override
	public void draw() {
		
	}
	
	public void update(int delta) {
		this.x += delta * dx * Math.cos(Math.toRadians(rotation));
		this.y += delta * dy * Math.sin(Math.toRadians(rotation));
		this.rotation += delta*dr;
		
		if (x < 0) x = 0;
		if (x > Display.getWidth()) x = Display.getWidth();
		if (y < 0) y = 0;
		if (y > Display.getHeight()) y = Display.getHeight();
	}
	
	public double getDX() {
		return dx;
	}
	public double getDY() {
		return dy;
	}
	public void setDX(double dx) {
		this.dx=dx;
	}
	public void setDY(double dy) {
		this.dy=dy;
	}
	public double getDR() {
		return dr;
	}
	public void setDR(double dr) {
		this.dr=dr;
	}

	
	@Override
	public void setLocation(double x, double y) {
		this.x=x;
		this.y=y;
	}

	@Override
	public void setX(double x) {
		this.x=x;
	}

	@Override
	public void setY(double y) {
		this.y=y;
	}

	@Override
	public void setWidth(double width) {
		this.width=width;
	}

	@Override
	public void setHeight(double height) {
		this.height=height;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public double getWidth() {
		return width;
	}
	
	
	
	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	@Override
	public boolean intersects(final IterativeEntityInterface other) {


		//Usa AffineTransform para detectar colisão - que mão
		
		//car
		java.awt.Rectangle rec = new java.awt.Rectangle();
		rec.setBounds((int)this.getX(), (int)(this.getY()),(int)this.getWidth(), (int)this.getHeight());
		AffineTransform transform = new AffineTransform();
		transform.translate(this.getX(), this.getY()-(this.getHeight()/2));
		transform.rotate(Math.toRadians(this.getRotation()));
		transform.translate(-this.getX(), -this.getY()-(this.getHeight()/2));
		Shape rec2D = (Shape) transform.createTransformedShape(rec);
		
		
		//object
		java.awt.Rectangle recOther = new java.awt.Rectangle((int)other.getX(), (int)(other.getY()),(int)other.getWidth(), (int)other.getHeight());

		//Collision test
		if (rec2D.intersects(recOther)) 
			return true;
		else
			return false;
		
  	}		

}
