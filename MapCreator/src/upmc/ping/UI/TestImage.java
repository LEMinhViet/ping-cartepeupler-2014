package upmc.ping.UI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import upmc.ping.Utils.ImageUtils;

class TestImage {
    public static void main(String args[]) {
		try {
			BufferedImage img = ImageIO.read(new File("res/mask/topo1mask.png"));
			BufferedImage mask = ImageIO.read(new File("res/flatSO_resized.png"));
	    	applyGrayscaleMaskToAlpha(img, mask);    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }
    
    public static void applyGrayscaleMaskToAlpha(BufferedImage image, BufferedImage mask)
    {
        int width = image.getWidth();
        int height = image.getHeight();

        int[] imagePixels = image.getRGB(0, 0, width, height, null, 0, width);
        int[] maskPixels = mask.getRGB(0, 0, width, height, null, 0, width);

        for (int i = 0; i < imagePixels.length; i++)
        {
            int color = imagePixels[i] & 0x00ffffff; // Mask preexisting alpha
            int alpha = maskPixels[i] & 0xff000000; // Shift green to alpha
            imagePixels[i] = (color | alpha) & imagePixels[i];
        }

        image.setRGB(0, 0, width, height, imagePixels, 0, width);
        try {
			ImageIO.write(image, "png", new File("res/hehe.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
