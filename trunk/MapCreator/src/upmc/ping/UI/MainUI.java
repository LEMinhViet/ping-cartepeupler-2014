package upmc.ping.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainUI {
	
	// QUEQUELS COMPOSENTS 
	private JFrame frmMapEditor;
	private JPanel previewPanel;
	private JLabel previewLabel;
	private JScrollPane scrollPane;
	
	private MainPanel mainPanel;
	private MenuUI menuBar;
	private ToolBoxUI toolsBox;
	
	private boolean isShowEnvironment = true;
	private boolean isShowRoad = true;
	private boolean isShowTransport = true;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try { 
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); 
	    } catch(Exception e) { }
		
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
		frmMapEditor.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmMapEditor.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		/* FIRST TAB - MAP DESIGN */ 
		JPanel panelDesign = new JPanel();
		tabbedPane.addTab("Map Design", null, panelDesign, null);
		panelDesign.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(new Rectangle(0, 0, 200, 200));
		
		mainPanel = new MainPanel(new GridBagLayout(), this);
		scrollPane.setViewportView(mainPanel);
		panelDesign.add(scrollPane, BorderLayout.CENTER);
		
		/* MENU */
		menuBar = new MenuUI(this);
		frmMapEditor.setJMenuBar(menuBar);
		
		/* TOOL BOX */
		toolsBox = new ToolBoxUI(this);
		panelDesign.add(toolsBox, BorderLayout.EAST);
		
		/* SECOND TAB - HEIGHT MAP PREVIEW */ 
		JScrollPane previewPane = new JScrollPane();
		previewPane.setBounds(new Rectangle(0, 0, 200, 200));
		tabbedPane.addTab("Height Map Preview", null, previewPane, null);
		
		previewPanel = new JPanel(new GridBagLayout());
		previewPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		previewPane.setViewportView(previewPanel);
		
		previewLabel = new JLabel(new ImageIcon(mainPanel.getSrcHeightMap()));
		previewPanel.add(previewLabel, null);
		
		mainPanel.drawCanvas();
		
		/************************************************************************************************************/
		/* EVENT HANDLER */	
		
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int tabIndex = ((JTabbedPane)arg0.getSource()).getSelectedIndex();
				if (tabIndex == 1) {
					previewLabel.setIcon(new ImageIcon(mainPanel.getSrcHeightMap()));
					mainPanel.revalidate();
					frmMapEditor.getContentPane().repaint();
				}
			}
		});
		
		/* CHANGE CURSOR */
		/*Toolkit toolkit = Toolkit.getDefaultToolkit();
		BufferedImage mouseBuf = ImageIO.read(new File("res/mouse.png"));
		Cursor c = toolkit.createCustomCursor(mouseBuf , new Point(mainPanel.getX() + mouseBuf.getWidth(), 
																	mainPanel.getY() + mouseBuf.getHeight()), "img");
		mainPanel.setCursor (c);*/
	}
	
	
	/**
	 * Retirer les couleurs pour les niveaux de l'hauteur 
	 * @param tool 
	 * @return
	 */
	public Paint getTopo() {
		int colorin = (int)(0.2 * toolsBox.getHeightWeight());
		int colorout = (int)(0.02 * toolsBox.getHeightWeight());
		Paint p = new RadialGradientPaint(new Point2D.Double(toolsBox.getValue() / 2,
				toolsBox.getValue() / 2), (float) (toolsBox.getValue() / 2),
	                new Point2D.Double(toolsBox.getValue() / 2, toolsBox.getValue() / 2),
	                new float[] { 0.0f, 0.95f },
	                new Color[] { new Color(colorin, colorin, colorin),
	                    new Color(colorout, colorout, colorout) },
	                RadialGradientPaint.CycleMethod.NO_CYCLE);
		return p;
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
	
	public MainPanel getMainPanel() {
		return mainPanel;
	}
	
	public JFrame getFrame() {
		return frmMapEditor;
	}
	
	public int getTool() {
		return toolsBox.getTool();
	}
	
	public int getToolType() {
		return toolsBox.getToolType();
	}
	
	public int getDensity() {
		return toolsBox.getDensity();
	}
	
	public int getValue() {
		return toolsBox.getValue();
	}
	
	public boolean isShowEnvironment() {
		return isShowEnvironment;
	}
	
	public boolean isShowRoad() {
		return isShowRoad;
	}
	
	public boolean isShowTransport() {
		return isShowTransport;
	}	
	
	public void inverseIsShowEnvironment() {
		isShowEnvironment = !isShowEnvironment;
	}
	
	public void inverseIsShowRoad() {
		isShowRoad = !isShowRoad;
	}
	
	public void inverseIsShowTransport() {
		isShowTransport = !isShowTransport;
	}
	
	public void setIsUndo(boolean value) {
		menuBar.setIsUndo(value);
	}
	
	public boolean isUndo() {
		return menuBar.isUndo();
	}
	
	public void repaint() {
		frmMapEditor.getContentPane().repaint();
	}
}
