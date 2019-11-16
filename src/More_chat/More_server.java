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
        frame.setTitle("������");
        frame.setVisible(true);

        frame.setSize(600, 400);  
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width; //��ȡ��Ļ��� 
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;  //��ȡ��Ļ�߶�
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
        southPanel.setBorder(new TitledBorder("д��Ϣ"));  
        
        txt_message = new JTextField();
        // �ı��򰴻س���ʱ�¼�  
        txt_message.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                send();  
            }  
        });  
        southPanel.add(txt_message, "Center");
        
        // �������Ͱ�ťʱ�¼�  
        btn_send = new JButton("����");  
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
        leftPanel.setBorder(new TitledBorder("�����û�"));  
        rightPanel = new JScrollPane(contentArea);  
        rightPanel.setBorder(new TitledBorder("��Ϣ��ʾ��"));  
        centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        centerSplit.setDividerLocation(100);
		
		return centerSplit;
	}

	public JPanel creatPanelNorth() {
		
		northPanel = new JPanel();  
        northPanel.setLayout(new GridLayout(1, 6));
        northPanel.setBounds(0,0,600,50);
        
        JLabel label = new JLabel("��������");
        label.setBackground(SystemColor.inactiveCaptionBorder);
        northPanel.add(label);  
        txt_max = new JTextField("50"); 
        northPanel.add(txt_max);  
        northPanel.add(new JLabel("�˿�"));         
        txt_port = new JTextField("117");
        northPanel.add(txt_port);
        
        JButton btn_start = new JButton("����");
        btn_start.addActionListener(new ActionListener()
        {  
            public void actionPerformed(ActionEvent e)
            {  
                if (isStart)
                {  
                    JOptionPane.showMessageDialog(frame, "�������Ѵ�������״̬����Ҫ�ظ�������",  
                            "����", JOptionPane.ERROR_MESSAGE);  
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
                        throw new Exception("��������Ϊ��������");  
                    }  
                    if (max <= 0) 
                    {  
                        throw new Exception("��������Ϊ��������");  
                    }  
                    try 
                    {  
                        port = Integer.parseInt(txt_port.getText());  
                    } catch (Exception e1) {  
                        throw new Exception("�˿ں�Ϊ��������");  
                    }  
                    if (port <= 0) {  
                        throw new Exception("�˿ں� Ϊ��������");  
                    }  
                    serverStart(max, port);  
                    contentArea.append("�������ѳɹ�����!�������ޣ�" + max + ",�˿ڣ�" + port  
                            + "\r\n");  
                    JOptionPane.showMessageDialog(frame, "�������ɹ�����!");  
                    btn_start.setEnabled(false);  
                    txt_max.setEnabled(false);  
                    txt_port.setEnabled(false);  
                    btn_stop.setEnabled(true);  
                } 
                catch (Exception exc)
                {  
                    //JOptionPane.showMessageDialog(frame, exc.getMessage(),  
                            //"����", JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
        northPanel.add(btn_start);
        
        // ����ֹͣ��������ťʱ�¼�
        JButton btn_stop = new JButton("ֹͣ");
        btn_stop.setEnabled(false);  //ֹͣ��ť��ʱ���ܵ�� 
        btn_stop.addActionListener(new ActionListener()
        {  
            public void actionPerformed(ActionEvent e)
            {  
                if (!isStart)
                {  
                    JOptionPane.showMessageDialog(frame, "��������δ����������ֹͣ��", "����",  
                            JOptionPane.ERROR_MESSAGE);  
                    return;  
                }  
                try {  
                    closeServer();  
                    btn_start.setEnabled(true);  
                    txt_max.setEnabled(true);  
                    txt_port.setEnabled(true);  
                    btn_stop.setEnabled(false);  
                    contentArea.append("�������ɹ�ֹͣ!\r\n");  
                    JOptionPane.showMessageDialog(frame, "�������ɹ�ֹͣ��");  
                } 
                catch (Exception exc) 
                {  
                    JOptionPane.showMessageDialog(frame, "ֹͣ�����������쳣��", "����",  
                            JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
        northPanel.add(btn_stop);  
        
		return northPanel;
	}

	public void send() {  
        if (!isStart) {  
            JOptionPane.showMessageDialog(frame, "��������δ����,���ܷ�����Ϣ��", "����",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        if (clients.size() == 0) {  
            JOptionPane.showMessageDialog(frame, "û���û�����,���ܷ�����Ϣ��", "����",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        String message = txt_message.getText().trim();  
        if (message == null || message.equals("")) {  
            JOptionPane.showMessageDialog(frame, "��Ϣ����Ϊ�գ�", "����",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        sendServerMessage(message);// Ⱥ����������Ϣ  
        contentArea.append("������˵��" + txt_message.getText() + "\r\n");  
        txt_message.setText(null);  
    }  
	
    // ����������  
    public void serverStart(int max, int port) throws java.net.BindException {  
        try {  
            clients = new ArrayList<ClientThread>();  
            serverSocket = new ServerSocket(port);  
            serverThread = new ServerThread(serverSocket, max);  
            serverThread.start();  
            isStart = true;  
        } catch (BindException e) {  
            isStart = false;  
            throw new BindException("�˿ں��ѱ�ռ�ã��뻻һ����");  
        } catch (Exception e1) {  
            e1.printStackTrace();  
            isStart = false;  
            throw new BindException("�����������쳣��");  
        }  
    }  
  
    // �رշ�����  
    @SuppressWarnings("deprecation")  
    public void closeServer() {  
        try {  
            if (serverThread != null)  
                serverThread.stop();// ֹͣ�������߳�  
  
            for (int i = clients.size() - 1; i >= 0; i--) {  
                // �����������û����͹ر�����  
                clients.get(i).getWriter().println("CLOSE");  
                clients.get(i).getWriter().flush();  
                // �ͷ���Դ  
                clients.get(i).stop();// ֹͣ����Ϊ�ͻ��˷�����߳�  
                clients.get(i).reader.close();  
                clients.get(i).writer.close();  
                clients.get(i).socket.close();  
                clients.remove(i);  
            }  
            if (serverSocket != null) {  
                serverSocket.close();// �رշ�����������  
            }  
            listModel.removeAllElements();// ����û��б�  
            isStart = false;  
        } catch (IOException e) {  
            e.printStackTrace();  
            isStart = true;  
        }  
    }  
  
    // Ⱥ����������Ϣ  
    public void sendServerMessage(String message) {  
        for (int i = clients.size() - 1; i >= 0; i--) {  
            clients.get(i).getWriter().println("��������" + message + "(���˷���)");  
            clients.get(i).getWriter().flush();  
        }  
    }  
  
    // �������߳�  
    class ServerThread extends Thread {  
        private ServerSocket serverSocket;  
        private int max;// ��������  
  
        // �������̵߳Ĺ��췽��  
        public ServerThread(ServerSocket serverSocket, int max) {  
            this.serverSocket = serverSocket;  
            this.max = max;  
        }  
  
        public void run() {  
            while (true) {// ��ͣ�ĵȴ��ͻ��˵�����  
                try {  
                    Socket socket = serverSocket.accept();  
                    if (clients.size() == max) {// ����Ѵ���������  
                        BufferedReader r = new BufferedReader(  
                                new InputStreamReader(socket.getInputStream()));  
                        PrintWriter w = new PrintWriter(socket  
                                .getOutputStream());  
                        // ���տͻ��˵Ļ����û���Ϣ  
                        String inf = r.readLine();  
                        StringTokenizer st = new StringTokenizer(inf, "@");  
                        User user = new User(st.nextToken(), st.nextToken());  
                        // �������ӳɹ���Ϣ  
                        w.println("MAX@���������Բ���" + user.getName()  
                                + user.getIp() + "�����������������Ѵ����ޣ����Ժ������ӣ�");  
                        w.flush();  
                        // �ͷ���Դ  
                        r.close();  
                        w.close();  
                        socket.close();  
                        continue;  
                    }  
                    ClientThread client = new ClientThread(socket);  
                    client.start();// �����Դ˿ͻ��˷�����߳�  
                    clients.add(client);  
                    listModel.addElement(client.getUser().getName());// ���������б�  
                    contentArea.append(client.getUser().getName()  
                            + client.getUser().getIp() + "����!\r\n");  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
  
    // Ϊһ���ͻ��˷�����߳�  
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
  
        // �ͻ����̵߳Ĺ��췽��  
        public ClientThread(Socket socket) {  
            try {  
                this.socket = socket;  
                reader = new BufferedReader(new InputStreamReader(socket  
                        .getInputStream()));  
                writer = new PrintWriter(socket.getOutputStream());  
                // ���տͻ��˵Ļ����û���Ϣ  
                String inf = reader.readLine();  
                StringTokenizer st = new StringTokenizer(inf, "@");  //Ϊָ���ַ�������һ�� string tokenizer��
                //string tokenizer ������Ӧ�ó����ַ����ֽ�Ϊ���
                user = new User(st.nextToken(), st.nextToken());  //nextToken()���ش� string tokenizer ����һ����ǡ�
                // �������ӳɹ���Ϣ  
                writer.println(user.getName() + user.getIp() + "����������ӳɹ�!");  
                writer.flush();  
                // ������ǰ�����û���Ϣ  
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
                // �����������û����͸��û���������  
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
        public void run() {// ���Ͻ��տͻ��˵���Ϣ�����д���  
            String message = null;  
            while (true) {  
                try {  
                    message = reader.readLine();// ���տͻ�����Ϣ  
                    if (message.equals("CLOSE"))// ��������  
                    {  
                        contentArea.append(this.getUser().getName()  
                                + this.getUser().getIp() + "����!\r\n");  
                        // �Ͽ������ͷ���Դ  
                        reader.close();  
                        writer.close();  
                        socket.close();  
  
                        // �����������û����͸��û�����������  
                        for (int i = clients.size() - 1; i >= 0; i--) {  
                            clients.get(i).getWriter().println(  
                                    "DELETE@" + user.getName());  
                            clients.get(i).getWriter().flush();  
                        }  
  
                        listModel.removeElement(user.getName());// ���������б�  
  
                        // ɾ�������ͻ��˷����߳�  
                        for (int i = clients.size() - 1; i >= 0; i--) {  
                            if (clients.get(i).getUser() == user) {  
                                ClientThread temp = clients.get(i);  
                                clients.remove(i);// ɾ�����û��ķ����߳�  
                                temp.stop();// ֹͣ���������߳�  
                                return;  
                            }  
                        }  
                    } else {  
                        dispatcherMessage(message);// ת����Ϣ  
                    }  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
  
        // ת����Ϣ  
        public void dispatcherMessage(String message) {  
            StringTokenizer stringTokenizer = new StringTokenizer(message, "@");  
            String source = stringTokenizer.nextToken();  
            String owner = stringTokenizer.nextToken();  
            String content = stringTokenizer.nextToken();  
            message = source + "˵��" + content;  
            contentArea.append(message + "\r\n");  
            if (owner.equals("ALL")) {// Ⱥ��  
                for (int i = clients.size() - 1; i >= 0; i--) {  
                    clients.get(i).getWriter().println(message + "(���˷���)");  
                    clients.get(i).getWriter().flush();  
                }  
            }  
        }  
    }  
}  


