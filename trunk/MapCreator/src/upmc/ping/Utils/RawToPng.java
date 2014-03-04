/**
 * Code sous licence LGPLv3 (http://www.gnu.org/licenses/lgpl.html)
 *
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 * GCS d- s+:+ a- C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- !y-
 */
package upmc.ping.Utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Convertir e terrain pour LA2 
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * Code sous licence LGPLv3 (http://www.gnu.org/licenses/lgpl.html)
 *
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 * GCS d- s+:+ a- C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- !y-
 */
public class RawToPng {
	private static final Logger logger = Logger.getLogger("RawToPng");

	/**
	 * TODO commentaire RawToPng.main
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(RawToPng.class.getResourceAsStream("/data/map/flat.raw"));
		DataInputStream dis = new DataInputStream(bis);
		int[][] data = new int[129][129];
		int max = 0;
		for (int y=0;y<129;y++)
			for (int x=0;x<129;x++) {
				data[x][y] = dis.readUnsignedShort();
				max = Math.max(max, data[x][y]);
			}
		
		logger.info("max : "+max);

		BufferedImage image = new BufferedImage(129, 129, BufferedImage.TYPE_INT_RGB);
		for (int y=0;y<129;y++)
			for (int x=0;x<129;x++) {
				int c = data[x][y]*255/max;
				image.setRGB(x, y, c+(c<<8)+(c<<16));
			}
		
		ImageIO.write(image, "png", new File("data/map/flat.png"));
	}

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
