package More_chat;

import java.awt.BorderLayout;  
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;  
import java.awt.event.WindowEvent;  
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.net.BindException;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.util.ArrayList;  
import java.util.StringTokenizer;  
import javax.swing.DefaultListModel;  
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

//import More_chat.More_server.ClientThread;
//import More_chat.More_server.ServerThread;
import java.awt.SystemColor;

public class More_server extends JFrame {


	private static final long serialVersionUID = 7943585749737194006L;
	private JFrame frame;  
    private JTextArea contentArea;  
    private JTextField txt_message;  
    private JTextField txt_max;  
    private JTextField txt_port;   
    private JButton btn_stop;  
    private JButton btn_send;  
    private JPanel northPanel;  
    private JPanel southPanel;  
    private JScrollPane rightPanel;  
    private JScrollPane leftPanel;  
    private JSplitPane centerSplit;  
    private JList userList;  
    private DefaultListModel listModel;  
    private ServerSocket serverSocket;  
    private ServerThread serverThread;  
    private ArrayList<ClientThread> clients;  
    static Point origin = new Point();
  
    private boolean isStart = false;  
	//private JPanel contentPane;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					More_server frame = new More_server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public More_server() {
		
		JFrame frame = new JFrame(); 
        frame.setTitle("服务器");
        frame.setVisible(true);

        frame.setSize(600, 400);  
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width; //获取屏幕宽度 
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;  //获取屏幕高度
        frame.setLocation((screen_width - frame.getWidth()) / 2,(screen_height - frame.getHeight()) / 2);  

        frame.addWindowListener(new WindowAdapter() {  
            public void windowClosing(WindowEvent e) {  
                if (isStart) {  
                    closeServer();
                }  
                System.exit(0);
            }  
        }); 
        
        BorderLayout bl = new BorderLayout();  
        frame.getContentPane().setLayout(bl); 
	       
	    JPanel northPanel = creatPanelNorth();  
	    frame.getContentPane().add(northPanel, "North");  
	       
	    JSplitPane centerSplit = creatPanelCenter();  
	    frame.getContentPane().add(centerSplit, "Center");  
	    
	    JPanel southPanel = creatPanelSouth();  
	    frame.getContentPane().add(southPanel, "South"); 
          
    }  
  
	public JPanel creatPanelSouth() {

		southPanel = new JPanel(new BorderLayout());  
        southPanel.setBorder(new TitledBorder("写消息"));  
        
        txt_message = new JTextField();
        // 文本框按回车键时事件  
        txt_message.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                send();  
            }  
        });  
        southPanel.add(txt_message, "Center");
        
        // 单击发送按钮时事件  
        btn_send = new JButton("发送");  
        btn_send.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent arg0) {  
                send();  
            }  
        });  
        southPanel.add(btn_send, "East"); 
        
		return southPanel;
	}

	public JSplitPane creatPanelCenter() {
		
		listModel = new DefaultListModel();  
        userList = new JList(listModel);
        contentArea = new JTextArea();  
        contentArea.setEditable(false);  
        contentArea.setForeground(Color.blue);
        leftPanel = new JScrollPane(userList);  
        leftPanel.setBorder(new TitledBorder("在线用户"));  
        rightPanel = new JScrollPane(contentArea);  
        rightPanel.setBorder(new TitledBorder("消息显示区"));  
        centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        centerSplit.setDividerLocation(100);
		
		return centerSplit;
	}

	public JPanel creatPanelNorth() {
		
		northPanel = new JPanel();  
        northPanel.setLayout(new GridLayout(1, 6));
        northPanel.setBounds(0,0,600,50);
        
        JLabel label = new JLabel("人数上限");
        label.setBackground(SystemColor.inactiveCaptionBorder);
        northPanel.add(label);  
        txt_max = new JTextField("50"); 
        northPanel.add(txt_max);  
        northPanel.add(new JLabel("端口"));         
        txt_port = new JTextField("117");
        northPanel.add(txt_port);
        
        JButton btn_start = new JButton("启动");
        btn_start.addActionListener(new ActionListener()
        {  
            public void actionPerformed(ActionEvent e)
            {  
                if (isStart)
                {  
                    JOptionPane.showMessageDialog(frame, "服务器已处于启动状态，不要重复启动！",  
                            "错误", JOptionPane.ERROR_MESSAGE);  
                    return;  
                }  
                int max;  
                int port;  
                try
                {  
                    try
                    {  
                        max = Integer.parseInt(txt_max.getText());  
                    } 
                    catch (Exception e1) 
                    {  
                        throw new Exception("人数上限为正整数！");  
                    }  
                    if (max <= 0) 
                    {  
                        throw new Exception("人数上限为正整数！");  
                    }  
                    try 
                    {  
                        port = Integer.parseInt(txt_port.getText());  
                    } catch (Exception e1) {  
                        throw new Exception("端口号为正整数！");  
                    }  
                    if (port <= 0) {  
                        throw new Exception("端口号 为正整数！");  
                    }  
                    serverStart(max, port);  
                    contentArea.append("服务器已成功启动!人数上限：" + max + ",端口：" + port  
                            + "\r\n");  
                    JOptionPane.showMessageDialog(frame, "服务器成功启动!");  
                    btn_start.setEnabled(false);  
                    txt_max.setEnabled(false);  
                    txt_port.setEnabled(false);  
                    btn_stop.setEnabled(true);  
                } 
                catch (Exception exc)
                {  
                    //JOptionPane.showMessageDialog(frame, exc.getMessage(),  
                            //"错误", JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
        northPanel.add(btn_start);
        
        // 单击停止服务器按钮时事件
        JButton btn_stop = new JButton("停止");
        btn_stop.setEnabled(false);  //停止按钮此时不能点击 
        btn_stop.addActionListener(new ActionListener()
        {  
            public void actionPerformed(ActionEvent e)
            {  
                if (!isStart)
                {  
                    JOptionPane.showMessageDialog(frame, "服务器还未启动，无需停止！", "错误",  
                            JOptionPane.ERROR_MESSAGE);  
                    return;  
                }  
                try {  
                    closeServer();  
                    btn_start.setEnabled(true);  
                    txt_max.setEnabled(true);  
                    txt_port.setEnabled(true);  
                    btn_stop.setEnabled(false);  
                    contentArea.append("服务器成功停止!\r\n");  
                    JOptionPane.showMessageDialog(frame, "服务器成功停止！");  
                } 
                catch (Exception exc) 
                {  
                    JOptionPane.showMessageDialog(frame, "停止服务器发生异常！", "错误",  
                            JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
        northPanel.add(btn_stop);  
        
		return northPanel;
	}

	public void send() {  
        if (!isStart) {  
            JOptionPane.showMessageDialog(frame, "服务器还未启动,不能发送消息！", "错误",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        if (clients.size() == 0) {  
            JOptionPane.showMessageDialog(frame, "没有用户在线,不能发送消息！", "错误",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        String message = txt_message.getText().trim();  
        if (message == null || message.equals("")) {  
            JOptionPane.showMessageDialog(frame, "消息不能为空！", "错误",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        sendServerMessage(message);// 群发服务器消息  
        contentArea.append("服务器说：" + txt_message.getText() + "\r\n");  
        txt_message.setText(null);  
    }  
	
    // 启动服务器  
    public void serverStart(int max, int port) throws java.net.BindException {  
        try {  
            clients = new ArrayList<ClientThread>();  
            serverSocket = new ServerSocket(port);  
            serverThread = new ServerThread(serverSocket, max);  
            serverThread.start();  
            isStart = true;  
        } catch (BindException e) {  
            isStart = false;  
            throw new BindException("端口号已被占用，请换一个！");  
        } catch (Exception e1) {  
            e1.printStackTrace();  
            isStart = false;  
            throw new BindException("启动服务器异常！");  
        }  
    }  
  
    // 关闭服务器  
    @SuppressWarnings("deprecation")  
    public void closeServer() {  
        try {  
            if (serverThread != null)  
                serverThread.stop();// 停止服务器线程  
  
            for (int i = clients.size() - 1; i >= 0; i--) {  
                // 给所有在线用户发送关闭命令  
                clients.get(i).getWriter().println("CLOSE");  
                clients.get(i).getWriter().flush();  
                // 释放资源  
                clients.get(i).stop();// 停止此条为客户端服务的线程  
                clients.get(i).reader.close();  
                clients.get(i).writer.close();  
                clients.get(i).socket.close();  
                clients.remove(i);  
            }  
            if (serverSocket != null) {  
                serverSocket.close();// 关闭服务器端连接  
            }  
            listModel.removeAllElements();// 清空用户列表  
            isStart = false;  
        } catch (IOException e) {  
            e.printStackTrace();  
            isStart = true;  
        }  
    }  
  
    // 群发服务器消息  
    public void sendServerMessage(String message) {  
        for (int i = clients.size() - 1; i >= 0; i--) {  
            clients.get(i).getWriter().println("服务器：" + message + "(多人发送)");  
            clients.get(i).getWriter().flush();  
        }  
    }  
  
    // 服务器线程  
    class ServerThread extends Thread {  
        private ServerSocket serverSocket;  
        private int max;// 人数上限  
  
        // 服务器线程的构造方法  
        public ServerThread(ServerSocket serverSocket, int max) {  
            this.serverSocket = serverSocket;  
            this.max = max;  
        }  
  
        public void run() {  
            while (true) {// 不停的等待客户端的链接  
                try {  
                    Socket socket = serverSocket.accept();  
                    if (clients.size() == max) {// 如果已达人数上限  
                        BufferedReader r = new BufferedReader(  
                                new InputStreamReader(socket.getInputStream()));  
                        PrintWriter w = new PrintWriter(socket  
                                .getOutputStream());  
                        // 接收客户端的基本用户信息  
                        String inf = r.readLine();  
                        StringTokenizer st = new StringTokenizer(inf, "@");  
                        User user = new User(st.nextToken(), st.nextToken());  
                        // 反馈连接成功信息  
                        w.println("MAX@服务器：对不起，" + user.getName()  
                                + user.getIp() + "，服务器在线人数已达上限，请稍后尝试连接！");  
                        w.flush();  
                        // 释放资源  
                        r.close();  
                        w.close();  
                        socket.close();  
                        continue;  
                    }  
                    ClientThread client = new ClientThread(socket);  
                    client.start();// 开启对此客户端服务的线程  
                    clients.add(client);  
                    listModel.addElement(client.getUser().getName());// 更新在线列表  
                    contentArea.append(client.getUser().getName()  
                            + client.getUser().getIp() + "上线!\r\n");  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
  
    // 为一个客户端服务的线程  
    class ClientThread extends Thread {  
        private Socket socket;  
        private BufferedReader reader;  
        private PrintWriter writer;  
        private User user;  
  
        public BufferedReader getReader() {  
            return reader;  
        }  
  
        public PrintWriter getWriter() {  
            return writer;  
        }  
  
        public User getUser() {  
            return user;  
        }  
  
        // 客户端线程的构造方法  
        public ClientThread(Socket socket) {  
            try {  
                this.socket = socket;  
                reader = new BufferedReader(new InputStreamReader(socket  
                        .getInputStream()));  
                writer = new PrintWriter(socket.getOutputStream());  
                // 接收客户端的基本用户信息  
                String inf = reader.readLine();  
                StringTokenizer st = new StringTokenizer(inf, "@");  //为指定字符串构造一个 string tokenizer。
                //string tokenizer 类允许应用程序将字符串分解为标记
                user = new User(st.nextToken(), st.nextToken());  //nextToken()返回此 string tokenizer 的下一个标记。
                // 反馈连接成功信息  
                writer.println(user.getName() + user.getIp() + "与服务器连接成功!");  
                writer.flush();  
                // 反馈当前在线用户信息  
                if (clients.size() > 0) {  
                    String temp = "";  
                    for (int i = clients.size() - 1; i >= 0; i--) {  
                        temp += (clients.get(i).getUser().getName() + "/" + clients  
                                .get(i).getUser().getIp())  
                                + "@";  
                    }  
                    writer.println("USERLIST@" + clients.size() + "@" + temp);  
                    writer.flush();  
                }  
                // 向所有在线用户发送该用户上线命令  
                for (int i = clients.size() - 1; i >= 0; i--) {  
                    clients.get(i).getWriter().println(  
                            "ADD@" + user.getName() + user.getIp());  
                    clients.get(i).getWriter().flush();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
        @SuppressWarnings("deprecation")  
        public void run() {// 不断接收客户端的消息，进行处理。  
            String message = null;  
            while (true) {  
                try {  
                    message = reader.readLine();// 接收客户端消息  
                    if (message.equals("CLOSE"))// 下线命令  
                    {  
                        contentArea.append(this.getUser().getName()  
                                + this.getUser().getIp() + "下线!\r\n");  
                        // 断开连接释放资源  
                        reader.close();  
                        writer.close();  
                        socket.close();  
  
                        // 向所有在线用户发送该用户的下线命令  
                        for (int i = clients.size() - 1; i >= 0; i--) {  
                            clients.get(i).getWriter().println(  
                                    "DELETE@" + user.getName());  
                            clients.get(i).getWriter().flush();  
                        }  
  
                        listModel.removeElement(user.getName());// 更新在线列表  
  
                        // 删除此条客户端服务线程  
                        for (int i = clients.size() - 1; i >= 0; i--) {  
                            if (clients.get(i).getUser() == user) {  
                                ClientThread temp = clients.get(i);  
                                clients.remove(i);// 删除此用户的服务线程  
                                temp.stop();// 停止这条服务线程  
                                return;  
                            }  
                        }  
                    } else {  
                        dispatcherMessage(message);// 转发消息  
                    }  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
  
        // 转发消息  
        public void dispatcherMessage(String message) {  
            StringTokenizer stringTokenizer = new StringTokenizer(message, "@");  
            String source = stringTokenizer.nextToken();  
            String owner = stringTokenizer.nextToken();  
            String content = stringTokenizer.nextToken();  
            message = source + "说：" + content;  
            contentArea.append(message + "\r\n");  
            if (owner.equals("ALL")) {// 群发  
                for (int i = clients.size() - 1; i >= 0; i--) {  
                    clients.get(i).getWriter().println(message + "(多人发送)");  
                    clients.get(i).getWriter().flush();  
                }  
            }  
        }  
    }  
}  


