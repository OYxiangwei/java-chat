package TIM;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class J_listener implements ItemListener{
     JComboBox choiceBox;
     JPanel panel ;
     JLabel lable;
     public void setPanel(JPanel panel)
     {
 		this.panel = panel;
 		Component[] elements = panel.getComponents();
 	 }
	public void setChoiceBox(JComboBox choiceBox)
	{
		this.choiceBox = choiceBox;
	}
	
	@Override
	public void itemStateChanged(ItemEvent arg0)
	{
		int choice = choiceBox.getSelectedIndex();
		if(choice==1)
		{
			String name = JOptionPane.showInputDialog(null," ‰»Î∫√”—Í«≥∆","ÃÌº”∫√”—",
			JOptionPane.PLAIN_MESSAGE);
			
			if(name!=null)
			{
				ImageIcon ico1=new ImageIcon("ww.jpg");
				ico1.setImage(ico1.getImage().getScaledInstance(60, 50,Image.SCALE_DEFAULT));
				JButton btnNewButton_1 = new JButton(name,ico1);
				btnNewButton_1.setBounds(0, 0, 50, 50);
				JPanel panel_2 = new JPanel();
				panel_2.setBackground(new Color(250, 250, 229));
				panel_2.setPreferredSize(new Dimension(224, 50));
				panel_2.setLayout(null);
				panel_2.add(btnNewButton_1);
				JLabel lblNewLabel_2 = new JLabel("Í«≥∆£∫"+name);
				lblNewLabel_2.setBounds(101, 0, 111, 22);	
				panel_2.add(lblNewLabel_2);
				Mouse_tip mouse= new Mouse_tip();
				mouse.setJanel(panel_2);
			    mouse.setName(name);
				btnNewButton_1.addMouseListener(mouse);
				panel.add(panel_2);
				choiceBox.setSelectedIndex(0);
			}
		}		
	}
}


