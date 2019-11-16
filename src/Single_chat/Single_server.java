package Single_chat;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import TIM.Login_system;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class Single_server extends JFrame {

	private JPanel contentPane;
	static DataInputStream bis;
	static DataOutputStream bos;
	static Socket you;
	ServerThread readData;
	static JTextArea friend_message ;
	JTextArea my_message ;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Single_server frame = new Single_server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Single_server() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(450, 200, 550, 485);
		this.setVisible(false);
		this.setUndecorated(true);
		
		getContentPane().setLayout(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setBounds(0, 0, 395, 418);
		contentPane.setLayout(null);
		
		MouseEventListener mouseListener = new MouseEventListener(this);
		contentPane.addMouseListener(mouseListener);
		contentPane.addMouseMotionListener(mouseListener);
			
		JButton jb_close = new JButton(new ImageIcon(Login_system.class.getResource("/TIM/icon_close.png")));
		jb_close.setBounds(496,2,40,32);
		jb_close.setRolloverIcon(new ImageIcon(Login_system.class.getResource("/TIM/Error_Symbol.png")));
		jb_close.setPressedIcon(new ImageIcon(Login_system.class.getResource("/TIM/icon_close_alt2.png")));
		jb_close.setBorderPainted(false);
		jb_close.setFocusPainted(false);
		jb_close.setContentAreaFilled(false);
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 52, 352, 221);
		contentPane.add(scrollPane);
		
		 friend_message = new JTextArea();
		 friend_message.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		friend_message.setEditable(false);
		scrollPane.setViewportView(friend_message);
		
		
		JLabel font = new JLabel(" ×ÖºÅ");
		font.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		font.setBounds(14, 286, 50, 30);
		contentPane.add(font);
	
		
		final JComboBox select_font = new JComboBox();
		select_font.setBackground(SystemColor.text);
		select_font.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		select_font.setBounds(60, 287, 50, 30);
		contentPane.add(select_font);
		
		select_font.addItem("14");
		select_font.addItem("16");
		select_font.addItem("18");
		select_font.addItem("20");
		select_font.addItem("22");
		select_font.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(select_font.getSelectedItem().toString().equals("14"))
				{
					Font f=new Font(null, ABORT, 14);
					friend_message.setFont(f);
				}
				if(select_font.getSelectedItem().toString().equals("16"))
				{
					Font f=new Font(null, ABORT, 16);
					friend_message.setFont(f);
				}
				if(select_font.getSelectedItem().toString().equals("18"))
				{
					Font f=new Font(null, ABORT, 18);
					friend_message.setFont(f);
				}
				if(select_font.getSelectedItem().toString().equals("20"))
				{
					Font f=new Font(null, ABORT, 20);
					friend_message.setFont(f);
				}
				if(select_font.getSelectedItem().toString().equals("22"))
				{
					Font f=new Font(null, ABORT, 22);
					friend_message.setFont(f);
				}
			}
		});
		
		JLabel color = new JLabel(" ÑÕÉ«");
		color.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		color.setBounds(124, 286, 50, 30);
		
		contentPane.add(color);
		
		final JComboBox select_color = new JComboBox();
		select_color.setBackground(SystemColor.text);
		select_color.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		select_color.setBounds(174, 287, 70, 30);
		select_color.addItem("ºÚÉ«");
		select_color.addItem("ºìÉ«");
		select_color.addItem("À¶É«");
		select_color.addItem("»ÆÉ«");
		contentPane.add(select_color);
      
		select_color.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(select_color.getSelectedItem().toString().equals("ºìÉ«"))
				{
					friend_message.setForeground(Color.RED);
				}
				if(select_color.getSelectedItem().toString().equals("ºÚÉ«"))
				{
					friend_message.setForeground(Color.BLACK);
				}
					if(select_color.getSelectedItem().toString().equals("À¶É«"))
					{
						friend_message.setForeground(Color.BLUE);
					}
					if(select_color.getSelectedItem().toString().equals("»ÆÉ«"))
					{
						friend_message.setForeground(Color.YELLOW);
					}
				}
		});
		
		JButton message_record = new JButton("ÁÄÌì¼ÇÂ¼");
		message_record.setBackground(Color.WHITE);
		message_record.setForeground(SystemColor.textHighlight);
		message_record.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		message_record.setBounds(256, 286, 110, 30);
		contentPane.add(message_record);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 329, 352, 100);
		contentPane.add(scrollPane_1);
		
		my_message = new JTextArea();
		my_message.setFont(new Font("ËÎÌå", Font.PLAIN, 15));
		scrollPane_1.setViewportView(my_message);
		
		
		
		JButton bclose = new JButton("¹Ø±Õ");
		bclose.setForeground(SystemColor.activeCaptionText);
		bclose.setBackground(SystemColor.text);
		bclose.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		bclose.setBounds(192, 442, 80, 30);
		bclose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					you.close();
					bis.close();
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(bclose);
		
		JButton bsend = new JButton("·¢ËÍ");
		bsend.setBackground(SystemColor.text);
		bsend.setForeground(SystemColor.menuText);
		bsend.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		bsend.setBounds(286, 442, 80, 30);
		bsend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send_message();
			}
		});
		contentPane.add(bsend);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(Single_server.class.getResource("speaker.png")));
		lblNewLabel_1.setBounds(375, 47, 150, 417);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Single_server.class.getResource("/TIM/hua.jpg")));
		lblNewLabel.setBounds(0, 0, 550, 485);
		contentPane.add(lblNewLabel);
		
		try
		{
			ServerSocket server =new ServerSocket(9999);
			System.out.println("µÈ´ýÁ¬½Ó");
			you=server.accept();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if(you!=null)
		{
			readData =new ServerThread(you);
			readData.start();
		}
	}
	
	public void send_message(){
		
		Date now=new Date();
		String pattern="%tY-%<tm-%<td(%<tA) %<tT";
		String s=String.format(pattern, now);
		String txt_send=s+'\n'+my_message.getText();
		try
		{
			bos.writeUTF("ÅóÓÑ£º"+txt_send);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//bos.flush();
		friend_message.setText(friend_message.getText()+'\n'+"±¾ÈË£º"+txt_send+'\n');
		my_message.setText("");
		my_message.requestFocus();
		
	}

	class ServerThread extends Thread{
	Socket socket;
	
	public ServerThread(Socket t)
	{
		socket=t;
	}
	@Override
	public void run()
	{
		while(true)
		{
			try 
			{
				bis=new DataInputStream(socket.getInputStream());
				bos=new DataOutputStream(socket.getOutputStream());
			    String  str;
			    str=bis.readUTF();
				friend_message.setText(friend_message.getText()+'\n'+str+'\n');
			} 
			catch (IOException e)
			{
				System.out.println("¿Í»§¶ËÀë¿ª");
				return;
			}		
			}
		}
	
	}
	
	class MouseEventListener implements MouseInputListener {
	     
	    Point origin;
	    Single_server frame;
	     
	    public MouseEventListener(Single_server frame) {
	      this.frame = frame;
	      origin = new Point();
	    }
	     
	    @Override
	    public void mouseClicked(MouseEvent e) {}
	 
	    @Override
	    public void mousePressed(MouseEvent e) {
	      origin.x = e.getX(); 
	      origin.y = e.getY();
	    }
	 
	    @Override
	    public void mouseReleased(MouseEvent e) {}
	 
	    @Override
	    public void mouseEntered(MouseEvent e) {
	      this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	    }
	     
	    @Override
	    public void mouseExited(MouseEvent e) {
	      this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    }
	 
	    @Override
	    public void mouseDragged(MouseEvent e) {
	      Point p = this.frame.getLocation();
	      this.frame.setLocation(
	        p.x + (e.getX() - origin.x), 
	        p.y + (e.getY() - origin.y)); 
	    }
	 
	    @Override
	    public void mouseMoved(MouseEvent e) {}
 }     
}



