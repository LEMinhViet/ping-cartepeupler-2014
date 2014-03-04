package upmc.ping.Utils;

/**
 * Code sous licence LGPLv3 (http://www.gnu.org/licenses/lgpl.html)
 *
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 * GCS d- s+:+ a- C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- !y-
 */

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
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
public class RawToPng_1025 {
	private static final Logger logger = Logger.getLogger("RawToPng");

	//arguments d'exécution du main:data/map/flatSO.raw data/map/flatSO
	
	/**
	 * @param args
	 * 	chemin vers le fichier en entrée
	 * 	nom du fichier de sortie (sans extension)
	 * @return 
	 * 	chemin vers le fichier de sortie
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BufferedImage image = conversion(args[0]);
		ImageIO.write(image, "png", new File(args[1]+"_1025.png"));
	}
	
	public static BufferedImage conversion(String fichierEntree) throws IOException {
		
//		FileInputStream fileInputStream = new FileInputStream("data/map/flatSO.raw");
//		FileInputStream fileInputStream = new FileInputStream("data/map/nexus33.raw");
		FileInputStream fileInputStream = new FileInputStream(fichierEntree);
		DataInputStream dataInputStream = new DataInputStream(fileInputStream);
		long max = 0;
//		long min = 65535;//l'entier maximal sur 16bits
		char[][] data = new char[1025][1025];
		char datatmp = 0;
		
		for (int y=0;y<129;y++){
			for (int x=0;x<129;x++) {
				datatmp = (char)dataInputStream.readUnsignedShort();
				if ((y>0)&&(x>0)&&(y<128)&&(x<128)) {//les bords sont a traiter differement
					for (int i = -4; i < 5; i++) {
						for (int j = -4; j < 5; j++) {
							System.out.print("["+(x*8+i)+","+(y*8+j)+"]:"+(short)datatmp+"|");
							data[x*8+i][y*8+j] = (char)datatmp;//on applique un patch de pixels de la même couleur							
						}
						System.out.println();
					}
					System.out.println();
				} else if((y==0)&&(x==0)) {
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 5; j++) {
							data[x*8+i][y*8+j] = (char)datatmp;
						}
					}
				} else if((y==0)&&(x!=0)&&(x!=128)) {
					for (int i = -4; i < 5 ; i++) {
						for (int j = 0 ; j < 5 ; j++) {
							data[x*8+i][y*8+j] = (char)datatmp;
						}
					}
				} else if((y==0)&&(x==128)) {
					for (int i = -4; i < 0 ; i++) {
						for (int j = 0; j < 5; j++) {
							data[x*8+i][y*8+j] = (char)datatmp;
						}
					}
				} else if((y==128)&&(x==0)) {
					for (int i = 0; i < 5; i++) {
						for (int j = -4; j <0 ; j++) {
							data[x*8+i][y*8+j] = (char)datatmp;
						}
					}
				} else if((y==128)&&(x!=0)&&(x!=128)) {
					for (int i = -4; i < 5; i++) {
						for (int j = -4; j < 0; j++) {
							data[x*8+i][y*8+j] = (char)datatmp;
						}
					}
				} else if((y==128)&&(x==128)) {
					for (int i = -4; i < 0; i++) {
						for (int j = -4; j < 0; j++) {
							data[x*8+i][y*8+j] = (char)datatmp;
						}
					}
				} else if((y!=0)&&(y!=128)&&(x==0)) {
					for (int i = 0; i < 5; i++) {
						for (int j = -4; j < 5; j++) {
							data[x*8+i][y*8+j] = (char)datatmp;
						}
					}
				} else if((y!=0)&&(y!=128)&&(x==128)) {
					for (int i = -4; i < 0; i++) {
						for (int j = -4; j < 5; j++) {
							data[x*8+i][y*8+j] = (char)datatmp;
						}
					}
				} 
				if (datatmp>max) {
					max = datatmp;
				}
//				if (data[x][y]<min) {
//					min = data[x][y];
//				}
			}
		}
		dataInputStream.close();
		
		
//		logger.info("min : "+min);
		logger.info("max : "+max);

		BufferedImage image = new BufferedImage(1025, 1025, BufferedImage.TYPE_INT_RGB);
		for (int y=0;y<1025;y++){
			for (int x=0;x<1025;x++) {
				int c = (int)(data[x][y]*255/max);
				image.setRGB(x, y, c+(c<<8)+(c<<16));
			}
		}
		
		return(image);
	}
	
}
