package More_chat;

import java.awt.BorderLayout;  

import java.awt.Color;   
import java.awt.Toolkit;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.awt.event.WindowAdapter;  
import java.awt.event.WindowEvent;  
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.net.Socket;  
import java.util.HashMap;  
import java.util.Map;  
import java.util.StringTokenizer;  
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JList;  
import javax.swing.JOptionPane;  
import javax.swing.JPanel;  
import javax.swing.JScrollPane;  
import javax.swing.JSplitPane;  
import javax.swing.JTextArea;  
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import Single_chat.Single_server;
import java.awt.Font;
import java.awt.SystemColor;  
  
public class More_client extends JFrame{  
  
    private JFrame frame;  
    private JList userList;  
    private JTextArea textArea;  
    private JTextField textField;  
    private JTextField txt_port;  
    private JTextField txt_hostIp;  
    private JTextField txt_name;  
    private JButton btn_start;  
    private JButton btn_stop;  
    private JButton btn_send;  
    private JPanel northPanel;  
    private DefaultListModel listModel;  
    private boolean isConnected = false;  
    private Socket socket;  
    private PrintWriter writer;  
    private BufferedReader reader;  
    private MessageThread messageThread;// 负责接收消息的线程  
    private Map<String, User> onLineUsers = new HashMap<String, User>();// 所有在线用户  
  

    public More_client(String name) {  
    	
    	frame = new JFrame(name);
    	frame.getContentPane().setLayout(null);
        frame.setVisible(true);
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(Toolkit.getDefaultToolkit().createImage(More_client.class.getResource(name+".jpg")));
        frame.setSize(611, 526);  
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;  
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;  
        frame.setLocation((screen_width - frame.getWidth()) / 2, (screen_height - frame.getHeight()) / 2);  
    	frame.addWindowListener(new WindowAdapter()
    	{  
            public void windowClosing(WindowEvent e)
            {  
                if (isConnected)
                {  
                    closeConnection();// 关闭连接  
                }  
                //System.exit(0);// 退出程序  
            }  
        }); 
    	JPanel JP1 = new JPanel();
    	JP1.setBackground(new Color(176,224,230));
    	JP1.setBounds(0,0,600, 490);
    	frame.setContentPane(JP1);
    	JP1.setLayout(null);

        JLabel label = new JLabel("端口");
        label.setFont(new Font("微软雅黑", Font.PLAIN, 17));
        label.setLocation(14, 14);
        label.setSize(70, 30);
        JP1.add(label); 
        
        txt_port = new JTextField("117");
        txt_port.setFont(new Font("微软雅黑", Font.PLAIN, 17));
        txt_port.setLocation(67, 15);
        txt_port.setSize(70, 30);
        JP1.add(txt_port);
        
        JLabel label_1 = new JLabel("服务器IP");
        label_1.setForeground(Color.WHITE);
        label_1.setFont(new Font("微软雅黑", Font.PLAIN, 17));
        label_1.setLocation(182, 13);
        label_1.setSize(70, 30);
        JP1.add(label_1);  
        
        txt_hostIp = new JTextField("127.0.0.1");  
        txt_hostIp.setFont(new Font("微软雅黑", Font.PLAIN, 17));
        txt_hostIp.setLocation(266, 14);
        txt_hostIp.setSize(100, 30);
        JP1.add(txt_hostIp); 
        
        JLabel label_2 = new JLabel("姓名");
        label_2.setLocation(181, 0);
        label_2.setSize(70, 30);
        //JP1.add(label_2);  
        
        txt_name = new JTextField(name);  
        txt_name.setLocation(79, 0);
        txt_name.setSize(70, 30);
        //JP1.add(txt_name);
        
        btn_start = new JButton("连接");  
        btn_start.setFont(new Font("微软雅黑", Font.PLAIN, 17));
        btn_start.setBackground(Color.WHITE);
        btn_start.setLocation(393, 15);
        btn_start.setSize(70, 30);
        btn_start.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                int port;  
                if (isConnected) {  
                    JOptionPane.showMessageDialog(frame, "已处于连接上状态，不要重复连接!","错误", JOptionPane.ERROR_MESSAGE);  
                    return;  
                }  
                try {  
                    try {  
                        port = Integer.parseInt(txt_port.getText().trim());  
                    } catch (NumberFormatException e2) {  
                        throw new Exception("端口号不符合要求!端口为整数!");  
                    }  
                    String hostIp = txt_hostIp.getText().trim();  
                    String name = txt_name.getText().trim();  
                    if (name.equals("") || hostIp.equals("")) 
                    {  
                        throw new Exception("姓名、服务器IP不能为空!");  
                    }  
                    boolean flag = connectServer(port, hostIp, name);  
                    if (flag == false) 
                    {  
                        throw new Exception("与服务器连接失败!");  
                    }  
                    frame.setTitle(name);  
                    JOptionPane.showMessageDialog(frame, "成功连接!");  
                } catch (Exception exc) {  
                    JOptionPane.showMessageDialog(frame, exc.getMessage(),  
                            "错误", JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
        JP1.add(btn_start);
        
        
        
        btn_stop = new JButton("断开");  
        btn_stop.setBackground(Color.WHITE);
        btn_stop.setFont(new Font("微软雅黑", Font.PLAIN, 17));
        btn_stop.setLocation(509, 14);
        btn_stop.setSize(70, 30);
        btn_stop.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                if (!isConnected) {  
                    JOptionPane.showMessageDialog(frame, "已处于断开状态，不要重复断开!",  
                            "错误", JOptionPane.ERROR_MESSAGE);  
                    return;  
                }  
                try {  
                    boolean flag = closeConnection();// 断开连接  
                    if (flag == false) {  
                        throw new Exception("断开连接发生异常！");  
                    }  
                    JOptionPane.showMessageDialog(frame, "成功断开!");  
                } catch (Exception exc) {  
                    JOptionPane.showMessageDialog(frame, exc.getMessage(),  
                            "错误", JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
        JP1.add(btn_stop);
        
        JButton jb_close = new JButton();
        jb_close.setForeground(new Color(0, 0, 0));
        jb_close.setBackground(SystemColor.text);
        jb_close.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		jb_close.setBounds(167,427,90,30);
		jb_close.setText("关闭");
		jb_close.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		JP1.add(jb_close);
        
         
        btn_send = new JButton("发送"); 
        btn_send.setFont(new Font("微软雅黑", Font.PLAIN, 17));
        btn_send.setBackground(SystemColor.text);
        btn_send.setForeground(SystemColor.textHighlight);
        btn_send.setLocation(276, 427);
        btn_send.setSize(90, 30);
        btn_send.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                send();  
            }  
        });
        JP1.add(btn_send);  
        
        textField = new JTextField();
        textField.setFont(new Font("微软雅黑", Font.PLAIN, 17));
        textField.setLocation(14, 340);
        textField.setSize(352, 74);
        textField.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent arg0) {  
                send();  
            }  
        });
        JP1.add(textField); 
        
        JScrollPane scrollPane = new JScrollPane();
    	scrollPane.setBounds(14, 56, 352, 271);
    	JP1.add(scrollPane);
    	
    	textArea = new JTextArea();
    	textArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
    	textArea.setEditable(false);
    	scrollPane.setViewportView(textArea);  
    	
    	listModel = new DefaultListModel();  
        userList = new JList(listModel);
        userList.setFont(new Font("微软雅黑", Font.PLAIN, 17));
    	JScrollPane scrollPane2 = new JScrollPane(userList);
    	scrollPane2.setBounds(393, 58, 186, 399);
    	scrollPane2.setBorder(new TitledBorder("在线用户"));
    	JP1.add(scrollPane2);
    	
    	JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Single_server.class.getResource("/TIM/hua.jpg")));
		lblNewLabel.setBounds(0, 0, 593, 479);
		JP1.add(lblNewLabel);
    	
		    
    }  
  
 // 执行发送  
    public void send() {  
        if (!isConnected) {  
            JOptionPane.showMessageDialog(frame, "还没有连接服务器，无法发送消息！", "错误",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        String message = textField.getText().trim();  
        if (message == null || message.equals("")) {  
            JOptionPane.showMessageDialog(frame, "消息不能为空！", "错误",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        sendMessage(frame.getTitle() + "@" + "ALL" + "@" + message);  
        textField.setText(null);  
    }  
    
    /**  
     * 连接服务器   
     */  
    public boolean connectServer(int port, String hostIp, String name) {  
        // 连接服务器  
        try {  
            socket = new Socket(hostIp, port);// 根据端口号和服务器ip建立连接  
            writer = new PrintWriter(socket.getOutputStream());  
            reader = new BufferedReader(new InputStreamReader(socket  
                    .getInputStream()));  
            // 发送客户端用户基本信息(用户名和ip地址)  
            sendMessage(name + "@" + socket.getLocalAddress().toString());  
            // 开启接收消息的线程  
            messageThread = new MessageThread(reader, textArea);  
            messageThread.start();  
            isConnected = true;// 已经连接上了  
            return true;  
        } catch (Exception e) {  
            textArea.append("与端口号为：" + port + "    IP地址为：" + hostIp  
                    + "   的服务器连接失败!" + "\r\n");  
            isConnected = false;// 未连接上  
            return false;  
        }  
    }  
  
    /**  
     * 发送消息   
     */  
    public void sendMessage(String message) {  
        writer.println(message);  
        writer.flush();  
    }  
  
    /**  
     * 客户端主动关闭连接  
     */  
    @SuppressWarnings("deprecation")  
    public synchronized boolean closeConnection() {  
        try {  
            sendMessage("CLOSE");// 发送断开连接命令给服务器  
            messageThread.stop();// 停止接受消息线程  
            // 释放资源  
            if (reader != null) {  
                reader.close();  
            }  
            if (writer != null) {  
                writer.close();  
            }  
            if (socket != null) {  
                socket.close();  
            }  
            isConnected = false;  
            return true;  
        } catch (IOException e1) {  
            e1.printStackTrace();  
            isConnected = true;  
            return false;  
        }  
    }  
  
    // 不断接收消息的线程  
    class MessageThread extends Thread {  
        private BufferedReader reader;  
        private JTextArea textArea;  
  
        // 接收消息线程的构造方法  
        public MessageThread(BufferedReader reader, JTextArea textArea) {  
            this.reader = reader;  
            this.textArea = textArea;  
        }  
  
        // 被动的关闭连接  
        public synchronized void closeCon() throws Exception {  
            // 清空用户列表  
            listModel.removeAllElements();  
            // 被动的关闭连接释放资源  
            if (reader != null) {  
                reader.close();  
            }  
            if (writer != null) {  
                writer.close();  
            }  
            if (socket != null) {  
                socket.close();  
            }  
            isConnected = false;// 修改状态为断开  
        }  
  
        public void run() {  
            String message = "";  
            while (true) {  
                try {  
                    message = reader.readLine();  
                    StringTokenizer stringTokenizer = new StringTokenizer(  
                            message, "/@");  
                    String command = stringTokenizer.nextToken();// 命令  
                    if (command.equals("CLOSE"))// 服务器已关闭命令  
                    {  
                        textArea.append("服务器已关闭!\r\n");  
                        closeCon();// 被动的关闭连接  
                        return;// 结束线程  
                    } else if (command.equals("ADD")) {// 有用户上线更新在线列表  
                        String username = "";  
                        String userIp = "";  
                        if ((username = stringTokenizer.nextToken()) != null  
                                && (userIp = stringTokenizer.nextToken()) != null) {  
                            User user = new User(username, userIp);  
                            onLineUsers.put(username, user);  
                            listModel.addElement(username);  
                        }  
                    } else if (command.equals("DELETE")) {// 有用户下线更新在线列表  
                        String username = stringTokenizer.nextToken();  
                        User user = (User) onLineUsers.get(username);  
                        onLineUsers.remove(user);  
                        listModel.removeElement(username);  
                    } else if (command.equals("USERLIST")) {// 加载在线用户列表  
                        int size = Integer  
                                .parseInt(stringTokenizer.nextToken());  
                        String username = null;  
                        String userIp = null;  
                        for (int i = 0; i < size; i++) {  
                            username = stringTokenizer.nextToken();  
                            userIp = stringTokenizer.nextToken();  
                            User user = new User(username, userIp);  
                            onLineUsers.put(username, user);  
                            listModel.addElement(username);  
                        }  
                    } else if (command.equals("MAX")) {// 人数已达上限  
                        textArea.append(stringTokenizer.nextToken()  
                                + stringTokenizer.nextToken() + "\r\n");  
                        closeCon();// 被动的关闭连接  
                        JOptionPane.showMessageDialog(frame, "服务器缓冲区已满！", "错误",  
                                JOptionPane.ERROR_MESSAGE);  
                        return;// 结束线程  
                    } else {// 普通消息  
                        textArea.append(message + "\r\n");  
                    }  
                } catch (IOException e) {  
                    e.printStackTrace();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
}  
