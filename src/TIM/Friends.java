package TIM;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import More_chat.More_client;
import More_chat.More_server;
import More_chat.User;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.DefaultComboBoxModel;

public class Friends extends JFrame {

	private static final long serialVersionUID = 7233885642386332121L;
	private JPanel contentPane;
	private JFrame frame;
	static Point origin = new Point();
	
	JPanel panel ;
	JPanel panel_1 ;
	JButton button ;
	JComboBox choiceBox;
	User user1=new User("Lin","127.0.0.1");
	User user2=new User("Blue","127.0.0.2");
	User user3=new User("Red","127.0.0.3");
	User user4=new User("Green","127.0.0.4");
	User user5=new User("Yellow","127.0.0.5");
	User user6=new User("Black","127.0.0.6");
	User user7=new User("Orange","127.0.0.7");
	User users[] = {user1,user2,user3,user4,user5,user6,user7};
	String image[]={"Lin.jpg","Blue.jpg","Red.jpg","Green.jpg","Yellow.jpg","Black.jpg","Orange.jpg"};
	
	J_listener jcomboxListener;//¼àÊÓItemEventÊÂ¼þµÄ¼àÌýÆ÷
	Mouse_tip mouse ;//ÁÄÌìÊÂ¼þ¼àÌýÆ÷

	public Friends(final String str) {
		
		frame = new JFrame();  
        //frame.setVisible(false);
        frame.setUndecorated(true);
        
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 330, 650);
		this.setVisible(false);
		this.setUndecorated(true);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(176,224,230));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel font = new JLabel(str);
		font.setForeground(SystemColor.text);
		font.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 18));
		font.setBounds(10, 0, 100, 40);
		contentPane.add(font);
		
		MouseEventListener mouseListener = new MouseEventListener(this);
		contentPane.addMouseListener(mouseListener);
		contentPane.addMouseMotionListener(mouseListener);
		
		JButton jb_close = new JButton(new ImageIcon(Login_system.class.getResource("/TIM/close1.png")));
		jb_close.setBounds(271,0,59,34);
		jb_close.setRolloverIcon(new ImageIcon(Login_system.class.getResource("/TIM/close2.png")));
		jb_close.setPressedIcon(new ImageIcon(Login_system.class.getResource("/TIM/close3.png")));
		jb_close.setBorderPainted(false);
		jb_close.setFocusPainted(false);
		jb_close.setToolTipText("¹Ø±Õ");
		jb_close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}
		});
		contentPane.add(jb_close);
		
		
		//ÓÃ»§Í·Ïñ
		panel = new JPanel();
		panel.setOpaque(false);
		panel.setBounds(0, 35, 330, 100);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton bt1 = new JButton();
		ImageIcon icon=new ImageIcon(str+".jpg");
		icon.setImage(icon.getImage().getScaledInstance(100, 100,Image.SCALE_DEFAULT));
		bt1.setIcon(icon);
		bt1.setBounds(10, 10, 85, 85);
		panel.add(bt1);
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(More_server.class.getResource(str+".jpg")));
		
		JLabel lblNewLabel = new JLabel("êÇ³Æ:"+str);
		lblNewLabel.setFont(new Font("Î¢ÈíÑÅºÚ",0,17));
		lblNewLabel.setBounds(143, 10, 111, 28);
		panel.add(lblNewLabel);
	
		JLabel lblNewLabel_1 = new JLabel("×´Ì¬£ºÔÚÏß");
		lblNewLabel_1.setFont(new Font("Î¢ÈíÑÅºÚ",0,17));
		lblNewLabel_1.setBounds(143, 51, 95, 33);
		panel.add(lblNewLabel_1);
		
		//ºÃÓÑÃæ°å
		panel_1 = new JPanel();
		panel_1.setOpaque(false);
		panel_1.setBounds(0, 186, 330, 401);
		FlowLayout flow = new FlowLayout();
		flow.setVgap(1);
		panel_1.setLayout(flow);
		
		contentPane.add(panel_1);
		
		//ºÃÓÑÁÐ±í
		for(int i=0;i<users.length;i++)
		{
			String name=users[i].getName();
			String uri=image[i];
			if(!name.equals(str))
			{	
				JPanel panel_2 = new JPanel();
				//panel_2.setBackground(new Color(255,228,225));
				panel_2.setPreferredSize(new Dimension(300, 60));
				panel_2.setLayout(null);
				ImageIcon ico1=new ImageIcon(uri);
				ico1.setImage(ico1.getImage().getScaledInstance(70, 70,Image.SCALE_DEFAULT));
				JButton bt2 = new JButton(name,ico1);
				bt2.setBounds(0, 0, 70, 70);
				panel_2.add(bt2);
				JLabel lblNewLabel_2 = new JLabel("êÇ³Æ£º"+name);
				lblNewLabel_2.setFont(new Font("Î¢ÈíÑÅºÚ",0,17));
				lblNewLabel_2.setBounds(101, 0, 111, 22);	
				panel_2.add(lblNewLabel_2);
				mouse= new Mouse_tip();
				mouse.setJanel(panel_2);
			    mouse.setName(name);
				bt2.addMouseListener(mouse);
				panel_1.add(panel_2);		
			}
		}
		
	    button = new JButton("ÈºÁÄ");
	    button.setBackground(Color.WHITE);
	    button.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		More_client c=new More_client(str);
	    		//c.setVisible(true);
	    	}
	    });
	    button.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 18));
	    //button.setBackground(new Color(255, 218, 185));
	    button.setForeground(Color.RED);
	    button.setBounds(220, 138, 110, 47);
	    contentPane.add(button);
		//ÉèÖÃÑ¡Ôñ¿ò
		choiceBox = new JComboBox();
		choiceBox.setModel(new DefaultComboBoxModel(new String[] {"¹¦ÄÜ"}));
		choiceBox.addItem("Ìí¼ÓºÃÓÑ");
		choiceBox.addItem("ÉèÖÃ");
		choiceBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 17));
		choiceBox.setBounds(208, 600, 100, 37);
		contentPane.add(choiceBox);
		jcomboxListener = new J_listener();
		jcomboxListener .setPanel(panel_1);
		jcomboxListener.setChoiceBox(choiceBox);
	    choiceBox.addItemListener(jcomboxListener);
		
	    JLabel bgLabel = new JLabel(new ImageIcon("hua.jpg"));  
		bgLabel.setBounds(0, 0, this.getWidth(), this.getHeight());  
		JPanel bgPanel = (JPanel) this.getContentPane();  
		bgPanel.setOpaque(false);
      
		JButton btnNewButton = new JButton("ÏûÏ¢");
		btnNewButton.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 18));
		btnNewButton.setBounds(0, 138, 110, 47);
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setBorderPainted(false);
		contentPane.add(btnNewButton);
      
		JButton btnNewButton_1 = new JButton("ÁªÏµÈË");
		btnNewButton_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 18));
		btnNewButton_1.setBounds(110, 138, 110, 47);
		btnNewButton_1.setBackground(Color.WHITE);
		btnNewButton_1.setBorderPainted(false);
		contentPane.add(btnNewButton_1);
		getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));	

	}
	
	 class MouseEventListener implements MouseInputListener {
	     
		    Point origin;
		    Friends frame;
		     
		    public MouseEventListener(Friends frame)
		    {
		      this.frame = frame;
		      origin = new Point();
		    }
		    @Override
		    public void mouseClicked(MouseEvent e) {}
		    @Override
		    public void mousePressed(MouseEvent e)
		    {
		      origin.x = e.getX(); 
		      origin.y = e.getY();
		    }
		    @Override
		    public void mouseReleased(MouseEvent e) {}
		    @Override
		    public void mouseEntered(MouseEvent e)
		    {
		      this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		    }
		    @Override
		    public void mouseExited(MouseEvent e)
		    {
		      this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		    }
		    @Override
		    public void mouseDragged(MouseEvent e)
		    {
		      Point p = this.frame.getLocation();
		      this.frame.setLocation(
		        p.x + (e.getX() - origin.x), 
		        p.y + (e.getY() - origin.y)); 
		    }
		    @Override
		    public void mouseMoved(MouseEvent e) {}
	 }     
		  
}


