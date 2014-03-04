package upmc.ping.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import upmc.ping.Utils.ImageUtils;

public class MainUI {
	private final int WIDTH = 1024;
	private final int HEIGHT = 1024;
	
	// LES TYPES D'OUTIL 
	private final int TOPOLOGY = 1;
	private final int TEXTURE = 2;
	private final int ENVIRONMENT = 3;
	private final int ROAD = 4;
	private final int OBJECT = 5;
	
	// QUEQUELS COMPOSENTS 
	private JFrame frmMapEditor;
	private JPanel mainPanel;
	private JLabel canvasLabel;
	private JScrollPane scrollPane;
	private JLabel lblValue;
	
	// Garder le heighmap sous forme blackgray
	private BufferedImage srcHeightMap;
	
	private BufferedImage canvasMain;			/*  Combiner tous les canvas */
	private BufferedImage canvasHeightMap;		/*  Contient le canvas qui affiche les hauteur de la terre */
	private BufferedImage canvasTexture;		/*  Contient les couleur des differents textures */
	private BufferedImage canvasRoad;			/*  Contient les chemin */
	private BufferedImage canvasEnvironment;	/*  Contient les environements */
	private BufferedImage canvasObject;			/*  Contient les objets */
	
	private BufferedImage canvasTmp;
		
	private Graphics2D gMain;
	private Graphics2D gHeightMap;
	private Graphics2D gTexture;
	
	private double value = 50;
	private int currentX, currentY, oldX, oldY;
	
	private ArrayList<JToggleButton> buttons = new ArrayList<JToggleButton>();
	
	private String selectingFile = "";
	
	private int tooltype, tool;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI();
					window.frmMapEditor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainUI() throws IOException {
		initialize();		
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		// Initialiser les composants UI
		frmMapEditor = new JFrame();
		frmMapEditor.setTitle("Map Editor");
		frmMapEditor.setBounds(100, 100, 800, 600);
		frmMapEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		resetCanvas();
	    
		JMenuBar menuBar = new JMenuBar();
		frmMapEditor.setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu("File");
		menuBar.add(mnFichier);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mnFichier.add(mntmNew);
		
		JMenuItem mntmLoad = new JMenuItem("Load");
		mnFichier.add(mntmLoad);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFichier.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As");
		mnFichier.add(mntmSaveAs);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFichier.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		frmMapEditor.getContentPane().setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		frmMapEditor.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBounds(new Rectangle(0, 0, 200, 200));
		
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		scrollPane.setViewportView(mainPanel);
		
		canvasLabel = new JLabel(new ImageIcon(canvasMain));
		mainPanel.add(canvasLabel, null);

		Box ToolsBox = Box.createVerticalBox();
		ToolsBox.setBorder(BorderFactory.createTitledBorder(" Tools "));
		frmMapEditor.getContentPane().add(ToolsBox, BorderLayout.EAST);
		
		Box topologyBox = Box.createHorizontalBox();
		topologyBox.setBorder(BorderFactory.createTitledBorder(" Topologies "));
		ToolsBox.add(topologyBox);
		
		JToggleButton btnTopo1 = new JToggleButton("");
		makeImageButton(btnTopo1, "res/img/topo1.jpg", "Topo1");
		topologyBox.add(btnTopo1);
		
		Component rigidAreaTopo1 = Box.createRigidArea(new Dimension(10, 10));
		topologyBox.add(rigidAreaTopo1);
		
		JToggleButton btnTopo2 = new JToggleButton("");		
		makeImageButton(btnTopo2, "res/img/topo2.jpg", "Topo2");
		topologyBox.add(btnTopo2);
		
		Component rigidAreaTopo2 = Box.createRigidArea(new Dimension(10, 10));
		topologyBox.add(rigidAreaTopo2);
		
		JToggleButton btnTopo3 = new JToggleButton("");
		makeImageButton(btnTopo3, "res/img/topo3.jpg", "Topo3");
		topologyBox.add(btnTopo3);
		
		Component rigidAreaTopo3 = Box.createRigidArea(new Dimension(10, 10));
		topologyBox.add(rigidAreaTopo3);
		
		JToggleButton btnTopo4 = new JToggleButton("");
		makeImageButton(btnTopo4, "res/img/topo4.jpg", "Topo4");
		topologyBox.add(btnTopo4);
		
		Box TextureBox = Box.createHorizontalBox();
		TextureBox.setBorder(BorderFactory.createTitledBorder(" Textures "));
		ToolsBox.add(TextureBox);
		
		JToggleButton btnTexture1 = new JToggleButton("");
		makeImageButton(btnTexture1, "res/img/sable05_ico.jpg", "Texture1");
		TextureBox.add(btnTexture1);
		
		Component rigidAreaTexture1 = Box.createRigidArea(new Dimension(10, 10));
		TextureBox.add(rigidAreaTexture1);
		
		JToggleButton btnTexture2 = new JToggleButton("");
		makeImageButton(btnTexture2, "res/img/nicegrass_ico.jpg", "Texture2");
		TextureBox.add(btnTexture2);
		
		Component rigidAreaTexture2 = Box.createRigidArea(new Dimension(10, 10));
		TextureBox.add(rigidAreaTexture2);
		
		JToggleButton btnTexture3 = new JToggleButton("");
		makeImageButton(btnTexture3, "res/img/baserock_ico.jpg", "Texture3");
		TextureBox.add(btnTexture3);
		
		Component rigidAreaTexture3 = Box.createRigidArea(new Dimension(10, 10));
		TextureBox.add(rigidAreaTexture3);
		
		JToggleButton btnTexture4 = new JToggleButton("");
		makeImageButton(btnTexture4, "res/img/terre01_ico.jpg", "Texture4");
		TextureBox.add(btnTexture4);
		
		Box EnvironmentBox = Box.createHorizontalBox();
		EnvironmentBox.setBorder(BorderFactory.createTitledBorder(" Environments "));
		ToolsBox.add(EnvironmentBox);
		
		JToggleButton btnEnvironment1 = new JToggleButton("");
		EnvironmentBox.add(btnEnvironment1);
		
		Component rigidAreaEnviron1 = Box.createRigidArea(new Dimension(10, 10));
		EnvironmentBox.add(rigidAreaEnviron1);
		
		JToggleButton btnEnvironment2 = new JToggleButton("");
		EnvironmentBox.add(btnEnvironment2);
		
		Component rigidAreaEnviron2 = Box.createRigidArea(new Dimension(10, 10));
		EnvironmentBox.add(rigidAreaEnviron2);
		
		JToggleButton btnEnvironment3 = new JToggleButton("");
		EnvironmentBox.add(btnEnvironment3);
		
		Component rigidAreaEnviron3 = Box.createRigidArea(new Dimension(10, 10));
		EnvironmentBox.add(rigidAreaEnviron3);
		
		JToggleButton btnEnvironment4 = new JToggleButton("");
		EnvironmentBox.add(btnEnvironment4);
		
		Box RoadBox = Box.createHorizontalBox();
		RoadBox.setBorder(BorderFactory.createTitledBorder(" Road "));
		ToolsBox.add(RoadBox);
		
		Component roadRigidArea_left = Box.createRigidArea(new Dimension(20, 10));
		RoadBox.add(roadRigidArea_left);
		
		JToggleButton btnRoad = new JToggleButton("");
		makeImageButton(btnRoad, "res/img/road.jpg", "Road");
		RoadBox.add(btnRoad);
		
		Component roadRigidArea_right = Box.createRigidArea(new Dimension(20, 10));
		RoadBox.add(roadRigidArea_right);
		
		Box sliderBox = Box.createHorizontalBox();
		sliderBox.setBorder(new TitledBorder(null, " Brush Size ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ToolsBox.add(sliderBox);
		
		JSlider sliderValue = new JSlider();
		sliderValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sliderValue.setMinorTickSpacing(5);
		sliderValue.setSnapToTicks(true);
		sliderValue.setPaintTicks(true);
		sliderValue.setPreferredSize(new Dimension(100, 20));
		sliderBox.add(sliderValue);
		
		lblValue = new JLabel("50");
		lblValue.setFont(new Font("Tahoma", Font.PLAIN, 12));
		sliderBox.add(lblValue);
		
	    drawCanvas();
	    
		/************************************************************************************************************/
		/* EVENT HANDLER */	
	    /**
	     * le button LOAD : charger le fichier raw, l'agrandir et 
	     */
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                JFileChooser openFile = new JFileChooser();
                //FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
                FileFilter imageFilter = new FileNameExtensionFilter("Raw files", "raw");
                
                openFile.setCurrentDirectory(new File("F:/Learning/Exercices/UPMC_S4V1/PING/Documents_fournis/Exemples_de_cartes"));  
                openFile.setAcceptAllFileFilterUsed(false);
                openFile.addChoosableFileFilter(imageFilter);
                
                if (openFile.showOpenDialog(frmMapEditor) == JFileChooser.APPROVE_OPTION) {
                	try {
                		selectingFile = openFile.getCurrentDirectory() + "\\" + openFile.getSelectedFile().getName().replace(".raw", ".png");
                		ImageUtils.rawToPng(openFile.getSelectedFile().getAbsolutePath(), selectingFile);
						
                		// Resize Height Map
                		canvasHeightMap = ImageUtils.resize(ImageIO.read(new File(selectingFile)), WIDTH, HEIGHT, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
						ImageUtils.writeJPEG(canvasHeightMap, selectingFile.replace(".png", "_resized.jpg"));
						// Save the height map image
						srcHeightMap = canvasHeightMap;
						canvasHeightMap = ImageUtils.heightmapGrayScaleToInterface(canvasHeightMap);
						
						gTexture = canvasTexture.createGraphics();
						gTexture.setColor(getColorTexture(0));
						gTexture.fillRect(0, 0, canvasTexture.getWidth(), canvasTexture.getHeight());
						
						gMain = canvasMain.createGraphics();
						gMain.drawImage(canvasTexture, 0, 0, null);
						gMain.drawImage(canvasHeightMap, 0, 0, null);
						
						canvasLabel.setIcon(new ImageIcon(canvasMain));
						mainPanel.revalidate();
						frmMapEditor.getContentPane().repaint();
					} catch (IOException e) {
						e.printStackTrace();
					}
           
                }                
            }
		});
		
		/**
		 * Le button SaveAs, sauvegarder le fichier heightmap et le fichier texture
		 */
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser saveFile = new JFileChooser();
                
				saveFile.setCurrentDirectory(new File("F:/Learning/Exercices/UPMC_S4V1/PING/Documents_fournis/Exemples_de_cartes"));  
                 
                if (saveFile.showSaveDialog(frmMapEditor) == JFileChooser.APPROVE_OPTION) {
                	try {
						ImageIO.write(srcHeightMap, "png", new File(saveFile.getSelectedFile().getAbsolutePath() + "_heightmap.png"));
						gTexture = canvasTexture.createGraphics();
						gTexture.drawImage(canvasTexture, 0, 0, null);
						ImageIO.write(canvasTexture, "png", new File(saveFile.getSelectedFile().getAbsolutePath() + "_texture.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }                
			}
		});
		
		/**
		 * Quitter l'appli
		 */
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showOptionDialog(frmMapEditor,
                        "Are You Sure to Close this Application?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
			}
		});
		
		/**
		 * Creer une nouvelle carte
		 */
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetCanvas();
			    drawCanvas();
			}
		});
		
		/**
		 * Détecter les utilisations du "mouse" 
		 */
		canvasLabel.addMouseListener(new MouseAdapter() {
	        public void mousePressed(MouseEvent event) {
	            jPanelMousePressed(event);
	        }
	        public void mouseReleased(MouseEvent event) {
	            jPanelMouseReleased(event);
	        }
	    });
		
	    canvasLabel.addMouseMotionListener(new MouseMotionAdapter() {
	        public void mouseDragged(MouseEvent event) {
	            jPanelMouseDragged(event);
	        }
	    });
	    
	    sliderValue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				value = ((JSlider)arg0.getSource()).getValue();
				lblValue.setText((int)value + "");
			}
		});
	}
	
	/**
	 * Quand on "drag" le souris, si le souris est dans la carte
	 * => l'utilisateur change la carte 
	 * => mise a jour les canvas
	 * @param event
	 */
	private void jPanelMouseDragged(MouseEvent event) {
		if (scrollPane.contains(new Point(event.getX() - scrollPane.getHorizontalScrollBar().getValue(), 
										 event.getY() - scrollPane.getVerticalScrollBar().getValue()))
										 &&
										 canvasLabel.contains(event.getPoint())) {
		    currentX = event.getX();
		    currentY = event.getY();	    
		    updateCanvas();		   
			oldX = currentX;
		    oldY = currentY;	    
		}
	}
	
	private void jPanelMouseReleased(MouseEvent event) {
	  
	}

	private void jPanelMousePressed(MouseEvent event) {
	    oldX = event.getX();
	    oldY = event.getY();
	    currentX = oldX;
	    currentY = oldY;
	    		
		/* ZOOM IN - ZOOM OUT CODE */
		/*
		canvasLabel.setIcon(new ImageIcon(ImageUtils.resize(canvasMain, canvasMain.getWidth() / 2, canvasMain.getHeight() / 2, RenderingHints.VALUE_INTERPOLATION_BICUBIC)));
		mainPanel.revalidate();
		*/
	}
	
	/**
	 * Re-initialiser les canvas pour une nouvelle carte
	 */
	public void resetCanvas() {
		srcHeightMap = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
		
		canvasMain = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
		canvasHeightMap = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
		canvasTexture = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
		
		gHeightMap = canvasHeightMap.createGraphics();
		gHeightMap.setColor(getColorTopo(0));
		gHeightMap.fillRect(0, 0, canvasHeightMap.getWidth(), canvasHeightMap.getHeight());
		
		srcHeightMap = canvasHeightMap;
		try {
			canvasHeightMap = ImageUtils.heightmapGrayScaleToInterface(canvasHeightMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		gTexture = canvasTexture.createGraphics();    
		gTexture.setColor(getColorTexture(0));
		gTexture.fillRect(0, 0, canvasTexture.getWidth(), canvasTexture.getHeight());
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
	 */
	public void updateCanvas() {
		int updateX = currentX - ((int)value / 2);
		int updateY = currentY - ((int)value / 2);
		int updateValueX = (int)value;
		int updateValueY = (int)value;
		if (updateX < 0) {
			updateValueX += updateX;
			updateX = 0;
		} else if (updateX + updateValueX > srcHeightMap.getWidth()) {
			updateValueX = srcHeightMap.getWidth() - updateX;
		}
		
		if (updateY < 0) {
			updateValueY += updateY;
			updateY = 0;
		} else if (updateY + updateValueY > srcHeightMap.getHeight()) {
			updateValueY = srcHeightMap.getHeight() - updateY;
		}
		
		if (tooltype == TOPOLOGY) {
			canvasTmp = new BufferedImage((int)value, (int)value, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = canvasTmp.createGraphics();
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2.setPaint(getColorTopo(tool));
	        g2.fillOval(0, 0, (int)value, (int)value);
	        
	        try {
	        	canvasHeightMap = ImageUtils.updateHeightMapInterface(canvasHeightMap, canvasTmp, tool, 
	        															updateX, updateY, updateValueX, updateValueY);
	        	srcHeightMap = ImageUtils.applyNewCanvas(srcHeightMap, canvasTmp,
	        												updateX, updateY, updateValueX, updateValueY);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (tooltype == TEXTURE) {
			canvasTmp = new BufferedImage((int)value, (int)value, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = canvasTmp.createGraphics();
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2.setPaint(getColorTexture(tool));
            g2.fillOval(0, 0, (int)value, (int)value);
            
            try {
            	canvasTexture = ImageUtils.applyNewCanvas(canvasTexture, canvasTmp,
            												updateX, updateY, updateValueX, updateValueY);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (tooltype == ENVIRONMENT) {
			
		} else if (tooltype == ROAD) {
			
		}
		
		drawCanvas();
    }
	
	/**
	 * afficher tous les canvas 
	 */
	public void drawCanvas() {
		gMain = canvasMain.createGraphics();
		gMain.drawImage(canvasTexture, 0, 0, null);
		gMain.drawImage(canvasRoad, 0, 0, null);
		gMain.drawImage(canvasHeightMap, 0, 0, null);
		gMain.drawImage(canvasEnvironment, 0, 0, null);
		gMain.drawImage(canvasObject, 0, 0, null);
		
		canvasLabel.setIcon(new ImageIcon(canvasMain));
		mainPanel.revalidate();
        frmMapEditor.getContentPane().repaint();
	}
	
	/**
	 * Creer un bouton qui est un image 
	 * @param button : la variable du bouton
	 * @param src : la location de l'image 
	 * @param name : le nom du bouton 
	 */
	public void makeImageButton(JToggleButton button, String src, String name) {
		button.setName(name);
		button.setIcon(new ImageIcon(src));
		button.setSelectedIcon(new ImageIcon(src.replace(".", "selected.")));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
		button.addActionListener(new ActionListener() {			 
            public void actionPerformed(ActionEvent e)
            {
            	updateSelectedButton((JToggleButton)e.getSource());
            	String btnName = ((JToggleButton)e.getSource()).getName();
            	getTool(btnName);
            }
        });   
		
		buttons.add(button);
	}
	
	/**
	 * Quand l'utilisateur selectionne un bouton, les autres boutons doivent etre déactivés 
	 */
	public void updateSelectedButton(JToggleButton button) {
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i) != button) {
				buttons.get(i).setSelected(false);
			}
		}
	}

	/**
	 * Retirer le type d'outil et l'outil a partir du nom du bouton
	 * @param name
	 */
	public void getTool(String name) {
		if (name.equals("Topo1")) {
			tooltype = TOPOLOGY;
			tool = 1;
		} else if (name.equals("Topo2")) {
			tooltype = TOPOLOGY;
			tool = 2;
		} else if (name.equals("Topo3")) {
			tooltype = TOPOLOGY;
			tool = 3;
		} else if (name.equals("Topo4")) {
			tooltype = TOPOLOGY;
			tool = 4;
		} else if (name.equals("Texture1")) {
			tooltype = TEXTURE;
			tool = 1;
		} else if (name.equals("Texture2")) {
			tooltype = TEXTURE;
			tool = 2;
		} else if (name.equals("Texture3")) {
			tooltype = TEXTURE;
			tool = 3;
		} else if (name.equals("Texture4")) {
			tooltype = TEXTURE;
			tool = 4;
		} else if (name.equals("Environment1")) {
			tooltype = ENVIRONMENT;
			tool = 1;
		} else if (name.equals("Environment2")) {
			tooltype = ENVIRONMENT;
			tool = 2;
		} else if (name.equals("Environment3")) {
			tooltype = ENVIRONMENT;
			tool = 3;
		} else if (name.equals("Environment4")) {
			tooltype = ENVIRONMENT;
			tool = 4;
		} else if (name.equals("Road")) {
			tooltype = ROAD;
			tool = 1;
		}
	}
	
	/**
	 * Retirer les couleurs pour les niveaux de l'hauteur 
	 * @param tool 
	 * @return
	 */
	public Color getColorTopo(int tool) {
		Color color;
		if (tool == 1) {
			color = new Color(25, 25, 25);
		} else if (tool == 2) {
			color = new Color(100, 100, 100);
		} else if (tool == 3) {
			color = new Color(175, 175, 175);
		} else if (tool == 4) {
			color = new Color(225, 225, 225);
		} else {
			color = new Color(25, 25, 25);
		}
		return color;
	}
	
	/**
	 * Retirer les couleurs pour les textures
	 * @param tool 
	 * @return
	 */
	public Color getColorTexture(int tool) {
		Color color;
		if (tool == 1) {
			color = new Color(209, 193, 160);
		} else if (tool == 2) {
			color = new Color(86, 120, 8);
		} else if (tool == 3) {
			color = new Color(132, 109, 78);
		} else { // if (tool == 4) {
			color = new Color(116, 107, 90);
		}		
		return color;
	}
}
