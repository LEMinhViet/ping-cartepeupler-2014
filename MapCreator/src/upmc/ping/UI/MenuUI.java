package upmc.ping.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import upmc.ping.Utils.ImageUtils;
import upmc.ping.app.Constants;

public class MenuUI extends JMenuBar {

	private static final long serialVersionUID = 1L;

	private MainUI mainUI;
	private MainPanel mainPanel;
	
	private JMenuItem mntmNew; 
	private JMenuItem mntmLoad;
	private JMenuItem mntmSave;
	private JMenuItem mntmSaveAs;
	private JMenuItem mntmReset;
	private JMenuItem mntmExit;
	private JMenuItem mntmUndo;
	private JMenuItem mntmRedo;
	private JMenuItem mntmHelp;
	private JMenuItem mntmAbout;
	
	private String selectingFile = "";
	
	private boolean isUndo = false;
	
	public MenuUI(MainUI mainUI) {
		super();
		this.mainUI = mainUI;
		this.mainPanel = mainUI.getMainPanel();
		init();
		eventHandler();
	}
	
	private void init() {
		JMenu mnFichier = new JMenu("File");
		add(mnFichier);
		
		mntmNew = new JMenuItem("New");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFichier.add(mntmNew);
		
		mntmLoad = new JMenuItem("Open File...");
		mntmLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFichier.add(mntmLoad);
		
		JSeparator separatorSave = new JSeparator();
		mnFichier.add(separatorSave);
		
//		mntmSave = new JMenuItem("Save");
//		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
//		mnFichier.add(mntmSave);
		
		mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFichier.add(mntmSaveAs);
		
		JSeparator separatorExit = new JSeparator();
		mnFichier.add(separatorExit);
		
		mntmReset = new JMenuItem("Reset");
		mnFichier.add(mntmReset);
		
		mntmExit = new JMenuItem("Exit");
		mnFichier.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		add(mnEdit);
		
		mntmUndo = new JMenuItem("Undo");
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		mnEdit.add(mntmUndo);
		
		mntmRedo = new JMenuItem("Redo");
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
		mnEdit.add(mntmRedo);
		
		JMenu mnHelp = new JMenu("Help");
		add(mnHelp);
		
		mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);
		
		mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
	}
	
	private void eventHandler() {
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
                
                if (openFile.showOpenDialog(mainUI.getFrame()) == JFileChooser.APPROVE_OPTION) {
                	try {
                		mainPanel.resetCanvas(true);
                		
                		selectingFile = openFile.getCurrentDirectory() + "\\" + openFile.getSelectedFile().getName().replace(".raw", ".png");
                		ImageUtils.rawToPng(openFile.getSelectedFile().getAbsolutePath(), selectingFile);
						
                		mainPanel.loadMap(selectingFile);						
						mainPanel.drawCanvas();
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
                 
                if (saveFile.showSaveDialog(mainUI.getFrame()) == JFileChooser.APPROVE_OPTION) {
                	SaveTypeDialog saveType = new SaveTypeDialog();
                	int result = saveType.showDialog();

                	if (result / 2 == SaveTypeDialog.JME3SDK) {
                		try {
    						mainPanel.saveJME3SDK(saveFile.getSelectedFile().getAbsolutePath(), result);
    					} catch (IOException e) {
    						e.printStackTrace();
    					}
                	} else { // Save by Default
                		try {
    						mainPanel.saveDefault(saveFile.getSelectedFile().getAbsolutePath(), result);
    					} catch (IOException e) {
    						e.printStackTrace();
    					}
                	}
                }                
			}
		});
		
		mntmReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainPanel.resetCanvas(false);
				mainPanel.drawCanvas();
			}
		});
		
		/**
		 * Quitter l'appli
		 */
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showOptionDialog(mainUI.getFrame(),
                        "Are You Sure to Close this Application?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
			}
		});
		
		
		/**
		 * About
		 */
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(mainUI.getFrame(), 
						"Version 1.0", 
						"About", 
						JOptionPane.INFORMATION_MESSAGE);

			}
		});
		
		/**
		 * Help
		 */
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(mainUI.getFrame(), 
						"Fonctionnalités :\n"
						+"1. Affichage de la heightmap\n"
						+"2. Affichage des textures disponibles\n"
						+"3. Créer une nouvelle heightmap\n"
						+"4. Modification des textures\n"
						+"5. Ajout des chemins (en rouge)\n"
						+"6. Ajout des objets\n"
						+"7. Affichage/Désaffichage des couches d’objets\n"
						+"8. Exportation\n",
						"Help", 
						JOptionPane.INFORMATION_MESSAGE);

			}
		});

		
		
		/**
		 * Creer une nouvelle carte
		 */
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainPanel.resetCanvas(true);
				mainPanel.drawCanvas();
			}
		});		
		
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int tooltype = mainUI.getToolType();
				if (!isUndo) {
					if (tooltype == Constants.TOPOLOGY)  		mainPanel.undoHeightMap();
					if (tooltype == Constants.TEXTURE)			mainPanel.undoTexture();
					if (tooltype == Constants.ROAD)				mainPanel.undoRoad();
					
					mainPanel.drawCanvas();
					isUndo = true;
				}				
			}
		});
		
		mntmRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int tooltype = mainUI.getToolType();
				if (isUndo) {
					if (tooltype == Constants.TOPOLOGY)		mainPanel.undoHeightMap();
					if (tooltype == Constants.TEXTURE)		mainPanel.undoTexture();
					if (tooltype == Constants.ROAD)			mainPanel.undoRoad();
					
					mainPanel.drawCanvas();
					isUndo = false;
				}
			}
		});
	}
	
	public boolean isUndo() {
		return isUndo;
	}
	
	public void setIsUndo(boolean value) {
		isUndo = value;
	}
}
