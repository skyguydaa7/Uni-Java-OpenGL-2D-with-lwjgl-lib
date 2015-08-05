package com.speedme.interfaces;

/**
 * Interface de definição de uma entity que pode ser movida
 *  
 */

public interface IterativeEntityInterface {
		public void draw();
		public void update(int delta);
		public void setLocation(double x, double y);
		public void setX(double x);
		public void setY(double y);
		public void setWidth(double width);
		public void setHeight(double height);
		public double getX();
		public double getY();
		public double getHeight();
		public double getWidth();
		public boolean intersects(IterativeEntityInterface other);
		public double getDX();
		public double getDY();
		public void setDX(double dx);
		public void setDY(double dy);
}
