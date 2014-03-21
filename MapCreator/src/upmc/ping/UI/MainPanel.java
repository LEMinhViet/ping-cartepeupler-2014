package upmc.ping.UI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import upmc.ping.Object.ObjectList;
import upmc.ping.Utils.ImageUtils;
import upmc.ping.app.Constants;
import upmc.ping.Object.Object;

public class MainPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 1024;
	
	private MainUI mainUI;
	private JLabel canvasLabel;
	
	// Garder le heighmap sous forme blackgray
	private BufferedImage srcHeightMap;	
	private BufferedImage canvasMain;			/*  Combiner tous les canvas */
	private BufferedImage canvasHeightMap;		/*  Contient le canvas qui affiche les hauteur de la terre */
	private BufferedImage canvasTexture;		/*  Contient les couleur des differents textures */
	private BufferedImage canvasRoad;			/*  Contient les chemin */
	private BufferedImage canvasEnvironment;	/*  Contient les environements */
	private BufferedImage canvasTransport;		/*  Contient les objets */
	
	private BufferedImage srcHeightMap_Backup;
	private BufferedImage canvasHeightMap_BackUp;
	private BufferedImage canvasTexture_BackUp;		
	private BufferedImage canvasRoad_BackUp;			
	
	private BufferedImage[] topology;
	
	private BufferedImage canvasTmp;
	
	private ObjectList treeList = new ObjectList("treedraw");
	private ObjectList rockList = new ObjectList("rockdraw");
	private ObjectList bulldozerList = new ObjectList("bulldozer");
	private ObjectList sawList = new ObjectList("saw");
	private ObjectList axeList = new ObjectList("axe");
	private ObjectList boatList = new ObjectList("boat");
	private ObjectList houseList = new ObjectList("house");
	private ObjectList teleportList = new ObjectList("teleport");
	private ObjectList horseList = new ObjectList("horse");	
		
	private Graphics2D gMain;
	private Graphics2D gHeightMap;
	private Graphics2D gTexture;
	private Graphics2D gTmp;
	
	private int currentX, currentY;
		
	public MainPanel(LayoutManager layout, MainUI mainUI) {
		super(layout);
		this.mainUI = mainUI;
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		try {
			topology = new BufferedImage[4]; 
			for (int i = 0; i < topology.length; i++) {
				 topology[i] = ImageIO.read(new File("res/mask/topo" + (i + 1) + "mask.png"));
			}
			
			resetCanvas(true);
			init();
			drawCanvas();
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void init() throws IOException {
		canvasLabel = new JLabel(new ImageIcon(canvasMain));
		ImageIO.write(canvasMain, "png", new File("res/out.png"));
		add(canvasLabel, null);
		canvasLabel.addMouseListener(new MouseAdapter() {
	        public void mousePressed(MouseEvent event) {
	            mainPanelMousePressed(event, mainUI.getToolType(), mainUI.getTool());
	        }
	    });
		
	    canvasLabel.addMouseMotionListener(new MouseMotionAdapter() {
	        public void mouseDragged(MouseEvent event) {
	            mainPanelMouseDragged(event);
	        }
	    });
	}
	
	/**
	 * Quand on "drag" le souris, si le souris est dans la carte
	 * => l'utilisateur change la carte 
	 * => mise a jour les canvas
	 * @param event
	 */
	private void mainPanelMouseDragged(MouseEvent event) {
	    currentX = event.getX();
	    currentY = event.getY();
	    
	    putObjectDensity(currentX, currentY, 
	    		mainUI.getToolType(), mainUI.getTool(), mainUI.getDensity(), mainUI.getValue());
	    try {
	    	updateCanvas(mainUI.getValue(), mainUI.getToolType(), mainUI.getTool());
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	private void mainPanelMousePressed(MouseEvent event, int tooltype, int tool) {
	    currentX = event.getX();
	    currentY = event.getY();
	    
	    if (tooltype == Constants.TOPOLOGY)  	srcHeightMap_Backup = ImageUtils.deepCopy(srcHeightMap);
		if (tooltype == Constants.TOPOLOGY) 	canvasHeightMap_BackUp = ImageUtils.deepCopy(canvasHeightMap);
		if (tooltype == Constants.TEXTURE)		canvasTexture_BackUp = ImageUtils.deepCopy(canvasTexture);	
		if (tooltype == Constants.ROAD)			canvasRoad_BackUp = ImageUtils.deepCopy(canvasRoad);		
		mainUI.setIsUndo(false);
		
		putObject(currentX, currentY, mainUI.getToolType(), mainUI.getTool());	    
	    try {
			updateCanvas(mainUI.getValue(), mainUI.getToolType(), mainUI.getTool());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void putObject(int x, int y, int tooltype, int tool) {
		if (tooltype == Constants.ENVIRONMENT) {
			if (tool == 1) {
				treeList.add(new Object(x, y, treeList.getSize()), false, true);
			} else if (tool == 2) {
				rockList.add(new Object(x, y, rockList.getSize()), false, true);
			} else if (tool == 3) {
				bulldozerList.add(new Object(x, y, bulldozerList.getSize()), true, true);
			} else if (tool == 4) {
				sawList.add(new Object(x, y, sawList.getSize()), true, true);
			} else if (tool == 5) {
				axeList.add(new Object(x, y, axeList.getSize()), true, true);
			}
		} else if (tooltype == Constants.TRANSPORT) {
			if (tool == 1) {
				boatList.add(new Object(x, y, boatList.getSize()), true, true);
			} else if (tool == 2) {
				horseList.add(new Object(x, y, horseList.getSize()), true, true);
			} else if (tool == 3) {
				teleportList.add(new Object(x, y, teleportList.getSize()), true, true);
			} else if (tool == 4) {
				houseList.add(new Object(x, y, houseList.getSize()), true, true);
			}
 		}
	}
	
	/**
	 * 
	 */
	public void putObjectDensity(int x, int y, int tooltype, int tool, int density, int value) {
		if (tooltype == Constants.ENVIRONMENT) {
			if (tool == 1) {
				treeList.addDensity(x, y, density, value);
			} else if (tool == 2) {
				rockList.addDensity(x, y, density, value);
			}
		}
	}
	
	/**
	 * Re-initialiser les canvas pour une nouvelle carte
	 */
	public void resetCanvas(boolean resetAll) {
		if (resetAll)	srcHeightMap = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);		
		if (resetAll)	canvasMain = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		if (resetAll)	canvasHeightMap = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		canvasTexture = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		canvasRoad = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		canvasEnvironment = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		canvasTransport = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		gHeightMap = canvasHeightMap.createGraphics();
		gHeightMap.setColor(Color.BLACK);
		gHeightMap.fillRect(0, 0, WIDTH, HEIGHT);
		
		srcHeightMap = canvasHeightMap;
		try {
			canvasHeightMap = ImageUtils.heightmapGrayScaleToInterface(canvasHeightMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		gTexture = canvasTexture.createGraphics();    
		gTexture.setColor(mainUI.getColorTexture(0));
		gTexture.fillRect(0, 0, WIDTH, HEIGHT);
		
		srcHeightMap_Backup = ImageUtils.deepCopy(srcHeightMap);
		canvasHeightMap_BackUp = ImageUtils.deepCopy(canvasHeightMap);
		canvasTexture_BackUp = ImageUtils.deepCopy(canvasTexture);	
		canvasRoad_BackUp = ImageUtils.deepCopy(canvasRoad);			
	}
	
	/**
	 * Mise a jour les canvas dan le cas ou l'utilisateur change la carte :
	 * Traiter les changements de la carte avec les differents outils 
	 * Il y a 5 groupes d'outil : 
	 * - Topologie : 4 niveau de l'hauteur
	 * - Texture : 4 texture maintenant
	 * - Environement : 
	 * - Road : 1 type de chemin
	 * - Object :
	 * @throws IOException 
	 */
	public void updateCanvas(int value, int tooltype, int tool) throws IOException {
		int updateX = currentX - (value / 2);
		int updateY = currentY - (value / 2);
		int updateValueX = value;
		int updateValueY = value;
		if (updateX < 0) {
			updateValueX += updateX;
			updateX = 0;
		} else if (updateX + updateValueX > WIDTH) {
			updateValueX = WIDTH - updateX;
		}
		
		if (updateY < 0) {
			updateValueY += updateY;
			updateY = 0;
		} else if (updateY + updateValueY > HEIGHT) {
			updateValueY = HEIGHT - updateY;
		}
		
		if (updateValueX <= 0 || updateValueY <= 0) {
			return;
		}
		
		if (tooltype == Constants.TOPOLOGY) {
			canvasTmp = new BufferedImage(value, value, BufferedImage.TYPE_INT_ARGB);
			gTmp = canvasTmp.createGraphics();
			gTmp.setPaint(mainUI.getTopo());
			gTmp.fillOval(0, 0, value, value);
	        
	        srcHeightMap = ImageUtils.applyNewCanvas(srcHeightMap, canvasTmp, tool,
	        												updateX, updateY, updateValueX, updateValueY);
	        canvasHeightMap = ImageUtils.updateHeightMapInterface(canvasHeightMap, srcHeightMap, topology,
						updateX, updateY, updateValueX, updateValueY);
		} else if (tooltype == Constants.TEXTURE) {
			canvasTmp = new BufferedImage(value, value, BufferedImage.TYPE_INT_ARGB);
			gTmp = canvasTmp.createGraphics();
			gTmp.setPaint(mainUI.getColorTexture(tool));
			gTmp.fillOval(0, 0, value, value);
            
            canvasTexture = ImageUtils.applyNewCanvas(canvasTexture, canvasTmp,
            												updateX, updateY, updateValueX, updateValueY);
		} else if (tooltype == Constants.ENVIRONMENT) {
			canvasEnvironment = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
			gTmp = canvasEnvironment.createGraphics();
			treeList.paint(gTmp);
			rockList.paint(gTmp);
			bulldozerList.paint(gTmp);
			sawList.paint(gTmp);
			axeList.paint(gTmp);			
		} else if (tooltype == Constants.ROAD) {
			canvasTmp = new BufferedImage(value, value, BufferedImage.TYPE_INT_ARGB);
			gTmp = canvasTmp.createGraphics();
			gTmp.setPaint(new Color(200, 0, 0));
			gTmp.fillOval(0, 0, value, value);
            
            canvasRoad = ImageUtils.applyNewCanvas(canvasRoad, canvasTmp, 
            												updateX, updateY, updateValueX, updateValueY);			
		} else if (tooltype == Constants.TRANSPORT) {
			canvasTransport = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
			gTmp = canvasTransport.createGraphics();
			boatList.paint(gTmp);
			horseList.paint(gTmp);
			teleportList.paint(gTmp);
			houseList.paint(gTmp);
		}
		
		drawCanvas();
    }
	
	/**
	 * afficher tous les canvas 
	 */
	public void drawCanvas() {
		gMain = canvasMain.createGraphics();
														gMain.drawImage(canvasTexture, 0, 0, null);
		if (mainUI.isShowRoad())						gMain.drawImage(canvasRoad, 0, 0, null);
														gMain.drawImage(canvasHeightMap, 0, 0, null);
		if (mainUI.isShowEnvironment())	 				gMain.drawImage(canvasEnvironment, 0, 0, null);
		if (mainUI.isShowTransport())		 			gMain.drawImage(canvasTransport, 0, 0, null);
		
		canvasLabel.setIcon(new ImageIcon(canvasMain));
		revalidate();
		mainUI.repaint();
	}
	
	public BufferedImage getSrcHeightMap() {
		try {
			ImageIO.write(srcHeightMap, "png", new File("res/hee.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return srcHeightMap;
	}
	
	public void undoHeightMap() { 
		canvasTmp = ImageUtils.deepCopy(srcHeightMap);
		srcHeightMap = ImageUtils.deepCopy(srcHeightMap_Backup);	
		srcHeightMap_Backup = ImageUtils.deepCopy(canvasTmp);	
		
		canvasTmp = ImageUtils.deepCopy(canvasHeightMap);
		canvasHeightMap = ImageUtils.deepCopy(canvasHeightMap_BackUp);	
		canvasHeightMap_BackUp = ImageUtils.deepCopy(canvasTmp);	
	}
	
	public void undoTexture() { 
		canvasTmp = ImageUtils.deepCopy(canvasTexture);
		canvasTexture = ImageUtils.deepCopy(canvasTexture_BackUp);	
		canvasTexture_BackUp = ImageUtils.deepCopy(canvasTmp);	
	}
	
	public void undoRoad() { 
		canvasTmp = ImageUtils.deepCopy(canvasRoad);
		canvasRoad = ImageUtils.deepCopy(canvasRoad_BackUp);	
		canvasRoad_BackUp = ImageUtils.deepCopy(canvasTmp);	
	}
	
	public void saveJME3SDK(String path, int result) throws IOException {
		ImageIO.write(srcHeightMap, "png", new File(path + "_heightmap.png"));
//		gTexture = canvasTexture.createGraphics();
//		gTexture.drawImage(canvasTexture, 0, 0, null);
		saveTexture(path);
		saveObjects(path);
		saveRoad(path);
		ImageIO.write(canvasTexture, "png", new File(path + "_texture.png"));
		if (result > SaveTypeDialog.JME3SDK * 2) {
			ImageIO.write(canvasMain, "png", new File(path + "_minimap.png"));
		}
	}
	
	public void saveDefault(String path, int result) throws IOException {
		ImageIO.write(srcHeightMap, "png", new File(path + "_heightmap.png"));
		saveTexture(path);
		saveObjects(path);
		saveRoad(path);
		if (result > SaveTypeDialog.PING * 2) {
			ImageIO.write(canvasMain, "png", new File(path + "_minimap.png"));
		}
	}
	
	public void saveObjects(String path) {
		new File(path).mkdir();
		for (Object obj : treeList) 		obj.save(path, "tree", "backup", "trees/trees-02.jmex", 0.0, "3", 0.0);
		for (Object obj : rockList)			obj.save(path, "rock", "backup", "rock/rock-02.jmex", 0.0, "4", 0.0);
		for (Object obj : bulldozerList) 	obj.save(path, "bulldozer", "backup", "bulldozer/bulldozer-02.jmex", 0.0, "5", 0.0);
		for (Object obj : sawList) 			obj.save(path, "saw", "backup", "saw/saw-02.jmex", 0.0, "6", 0.0);
		for (Object obj : axeList) 			obj.save(path, "axe", "backup", "axe/axe-02.jmex", 0.0, "7", 0.0);
		for (Object obj : boatList) 		obj.save(path, "boat", "backup", "boat/boat-02.jmex", 0.0, "8", 0.0);
		for (Object obj : houseList) 		obj.save(path, "house", "backup", "house/house-02.jmex", 0.0, "9", 0.0);
		for (Object obj : teleportList) 	obj.save(path, "teleport", "backup", "teleport/teleport-02.jmex", 0.0, "10", 0.0);
		for (Object obj : horseList) 		obj.save(path, "horse", "backup", "horse/horse-02.jmex", 0.0, "11", 0.0);
	}
	
	public void saveTexture(String path) {
		int[] imagePixels = canvasTexture.getRGB(0, 0, WIDTH, HEIGHT, null, 0, WIDTH);
		int[][] texturePixels = new int[4][WIDTH * HEIGHT]; 
		BufferedImage texture = new BufferedImage(WIDTH, WIDTH, BufferedImage.TYPE_INT_ARGB);
		
		for (int i = 0; i < imagePixels.length; i++) {
			for (int j = 0; j < 4; j++) {
				Color checkColor = mainUI.getColorTexture(j + 1);
				if (checkColor.getRGB() == imagePixels[i]) {
					texturePixels[j][i] = 0xffffffff;
				} else {
					texturePixels[j][i] = 0x00000000;
				}
			}
		}	
		
		for (int i = 0; i < 4; i++) {
			texture.setRGB(0, 0, WIDTH, HEIGHT, texturePixels[i], 0, WIDTH);
			try {
				ImageIO.write(texture, "png", new File(path + "-" + mainUI.getNameTexture(i + 1) + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveRoad(String path) {
		int[] imagePixels = canvasRoad.getRGB(0, 0, WIDTH, HEIGHT, null, 0, WIDTH);
		BufferedImage texture = new BufferedImage(WIDTH, WIDTH, BufferedImage.TYPE_INT_ARGB);
		
		for (int i = 0; i < imagePixels.length; i++) {
			if (imagePixels[i] != 0x00000000) {
				imagePixels[i] = 0xffffffff;
			} 		
		}	
		
		texture.setRGB(0, 0, WIDTH, HEIGHT, imagePixels, 0, WIDTH);
		try {
			ImageIO.write(texture, "png", new File(path + "-road.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String file) throws IOException {
		// Resize Height Map
		canvasHeightMap = ImageUtils.resize(ImageIO.read(new File(file)), WIDTH, HEIGHT, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		ImageIO.write(canvasHeightMap, "png", new File(file.replace(".png", "_resized.png")));
		// Garder l'image heightmap
		srcHeightMap = canvasHeightMap;
		canvasHeightMap = ImageUtils.heightmapGrayScaleToInterface(canvasHeightMap);
	}
}
