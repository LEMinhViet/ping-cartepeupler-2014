package upmc.ping.Object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class ObjectList extends ArrayList<Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Random random;
	private BufferedImage image;
	
	public ObjectList(String object) {
		super();
		random = new Random();
		try {
			image = ImageIO.read(new File("res/img/" + object + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean add(Object object, boolean isAddable, boolean isRemovable) {
		Object obj = boundObject_Point(object.getX(), object.getY());
		if (obj == null) {
			if (!isAddable)		return false;
			return super.add(object);
		} else {
			if (isRemovable)	remove(obj);
			return false;
		}		
	}
	
	public void addDensity(int x, int y, int density, int size) {
		int numberAdd = 0;
		int startX;
		int startY;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				startX = x + (i - 1) * size / 2;
				startY = y + (j - 1) * size / 2;
				numberAdd = 1 - boundRect_Object(startX, startY, size / 2);
				for (int add = 0; add < numberAdd; add++) {
					if (random.nextDouble() < (double)density / 200) {
						if (!add(new Object(startX + random.nextInt(size / 2), 
											startY + random.nextInt(size / 2), getSize()), 
								true, false)) {
							add--;
						}
					}
				}
			}
		}
	}
	
	public void paint(Graphics2D gTree) {
		for (Object object : this) {
			object.paint(gTree, image);
		}
	}
	
	public Object boundObject_Point(int posX, int posY) {
		for (Object object : this) {
			if (object.bound(posX, posY, image.getWidth(), image.getHeight())) {
				return object;
			}
		}
		return null;
	}
	
	public int boundRect_Object(int posX, int posY, int size) {
		int count = 0;
		Rectangle rect = new Rectangle(posX - size / 2, posY - size / 2, size, size);
		for (Object object : this) {
			if (rect.contains(object.getCenterX(), object.getCenterY())) {
				count++;
			}
		}
		return count;
	}
	
	public int getSize() {
		return image.getWidth();
	}
}
