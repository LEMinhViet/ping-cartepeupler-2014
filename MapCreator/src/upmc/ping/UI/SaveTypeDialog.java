package upmc.ping.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class SaveTypeDialog extends JDialog {

	public static final int JME3SDK = 0;
	public static final int JME3SDK_WITHMINIMAP = 1;
	public static final int PING = 2;
	public static final int PING_WITHMINIMAP = 3;
	
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> comboBoxSaveType;
	private JCheckBox chckbxWithMinimap;
	
	private int type = 0;
	private int minimap = 0;
	
	/**
	 * Create the dialog.
	 */
	public SaveTypeDialog() {
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Save as ");
		setResizable(false);
		setBounds(100, 100, 300, 140);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), " Choose your type of export ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			comboBoxSaveType = new JComboBox<String>();
			comboBoxSaveType.addActionListener(new ActionListener() {
				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent arg0) {
					if (((JComboBox<String>)arg0.getSource()).getSelectedIndex() == JME3SDK / 2) {
						type = 0;
					} else {
						type = 1;
					}
				}
			});
			comboBoxSaveType.setModel(new DefaultComboBoxModel<String>(new String[] {"JMonkey3 SDK ", "PING"}));
			comboBoxSaveType.setPreferredSize(new Dimension(260, 20));
			contentPanel.add(comboBoxSaveType);
		}
		{
			chckbxWithMinimap = new JCheckBox("With Minimap");
			chckbxWithMinimap.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if (((JCheckBox)arg0.getSource()).isSelected()) {
						minimap = 1;
					} else {
						minimap = 0;
					}
				}
			});
			chckbxWithMinimap.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(chckbxWithMinimap);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	
	public int showDialog() {
	    setVisible(true);	    
	    return type * 2 + minimap;
	}	

}
