package TIM;

import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import More_chat.More_client;
import Single_chat.Single_client;

public class Mouse_tip implements MouseListener{
		JPanel janel;
		String name;

		public void setJanel(JPanel janel) {
			this.janel = janel;
		}
		public void setName(String name) {
			this.name = name;
		}


		@Override
        
	public void mouseClicked(final MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getClickCount()>=2){
			janel.setBackground(new Color(255, 0, 0));
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Single_client frame = new Single_client(name);
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		janel.setBackground(new Color(255, 235, 245));
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		janel.setBackground(new Color(250, 250, 229));
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		janel.setBackground(new Color(255, 235, 245));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
