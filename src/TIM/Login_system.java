package TIM;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;

import Single_chat.Single_server;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.awt.SystemColor;
import java.awt.Toolkit;

public class Login_system extends JFrame {

	private static final long serialVersionUID = -8811070874941691147L;
	private JTextField textField;
	private JTextField textField_1;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Login_system frame = new Login_system();
					frame.setVisible(true);
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public Login_system() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(540, 420);
		int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width; //获取屏幕宽度 
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;  //获取屏幕高度
        this.setLocation((screen_width - this.getWidth()) / 2,(screen_height - this.getHeight()) / 2);
		this.setVisible(false);
		this.setUndecorated(true);
		
		BorderLayout bl = new BorderLayout();  
		getContentPane().setLayout(bl);//设置窗体的布局为边框布局  
	      
	    /*********北边的面板容器*************/  
	    JPanel JP1 = creatPanelNorth();  
	    getContentPane().add(JP1,BorderLayout.CENTER);  
	      
	    /*********中间的面板容器*************/  
	    JPanel JP2 = creatPanelCenter();  
	    getContentPane().add(JP2, BorderLayout.CENTER);  
	  
	    /*********南边的面板容器*************/   
	    JPanel JP3 = creatPanelSouth();  
	    getContentPane().add(JP3, BorderLayout.CENTER); 
	}
	
	private JPanel creatPanelNorth() {
	
		JPanel JP1 = new JPanel();
		JP1.setBounds(0,0,540,200);
		JP1.setLayout(null);
		//鼠标拖动窗口
		MouseEventListener mouseListener = new MouseEventListener(this);
		JP1.addMouseListener(mouseListener);
		JP1.addMouseMotionListener(mouseListener);
		//关闭窗口
		JButton jb_close = new JButton(new ImageIcon(Login_system.class.getResource("/TIM/icon_close.png")));
		jb_close.setBounds(487,2,40,32);
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
		
		//背景
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(Login_system.class.getResource("/TIM/bj.gif")));
		lblNewLabel.setBounds(0, 0, 540, 200);
		JP1.add(lblNewLabel);
		
		return JP1;
		}
	
	private JPanel creatPanelCenter() {
		
		JPanel JP2 = new JPanel();
		JP2.setBackground(new Color(255,255,255));
		JP2.setBounds(0, 200, 540, 150);
		JP2.setLayout(null);
		//头像
		JLabel lblNewLabel = new JLabel();
		int width=93;
		int height=93;
		ImageIcon image = new ImageIcon(Single_server.class.getResource("/TIM/Red.jpg"));
		image.setImage(image.getImage().getScaledInstance(width, height,Image.SCALE_DEFAULT ));
		lblNewLabel.setIcon(image);
		lblNewLabel.setLocation(25, 13);
		lblNewLabel.setSize(width, height);
		JP2.add(lblNewLabel);
		//用户名
		textField = new JTextField();
		textField.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		textField.setBounds(140, 13, 253, 40);
		JP2.add(textField);
		textField.setColumns(10);
		//密码
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		textField_1.setBounds(140, 69, 253, 40);
		textField_1.setColumns(10);	
		JP2.add(textField_1);
		
		JButton bt3 = new JButton("忘记密码");
		bt3.setForeground(SystemColor.textHighlight);
		bt3.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		bt3.setBackground(Color.WHITE);
		bt3.setBounds(407, 74, 95, 28);
		bt3.setBorderPainted(false);
		JP2.add(bt3);
		
		JButton consgistor = new JButton("注册账号");
		consgistor.setForeground(SystemColor.textHighlight);
		consgistor.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		consgistor.setBackground(Color.WHITE);
		consgistor.setBounds(407, 20, 95, 28);
		consgistor.setBorderPainted(false);
		consgistor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				
			}
		});
		JP2.add(consgistor);
		
		JCheckBox checkBox = new JCheckBox("记住密码");
		checkBox.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		checkBox.setBackground(SystemColor.window);
		checkBox.setBounds(140, 118, 91, 27);
		JP2.add(checkBox);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("自动登录");
		chckbxNewCheckBox.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		chckbxNewCheckBox.setBackground(SystemColor.window);
		chckbxNewCheckBox.setBounds(306, 118, 91, 27);
		JP2.add(chckbxNewCheckBox);
		
		JComboBox<String> status = new JComboBox<String>();
		status.setBounds(59, 119, 59, 25);
		JP2.add(status);
		status.setBackground(SystemColor.window);
		status.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		status.setBorder(null);
		status.addItem("状态");
		status.addItem("Q我");
		status.addItem("在线");
		status.addItem("隐身");
		status.addItem("离线");
		return JP2;
		}
	
	private JPanel creatPanelSouth() {
		
		JPanel JP3 = new JPanel();
		JP3.setBackground(new Color(255,255,255));
		JP3.setBounds(0, 350, 520, 70);
		JP3.setLayout(null);
		
		JButton login = new JButton("登录");
		login.setFont(new Font("微软雅黑", 0, 20));
		login.setBackground(new Color(0, 135, 250));
		login.setForeground(Color.white);
		login.setBounds(140, 354, 257, 40);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				final String str=textField.getText();
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						try
						{
							Friends frame = new Friends(str);
							frame.setVisible(true);
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});
				setVisible(false);
			}
		});
		JP3.add(login);
		
		return JP3;
		}
	
	class MouseEventListener implements MouseInputListener {
	     
	    Point origin;
	    //鼠标拖拽想要移动的目标组件
	    Login_system frame;
	     
	    public MouseEventListener(Login_system frame) {
	      this.frame = frame;
	      origin = new Point();
	    }
	     
	    @Override
	    public void mouseClicked(MouseEvent e) {}
	 
	    /**
	    * 记录鼠标按下时的点
	    */
	    @Override
	    public void mousePressed(MouseEvent e) {
	      origin.x = e.getX(); 
	      origin.y = e.getY();
	    }
	 
	    @Override
	    public void mouseReleased(MouseEvent e) {}
	 
	    //鼠标移进标题栏时，设置鼠标图标为移动图标
	    @Override
	    public void mouseEntered(MouseEvent e) {
	      this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	    }
	     
	    //鼠标移出标题栏时，设置鼠标图标为默认指针
	    @Override
	    public void mouseExited(MouseEvent e) {
	      this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    }
	 
	    /**
	    * 鼠标在标题栏拖拽时，设置窗口的坐标位置
	    * 窗口新的坐标位置 = 移动前坐标位置+（鼠标指针当前坐标-鼠标按下时指针的位置）
	    */
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

  


