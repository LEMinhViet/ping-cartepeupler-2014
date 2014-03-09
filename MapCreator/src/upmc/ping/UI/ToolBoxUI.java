package upmc.ping.UI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import upmc.ping.app.Constants;

public class ToolBoxUI extends Box {

	private static final long serialVersionUID = 1L;
	
	private MainUI mainUI;
	private MainPanel mainPanel;
	
	private JLabel lblValue;
	private JLabel lblHeight;
	private JLabel lblDensity;
	
	private JSlider sliderDensity;
	private JSlider sliderHeight;
	private JSlider sliderValue;
	
	private int tooltype, tool;
	private int value = 50;
	private int heightWeight = 50;
	private int density = 50;
	
	private ArrayList<JToggleButton> buttons = new ArrayList<JToggleButton>();
	
	public ToolBoxUI(MainUI mainUI) {
		super(BoxLayout.Y_AXIS);		
		this.mainUI = mainUI;
		this.mainPanel = mainUI.getMainPanel();
		
		setBorder(BorderFactory.createTitledBorder(" Tools "));
		
		init();
		eventHandler();
	}

	public void init() {
		Box topologyBox = Box.createHorizontalBox();
		topologyBox.setBorder(BorderFactory.createTitledBorder(" Topologies "));
		add(topologyBox);
		
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
		
		Box TextureBox = Box.createHorizontalBox();
		TextureBox.setBorder(BorderFactory.createTitledBorder(" Textures "));
		add(TextureBox);
		
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
		add(EnvironmentBox);
		
		JToggleButton btnEnvironment1 = new JToggleButton("");
		makeImageButton(btnEnvironment1, "res/img/tree.png", "Environment1");
		EnvironmentBox.add(btnEnvironment1);
		
		Component rigidAreaEnviron1 = Box.createRigidArea(new Dimension(5, 10));
		EnvironmentBox.add(rigidAreaEnviron1);
		
		JToggleButton btnEnvironment2 = new JToggleButton("");
		makeImageButton(btnEnvironment2, "res/img/rock.png", "Environment2");
		EnvironmentBox.add(btnEnvironment2);
		
		Component rigidAreaEnviron2 = Box.createRigidArea(new Dimension(5, 10));
		EnvironmentBox.add(rigidAreaEnviron2);
		
		JToggleButton btnEnvironment3 = new JToggleButton("");
		makeImageButton(btnEnvironment3, "res/img/bulldozer.png", "Environment3");
		EnvironmentBox.add(btnEnvironment3);
		
		Component rigidAreaEnviron3 = Box.createRigidArea(new Dimension(5, 10));
		EnvironmentBox.add(rigidAreaEnviron3);
		
		JToggleButton btnEnvironment4 = new JToggleButton("");
		makeImageButton(btnEnvironment4, "res/img/saw.png", "Environment4");
		EnvironmentBox.add(btnEnvironment4);
		
		Component rigidAreaEnviron4 = Box.createRigidArea(new Dimension(5, 10));
		EnvironmentBox.add(rigidAreaEnviron4);
		
		JToggleButton btnEnvironment5 = new JToggleButton("");
		makeImageButton(btnEnvironment5, "res/img/axe.png", "Environment5");
		EnvironmentBox.add(btnEnvironment5);
		
		Component rigidAreaEnvironEye = Box.createRigidArea(new Dimension(20, 10));
		EnvironmentBox.add(rigidAreaEnvironEye);
		
		JToggleButton btnEnvironEye = new JToggleButton("");
		makeImageButton(btnEnvironEye, "res/img/show.png", "EnvironEye");
		EnvironmentBox.add(btnEnvironEye);
		
		Box RoadBox = Box.createHorizontalBox();
		RoadBox.setBorder(BorderFactory.createTitledBorder(" Road "));
		add(RoadBox);
		
		Component roadRigidArea_left = Box.createRigidArea(new Dimension(20, 10));
		RoadBox.add(roadRigidArea_left);
		
		JToggleButton btnRoad = new JToggleButton("");
		makeImageButton(btnRoad, "res/img/road.jpg", "Road");
		RoadBox.add(btnRoad);
		
		Component roadRigidArea_right = Box.createRigidArea(new Dimension(20, 10));
		RoadBox.add(roadRigidArea_right);
		
		Component rigidAreaRoadEye = Box.createRigidArea(new Dimension(20, 10));
		RoadBox.add(rigidAreaRoadEye);
		
		JToggleButton btnRoadEye = new JToggleButton("");
		makeImageButton(btnRoadEye, "res/img/show.png", "RoadEye");
		RoadBox.add(btnRoadEye);
		
		Box DecorBox = Box.createHorizontalBox();
		DecorBox.setBorder(new TitledBorder(null, " Transport and Decoration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(DecorBox);
		
		JToggleButton btnTrans1 = new JToggleButton("");
		makeImageButton(btnTrans1, "res/img/boat.png", "Trans1");
		DecorBox.add(btnTrans1);
		
		Component rigidAreaTrans1 = Box.createRigidArea(new Dimension(10, 10));
		DecorBox.add(rigidAreaTrans1);
		
		JToggleButton btnTrans2 = new JToggleButton("");
		makeImageButton(btnTrans2, "res/img/horse.png", "Trans2");
		DecorBox.add(btnTrans2);
		
		Component rigidAreaTrans2 = Box.createRigidArea(new Dimension(10, 10));
		DecorBox.add(rigidAreaTrans2);
		
		JToggleButton btnTrans3 = new JToggleButton("");
		makeImageButton(btnTrans3, "res/img/teleport.png", "Trans3");
		DecorBox.add(btnTrans3);
		
		Component rigidAreaTrans3 = Box.createRigidArea(new Dimension(10, 10));
		DecorBox.add(rigidAreaTrans3);
		
		JToggleButton btnTrans4 = new JToggleButton("");
		makeImageButton(btnTrans4, "res/img/house.png", "Trans4");
		DecorBox.add(btnTrans4);
		
		Component rigidAreaTrans4 = Box.createRigidArea(new Dimension(20, 10));
		DecorBox.add(rigidAreaTrans4);
		
		JToggleButton btnTransEye = new JToggleButton("");
		makeImageButton(btnTransEye, "res/img/show.png", "TransEye");
		DecorBox.add(btnTransEye);
		
		Component verticalStrut = Box.createVerticalStrut(50);
		add(verticalStrut);
		
		Box additionToolsBox = Box.createVerticalBox();
		additionToolsBox.setBorder(new TitledBorder(null, " Addition Tools ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(additionToolsBox);
		
		Box BrushSizeBox = Box.createHorizontalBox();
		BrushSizeBox.setBorder(new TitledBorder(null, " Brush Size ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		additionToolsBox.add(BrushSizeBox);
		
		sliderValue = new JSlider();
		sliderValue.setMinimum(1);
		sliderValue.setSnapToTicks(true);
		sliderValue.setPreferredSize(new Dimension(80, 20));
		sliderValue.setPaintTicks(true);
		sliderValue.setMinorTickSpacing(5);
		BrushSizeBox.add(sliderValue);
		
		lblValue = new JLabel("50");
		lblValue.setFont(new Font("Tahoma", Font.PLAIN, 12));
		BrushSizeBox.add(lblValue);
		
		Box HeightBox = Box.createHorizontalBox();
		HeightBox.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), " Height / Weight (Topologies)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		additionToolsBox.add(HeightBox);

		sliderHeight = new JSlider();
		sliderHeight.setMinimum(1);
		sliderHeight.setSnapToTicks(true);
		sliderHeight.setPreferredSize(new Dimension(80, 20));
		sliderHeight.setPaintTicks(true);
		sliderHeight.setMinorTickSpacing(5);
		HeightBox.add(sliderHeight);
		
		lblHeight = new JLabel("50");
		lblHeight.setFont(new Font("Tahoma", Font.PLAIN, 12));
		HeightBox.add(lblHeight);
		
		Box DensityBox = Box.createHorizontalBox();
		DensityBox.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), " Density (Rock & Tree)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		additionToolsBox.add(DensityBox);
		
		sliderDensity = new JSlider();
		sliderDensity.setMinimum(1);
		sliderDensity.setSnapToTicks(true);
		sliderDensity.setPreferredSize(new Dimension(80, 20));
		sliderDensity.setPaintTicks(true);
		sliderDensity.setMinorTickSpacing(5);
		DensityBox.add(sliderDensity);
		
		lblDensity = new JLabel("50");
		lblDensity.setFont(new Font("Tahoma", Font.PLAIN, 12));
		DensityBox.add(lblDensity);
	}
	
	public void eventHandler() {
		sliderDensity.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				density = ((JSlider)arg0.getSource()).getValue();
				lblDensity.setText(density + "");
			}
		});
		
		sliderValue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				value = ((JSlider)arg0.getSource()).getValue();
				lblValue.setText((int)value + "");
			}
		});
		
		sliderHeight.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				heightWeight = ((JSlider)arg0.getSource()).getValue();
				lblHeight.setText(heightWeight + "");
			}
		});
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
		// Il y a seulement 1 bouton (outil) selectionné chaque fois
		if (!button.getName().contains("Eye")) {
			button.addActionListener(new ActionListener() {			 
	            public void actionPerformed(ActionEvent e)
	            {
	            	updateSelectedButton((JToggleButton)e.getSource());
	            	String btnName = ((JToggleButton)e.getSource()).getName();
	            	getTool(btnName);
	            }
	        });
		// Avec les boutons pour Affichage/Désaffichage des couches
		} else {
			button.addActionListener(new ActionListener() {			 
	            public void actionPerformed(ActionEvent e)
	            {	            	
	            	String btnName = ((JToggleButton)e.getSource()).getName();
	            	// Changer les variables pour affichage/deaffichage des couches
	            	setShowHideLayer(btnName);
	            	// Re-peintre l'ecran
	            	mainPanel.drawCanvas();
	            }
	        });
		}
		
		buttons.add(button);
	}
	
	/**
	 * Quand l'utilisateur selectionne un bouton, les autres boutons doivent etre déactivés 
	 */
	public void updateSelectedButton(JToggleButton button) {
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i) != button) {
				buttons.get(i).setSelected(false);
			} else {
				buttons.get(i).setSelected(true);
			}
		}
	}
	
	/**
	 * Retirer le type d'outil et l'outil a partir du nom du bouton
	 * @param name
	 */
	public void getTool(String name) {
		if (name.equals("Topo1")) {
			tooltype = Constants.TOPOLOGY;
			tool = 1;
		} else if (name.equals("Topo2")) {
			tooltype = Constants.TOPOLOGY;
			tool = 2;
		} else if (name.equals("Texture1")) {
			tooltype = Constants.TEXTURE;
			tool = 1;
		} else if (name.equals("Texture2")) {
			tooltype = Constants.TEXTURE;
			tool = 2;
		} else if (name.equals("Texture3")) {
			tooltype = Constants.TEXTURE;
			tool = 3;
		} else if (name.equals("Texture4")) {
			tooltype = Constants.TEXTURE;
			tool = 4;
		} else if (name.equals("Environment1")) {
			tooltype = Constants.ENVIRONMENT;
			tool = 1;
		} else if (name.equals("Environment2")) {
			tooltype = Constants.ENVIRONMENT;
			tool = 2;
		} else if (name.equals("Environment3")) {
			tooltype = Constants.ENVIRONMENT;
			tool = 3;
		} else if (name.equals("Environment4")) {
			tooltype = Constants.ENVIRONMENT;
			tool = 4;
		} else if (name.equals("Environment5")) {
			tooltype = Constants.ENVIRONMENT;
			tool = 5;
		} else if (name.equals("Road")) {
			tooltype = Constants.ROAD;
			tool = 1;
		} else if (name.equals("Trans1")) {
			tooltype = Constants.TRANSPORT;
			tool = 1;
		} else if (name.equals("Trans2")) {
			tooltype = Constants.TRANSPORT;
			tool = 2;
		} else if (name.equals("Trans3")) {
			tooltype = Constants.TRANSPORT;
			tool = 3;
		} else if (name.equals("Trans4")) {
			tooltype = Constants.TRANSPORT;
			tool = 4;
		}
	}
	
	/**
	 * Affichage/Désaffichage des couches
	 * @param name
	 */
	public void setShowHideLayer(String name) {
		if (name.equals("EnvironEye")) {
			mainUI.inverseIsShowEnvironment();
		} else if (name.equals("RoadEye")) {
			mainUI.inverseIsShowRoad();
		} else if (name.equals("TransEye")) {
			mainUI.inverseIsShowTransport();
		}
	}
	
	public int getTool() {
		return tool;
	}
	
	public int getToolType() {
		return tooltype;
	}
	
	public int getDensity() {
		return density;
	}
	
	public int getHeightWeight() {
		return heightWeight;
	}
	
	public int getValue() {
		return value;
	}
}
