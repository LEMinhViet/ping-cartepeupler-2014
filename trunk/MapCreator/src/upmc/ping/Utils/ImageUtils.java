package upmc.ping.Utils;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

public class ImageUtils {
	/**
	 * Transformer le fichier raw en png 129x129 pixels
	 * @param src : fichier raw
	 * @param out : fichier png
	 * @throws IOException
	 */
	public static void rawToPng(String src, String out) throws IOException {
		FileInputStream fis = new FileInputStream(src);
		DataInputStream dis = new DataInputStream(fis);
		// BufferedInputStream bis = new BufferedInputStream(RawToPng.class.getResourceAsStream(src));
		// DataInputStream dis = new DataInputStream(bis);
		int[][] data = new int[129][129];
		int max = 0;
		for (int y = 0; y < 129; y++)
			for (int x = 0; x < 129; x++) {
				data[x][y] = dis.readUnsignedShort();
				max = Math.max(max, data[x][y]);
			}
		
		BufferedImage image = new BufferedImage(129, 129, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < 129; y++)
			for (int x = 0; x < 129; x++) {
				int c = data[x][y] * 255 / max;
				image.setRGB(x, y, c + (c << 8) + (c << 16));
			}
		
		ImageIO.write(image, "png", new File(out));
		dis.close();
	}
	
	/**
	 * Changer la taille de l'image 
	 * @param source : l'image d'origin
	 * @param destWidth : la nouvelle taille 
	 * @param destHeight : la nouvelle taille 
	 * @param interpolation : type de l'agrandir
	 * @return
	 */
	public static BufferedImage resize(BufferedImage source, int destWidth, int destHeight, Object interpolation) {
        if (source == null)
            throw new NullPointerException("source image is NULL!");
        if (destWidth <= 0 && destHeight <= 0)
            throw new IllegalArgumentException("destination width & height are both <=0!");
        
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        
        double xScale = ((double) destWidth) / (double) sourceWidth;
        double yScale = ((double) destHeight) / (double) sourceHeight;
        
        if (destWidth <= 0) {
            xScale = yScale;
            destWidth = (int) Math.rint(xScale * sourceWidth);
        }
        if (destHeight <= 0) {
            yScale = xScale;
            destHeight = (int) Math.rint(yScale * sourceHeight);
        }
        
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(destWidth, destHeight, source.getColorModel().getTransparency());
        Graphics2D g2d = null;
        
        try {
            g2d = result.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
            AffineTransform at = AffineTransform.getScaleInstance(xScale, yScale);
            g2d.drawRenderedImage(source, at);
        } finally {
            if (g2d != null)
                g2d.dispose();
        }
        
        return result;
    }

	/**
	 * Une fonction pour supporter la fonction "resize" au dessus
	 * @return
	 */
    public static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }

    /**
     * Garder le fichier JPEG dans l'ordinateur
     * @param input : location 
     * @param name : nom
     * @throws IOException
     */
    public static void writeJPEG(BufferedImage input, String name) throws IOException {
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("JPG");
        if (iter.hasNext()) {
            ImageWriter writer = (ImageWriter) iter.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(0.95f);
            File outFile = new File(name);
            FileImageOutputStream output = new FileImageOutputStream(outFile);
            writer.setOutput(output);
            IIOImage image = new IIOImage(input, null, null);
            writer.write(null, image, iwp);
            output.close();
        }
    }
    
    /**
     * Appliquer le canal "Alpha" d'un image sur l'autre 
     * @param image : l'image origin
     * @param mask : l'image contient le canal Alpha 
     * @param x : la position qu'on commence à appliquer
     * @param y : la position qu'on commence à appliquer
     * @param width : la taille du zone qu'on va appliquer
     * @param height: la taille du zone qu'on va appliquer
     * @return : l'image recu
     * @throws IOException
     */
    public static BufferedImage applyGrayscaleMaskToAlpha(BufferedImage image, BufferedImage mask, int x, int y, int width, int height) throws IOException {
        int[] imagePixels = image.getRGB(x, y, width, height, null, 0, width);
        int[] maskPixels = mask.getRGB(0, 0, width, height, null, 0, width);

        for (int i = 0; i < imagePixels.length; i++) {
            int color = imagePixels[i]; 
            int alpha = maskPixels[i] & 0xff000000; 
            imagePixels[i] = color & alpha;
        }

        image.setRGB(0, 0, width, height, imagePixels, 0, width);
//        ImageIO.write(image, "png", new File("res/hehe" + x + ".png"));
        return image;
    }
    
    /**
     * Charger le fichier heightMap (les courleurs BlackGray) et le transformer à une carte avec 4 nvieau de l'hauteur
     * @param image : heightMap
     * @return : 
     * @throws IOException
     */
    public static BufferedImage heightmapGrayScaleToInterface(BufferedImage image) throws IOException {
    	int black; 
        int width = image.getWidth();
        int height = image.getHeight();
        
        BufferedImage[] topology = new BufferedImage[4];
    	
        int[] imagePixels = image.getRGB(0, 0, width, height, null, 0, width);
    	int[][] topoPixels = new int[topology.length][imagePixels.length];
    	
    	for (int i = 0; i < topology.length; i++) {
    		topology[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    		
    		for (int j = 0; j < imagePixels.length; j++) {
    			black = imagePixels[j] & 0x000000ff;
    			if (black >= getMin(i + 1) && black <= getMax(i + 1)) {
    				topoPixels[i][j] = 0xff000000;
    			} else {
    				topoPixels[i][j] = 0x00000000;
    			}
    		}
    		topology[i].setRGB(0, 0, width, height, topoPixels[i], 0, width);
    		topology[i] = applyGrayscaleMaskToAlpha(ImageIO.read(new File("res/mask/topo" + (i + 1) + "mask.png")), topology[i], 0, 0, width, height);
    		topoPixels[i] = topology[i].getRGB(0, 0, width, height, null, 0, width);
    	}
    	
    	for (int i = 0; i < imagePixels.length; i++) {
    		for (int j = 1; j < topology.length; j++) {
    			 if ((topoPixels[j][i] >> 24) > 0 || topoPixels[j][i] < 0) {
    				 topoPixels[0][i] = topoPixels[j][i];    				
    			}
    		}
    	}
    	
    	topology[0].setRGB(0, 0, width, height, topoPixels[0], 0, width);
    	//ImageIO.write(topology[0], "png", new File("res/xor.png"));
    	return topology[0];
    }
    
    /**
     * UpdateHeightMapInterface : Mise a jour les modifications de l'utilisateur  
     * @param srcImg : l'image d'origin
     * @param newImg : l'image qui contient la modification
     * @param topo   : le type d'outil utilisé
     * @param x		 : la position de la modification
     * @param y		 : la position de la modification
     * @param width  : la taille de la modification
     * @param height : la taille de la modification
     * @return
     * @throws IOException
     */
    public static BufferedImage updateHeightMapInterface(BufferedImage srcImg, BufferedImage newImg, int topo, int x, int y, int width, int height) throws IOException {
    	BufferedImage topology = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    	
        int[] imagePixels = srcImg.getRGB(x, y, width, height, null, 0, width);
    	topology = applyGrayscaleMaskToAlpha(ImageIO.read(new File("res/mask/topo" + topo + "mask.png")), newImg, x, y, width, height);
    	int[] topoPixels = topology.getRGB(0, 0, width, height, null, 0, width);
    	int[] newPixels = newImg.getRGB(0, 0, width, height, null, 0, width);
    	
       	for (int i = 0; i < topoPixels.length; i++) {          		
    		if ((topoPixels[i] >> 24) > 0 || topoPixels[i] < 0) {
    			imagePixels[i] = topoPixels[i];
    		} else if ((newPixels[i] >> 24) > 0 || newPixels[i] < 0) {
    			imagePixels[i] = 0;
    		}       		
    	}
    	
       	srcImg.setRGB(x, y, width, height, imagePixels, 0, width);
       	return srcImg;
    }
    
    /**
     * Appliquer les modifications sur le heightMap (BlackGray) 
     * @param srcImg : heightmap
     * @param newImg : l'image contient la modification
     * @param x		 : la position de la modification 
     * @param y		 : la position de la modification
     * @param width	 : la taille de la modification
     * @param height : la taille de la modification
     * @return		 : le nouveau heightmap
     * @throws IOException
     */
    public static BufferedImage applyNewCanvas(BufferedImage srcImg, BufferedImage newImg, int x, int y, int width, int height) throws IOException {
    	int[] imagePixels = srcImg.getRGB(x, y, width, height, null, 0, width);
    	int[] newPixels = newImg.getRGB(0, 0, width, height, null, 0, width);
    	
    	for (int i = 0; i < imagePixels.length; i++) {          		
    		if ((newPixels[i] >> 24) > 0 || newPixels[i] < 0) {
    			imagePixels[i] = newPixels[i];
    		}       		
    	}
    	
       	srcImg.setRGB(x, y, width, height, imagePixels, 0, width);
//       	ImageIO.write(srcImg, "png", new File("res/xor.png"));
    	return srcImg;
    }
    
    /**
     * Retirer la valeur maximal dans chaque niveau de l'hauteur
     * @param topology : le niveau de l'hauteur
     * @return
     */
    public static int getMax(int topology) {
    	if (topology == 1) {
    		return 50;
    	} else if (topology == 2) {
    		return 125;
    	} else if (topology == 3) {
    		return 200;
    	} else { // topology == 4
    		return 255; 
    	} 
    }
    
    /**
     * Retirer la valeur minimal dans chaque niveau de l'hauteur
     * @param topology : le niveau de l'hauteur
     * @return
     */
    public static int getMin(int topology) {
    	if (topology == 1) {
    		return 0;
    	} else if (topology == 2) {
    		return 50 + 1;
    	} else if (topology == 3) {
    		return 125 + 1;
    	} else { // topology == 4
    		return 200 + 1; 
    	} 
    }
}
