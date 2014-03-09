package upmc.ping.Object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Object {
	private int x; 
	private int y;
	private int size;
	
	public Object (int x, int y, int size) {
		this.x = x;
		this.y = y;		
		this.size = size;
	}
	
	public void paint(Graphics2D gTree, BufferedImage image) {
		AffineTransform at = new AffineTransform();

        at.translate(x, y);
//        at.rotate(Math.toRadians(270));        
        at.translate(- image.getWidth() / 2, - image.getHeight() / 2);
		gTree.drawImage(image, at, null);
		
	}
	
	public boolean bound(int posX, int posY, int width, int height) {
		return new Rectangle(x - width / 2,  y - height / 2, width, height).contains(posX, posY);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getCenterX() {
		return x - size / 2;
	}
	
	public int getCenterY() {
		return y - size / 2;
	}
}
