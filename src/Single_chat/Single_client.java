package Single_chat;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import TIM.Login_system;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

public class Single_client extends JFrame {

	private static final long serialVersionUID = -3127329443313086527L;
	private JPanel JP1;
	DataInputStream bis;
	DataOutputStream bos;
	Thread readData;
	Socket socket;
	JTextArea friend_message;//消息显示
	JTextArea my_message;//发消息输入
	Read read=null;
	
	
	public Single_client( final String name) {
		this.setTitle(name);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 550, 485);
		this.setVisible(false);
		this.setUndecorated(true);
		
		JP1 = new JPanel();
		JP1.setBackground(new Color(176, 224, 230));
		JP1.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(JP1);
		JP1.setLayout(null);
		
		MouseEventListener mouseListener = new MouseEventListener(this);
		JP1.addMouseListener(mouseListener);
		JP1.addMouseMotionListener(mouseListener);
		
		JLabel font = new JLabel(name);
		font.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		font.setBounds(14, -1, 100, 40);
		JP1.add(font);
		
		JButton jb_close = new JButton(new ImageIcon(Login_system.class.getResource("/TIM/icon_close.png")));
		jb_close.setBounds(496,-1,40,32);
		jb_close.setRolloverIcon(new ImageIcon(Login_system.class.getResource("/TIM/Error_Symbol.png")));
		jb_close.setPressedIcon(new ImageIcon(Login_system.class.getResource("/TIM/icon_close_alt2.png")));
		jb_close.setBorderPainted(false);
		jb_close.setFocusPainted(false);
		jb_close.setContentAreaFilled(false);
		jb_close.setToolTipText("关闭");
		jb_close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}
		});
		JP1.add(jb_close);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 52, 352, 221);
		JP1.add(scrollPane);
		
		friend_message = new JTextArea();
		friend_message.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		friend_message.setEditable(false);
		scrollPane.setViewportView(friend_message);

		JButton message_record = new JButton("聊天记录");
		message_record.setBackground(Color.WHITE);
		message_record.setForeground(SystemColor.textHighlight);
		message_record.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		message_record.setBounds(256, 286, 110, 30);
		JP1.add(message_record);
		message_record.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				File mess=new File(name+".txt");
				try {
					Reader in=new FileReader(mess);
					BufferedReader reader=new BufferedReader(in);
					String str=null;
					while((str=reader.readLine())!=null)
					{
						friend_message.append(str+"\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("文件不存在");
				}
			}
		});
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 329, 352, 100);
		JP1.add(scrollPane_1);
		
		my_message = new JTextArea();
		my_message.setFont(new Font("宋体", Font.PLAIN, 15));
		scrollPane_1.setViewportView(my_message);
				
		
		JButton bclose = new JButton("关闭");
		bclose.setForeground(SystemColor.activeCaptionText);
		bclose.setBackground(SystemColor.text);
		bclose.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		bclose.setBounds(192, 442, 80, 30);
		bclose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				        write_toFile(name);
						if(socket!=null){
							try {
								socket.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						setVisible(false);
				
			
			}
		});
		JP1.add(bclose);
		
		
		JButton bsend = new JButton("发送");
		bsend.setBounds(286, 442, 80, 30);
		bsend.setBackground(SystemColor.text);
		bsend.setForeground(SystemColor.menuText);
		bsend.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		
		bsend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send_message(name);
				String str=friend_message.getText();
				File write=new File(name+".txt");
				try{
					Writer out=new FileWriter(write);
					BufferedWriter bufferWrite =new BufferedWriter(out);
					bufferWrite.write(str);
					bufferWrite.newLine();
					bufferWrite.close();
					out.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		});
		
		JP1.add(bsend);
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setIcon(new ImageIcon(Single_server.class.getResource("speaker.png")));
		lblNewLabel.setBounds(375, 47, 150, 417);
		JP1.add(lblNewLabel);
		
		try {
			socket=new Socket("localhost",9999);	
			read=new Read();
			readData=new Thread(read);
			bis=new DataInputStream(socket.getInputStream());
			bos=new DataOutputStream(socket.getOutputStream());
			read.setDtaInputstream(bis);
			readData.start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println("服务器已断开");
		}
	
		JLabel lblNewLabel2 = new JLabel("");
		lblNewLabel2.setIcon(new ImageIcon(Single_server.class.getResource("/Single_chat/timg[6].jpg")));
		lblNewLabel2.setBounds(0, 0, 550, 485);
		JP1.add(lblNewLabel2);
		
	}
	
	public void send_message(String name){
		
		Date now=new Date();
		String pattern="%tY-%<tm-%<td(%<tA) %<tT";
		String s=String.format(pattern, now);
		
		String txt_send=s+'\n'+my_message.getText();
		try {
			bos.writeUTF(name+"："+txt_send);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		friend_message.setText(friend_message.getText()+'\n'+"本人："+txt_send);
		friend_message.setAlignmentX(Component.RIGHT_ALIGNMENT);
		my_message.setText("");
		my_message.requestFocus();
		
	}
	
	
	public void write_toFile(String name){
	    File message_record =new File(name+".txt");
	    try {
			FileOutputStream fos=new FileOutputStream(message_record);
			DataOutputStream outdata=new DataOutputStream(fos);
			outdata.writeUTF(friend_message.getText());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    //client_thread.interrupt();
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}
	
	public class Read implements Runnable{
		DataInputStream in;

		public void setDtaInputstream(DataInputStream in){
			this.in=in;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			while(true){
				String str;
				try {
					str=bis.readUTF();
					friend_message.setText(friend_message.getText()+'\n'+str+'\n');
				} catch (Exception e) {
					// TODO Auto-generated catch block
				  System.out.println("服务器断开");
					return;
				}
				
		
			}
		}
		
	}
	
	class MouseEventListener implements MouseInputListener {
	     
	    Point origin;
	    Single_client frame;
	     
	    public MouseEventListener(Single_client frame) {
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
