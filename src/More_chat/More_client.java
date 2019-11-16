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
    private MessageThread messageThread;// ���������Ϣ���߳�  
    private Map<String, User> onLineUsers = new HashMap<String, User>();// ���������û�  
  

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
                    closeConnection();// �ر�����  
                }  
                //System.exit(0);// �˳�����  
            }  
        }); 
    	JPanel JP1 = new JPanel();
    	JP1.setBackground(new Color(176,224,230));
    	JP1.setBounds(0,0,600, 490);
    	frame.setContentPane(JP1);
    	JP1.setLayout(null);

        JLabel label = new JLabel("�˿�");
        label.setFont(new Font("΢���ź�", Font.PLAIN, 17));
        label.setLocation(14, 14);
        label.setSize(70, 30);
        JP1.add(label); 
        
        txt_port = new JTextField("117");
        txt_port.setFont(new Font("΢���ź�", Font.PLAIN, 17));
        txt_port.setLocation(67, 15);
        txt_port.setSize(70, 30);
        JP1.add(txt_port);
        
        JLabel label_1 = new JLabel("������IP");
        label_1.setForeground(Color.WHITE);
        label_1.setFont(new Font("΢���ź�", Font.PLAIN, 17));
        label_1.setLocation(182, 13);
        label_1.setSize(70, 30);
        JP1.add(label_1);  
        
        txt_hostIp = new JTextField("127.0.0.1");  
        txt_hostIp.setFont(new Font("΢���ź�", Font.PLAIN, 17));
        txt_hostIp.setLocation(266, 14);
        txt_hostIp.setSize(100, 30);
        JP1.add(txt_hostIp); 
        
        JLabel label_2 = new JLabel("����");
        label_2.setLocation(181, 0);
        label_2.setSize(70, 30);
        //JP1.add(label_2);  
        
        txt_name = new JTextField(name);  
        txt_name.setLocation(79, 0);
        txt_name.setSize(70, 30);
        //JP1.add(txt_name);
        
        btn_start = new JButton("����");  
        btn_start.setFont(new Font("΢���ź�", Font.PLAIN, 17));
        btn_start.setBackground(Color.WHITE);
        btn_start.setLocation(393, 15);
        btn_start.setSize(70, 30);
        btn_start.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                int port;  
                if (isConnected) {  
                    JOptionPane.showMessageDialog(frame, "�Ѵ���������״̬����Ҫ�ظ�����!","����", JOptionPane.ERROR_MESSAGE);  
                    return;  
                }  
                try {  
                    try {  
                        port = Integer.parseInt(txt_port.getText().trim());  
                    } catch (NumberFormatException e2) {  
                        throw new Exception("�˿ںŲ�����Ҫ��!�˿�Ϊ����!");  
                    }  
                    String hostIp = txt_hostIp.getText().trim();  
                    String name = txt_name.getText().trim();  
                    if (name.equals("") || hostIp.equals("")) 
                    {  
                        throw new Exception("������������IP����Ϊ��!");  
                    }  
                    boolean flag = connectServer(port, hostIp, name);  
                    if (flag == false) 
                    {  
                        throw new Exception("�����������ʧ��!");  
                    }  
                    frame.setTitle(name);  
                    JOptionPane.showMessageDialog(frame, "�ɹ�����!");  
                } catch (Exception exc) {  
                    JOptionPane.showMessageDialog(frame, exc.getMessage(),  
                            "����", JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
        JP1.add(btn_start);
        
        
        
        btn_stop = new JButton("�Ͽ�");  
        btn_stop.setBackground(Color.WHITE);
        btn_stop.setFont(new Font("΢���ź�", Font.PLAIN, 17));
        btn_stop.setLocation(509, 14);
        btn_stop.setSize(70, 30);
        btn_stop.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                if (!isConnected) {  
                    JOptionPane.showMessageDialog(frame, "�Ѵ��ڶϿ�״̬����Ҫ�ظ��Ͽ�!",  
                            "����", JOptionPane.ERROR_MESSAGE);  
                    return;  
                }  
                try {  
                    boolean flag = closeConnection();// �Ͽ�����  
                    if (flag == false) {  
                        throw new Exception("�Ͽ����ӷ����쳣��");  
                    }  
                    JOptionPane.showMessageDialog(frame, "�ɹ��Ͽ�!");  
                } catch (Exception exc) {  
                    JOptionPane.showMessageDialog(frame, exc.getMessage(),  
                            "����", JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
        JP1.add(btn_stop);
        
        JButton jb_close = new JButton();
        jb_close.setForeground(new Color(0, 0, 0));
        jb_close.setBackground(SystemColor.text);
        jb_close.setFont(new Font("΢���ź�", Font.PLAIN, 17));
		jb_close.setBounds(167,427,90,30);
		jb_close.setText("�ر�");
		jb_close.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		JP1.add(jb_close);
        
         
        btn_send = new JButton("����"); 
        btn_send.setFont(new Font("΢���ź�", Font.PLAIN, 17));
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
        textField.setFont(new Font("΢���ź�", Font.PLAIN, 17));
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
    	textArea.setFont(new Font("΢���ź�", Font.PLAIN, 16));
    	textArea.setEditable(false);
    	scrollPane.setViewportView(textArea);  
    	
    	listModel = new DefaultListModel();  
        userList = new JList(listModel);
        userList.setFont(new Font("΢���ź�", Font.PLAIN, 17));
    	JScrollPane scrollPane2 = new JScrollPane(userList);
    	scrollPane2.setBounds(393, 58, 186, 399);
    	scrollPane2.setBorder(new TitledBorder("�����û�"));
    	JP1.add(scrollPane2);
    	
    	JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Single_server.class.getResource("/TIM/hua.jpg")));
		lblNewLabel.setBounds(0, 0, 593, 479);
		JP1.add(lblNewLabel);
    	
		    
    }  
  
 // ִ�з���  
    public void send() {  
        if (!isConnected) {  
            JOptionPane.showMessageDialog(frame, "��û�����ӷ��������޷�������Ϣ��", "����",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        String message = textField.getText().trim();  
        if (message == null || message.equals("")) {  
            JOptionPane.showMessageDialog(frame, "��Ϣ����Ϊ�գ�", "����",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        sendMessage(frame.getTitle() + "@" + "ALL" + "@" + message);  
        textField.setText(null);  
    }  
    
    /**  
     * ���ӷ�����   
     */  
    public boolean connectServer(int port, String hostIp, String name) {  
        // ���ӷ�����  
        try {  
            socket = new Socket(hostIp, port);// ���ݶ˿ںźͷ�����ip��������  
            writer = new PrintWriter(socket.getOutputStream());  
            reader = new BufferedReader(new InputStreamReader(socket  
                    .getInputStream()));  
            // ���Ϳͻ����û�������Ϣ(�û�����ip��ַ)  
            sendMessage(name + "@" + socket.getLocalAddress().toString());  
            // ����������Ϣ���߳�  
            messageThread = new MessageThread(reader, textArea);  
            messageThread.start();  
            isConnected = true;// �Ѿ���������  
            return true;  
        } catch (Exception e) {  
            textArea.append("��˿ں�Ϊ��" + port + "    IP��ַΪ��" + hostIp  
                    + "   �ķ���������ʧ��!" + "\r\n");  
            isConnected = false;// δ������  
            return false;  
        }  
    }  
  
    /**  
     * ������Ϣ   
     */  
    public void sendMessage(String message) {  
        writer.println(message);  
        writer.flush();  
    }  
  
    /**  
     * �ͻ��������ر�����  
     */  
    @SuppressWarnings("deprecation")  
    public synchronized boolean closeConnection() {  
        try {  
            sendMessage("CLOSE");// ���ͶϿ����������������  
            messageThread.stop();// ֹͣ������Ϣ�߳�  
            // �ͷ���Դ  
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
  
    // ���Ͻ�����Ϣ���߳�  
    class MessageThread extends Thread {  
        private BufferedReader reader;  
        private JTextArea textArea;  
  
        // ������Ϣ�̵߳Ĺ��췽��  
        public MessageThread(BufferedReader reader, JTextArea textArea) {  
            this.reader = reader;  
            this.textArea = textArea;  
        }  
  
        // �����Ĺر�����  
        public synchronized void closeCon() throws Exception {  
            // ����û��б�  
            listModel.removeAllElements();  
            // �����Ĺر������ͷ���Դ  
            if (reader != null) {  
                reader.close();  
            }  
            if (writer != null) {  
                writer.close();  
            }  
            if (socket != null) {  
                socket.close();  
            }  
            isConnected = false;// �޸�״̬Ϊ�Ͽ�  
        }  
  
        public void run() {  
            String message = "";  
            while (true) {  
                try {  
                    message = reader.readLine();  
                    StringTokenizer stringTokenizer = new StringTokenizer(  
                            message, "/@");  
                    String command = stringTokenizer.nextToken();// ����  
                    if (command.equals("CLOSE"))// �������ѹر�����  
                    {  
                        textArea.append("�������ѹر�!\r\n");  
                        closeCon();// �����Ĺر�����  
                        return;// �����߳�  
                    } else if (command.equals("ADD")) {// ���û����߸��������б�  
                        String username = "";  
                        String userIp = "";  
                        if ((username = stringTokenizer.nextToken()) != null  
                                && (userIp = stringTokenizer.nextToken()) != null) {  
                            User user = new User(username, userIp);  
                            onLineUsers.put(username, user);  
                            listModel.addElement(username);  
                        }  
                    } else if (command.equals("DELETE")) {// ���û����߸��������б�  
                        String username = stringTokenizer.nextToken();  
                        User user = (User) onLineUsers.get(username);  
                        onLineUsers.remove(user);  
                        listModel.removeElement(username);  
                    } else if (command.equals("USERLIST")) {// ���������û��б�  
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
                    } else if (command.equals("MAX")) {// �����Ѵ�����  
                        textArea.append(stringTokenizer.nextToken()  
                                + stringTokenizer.nextToken() + "\r\n");  
                        closeCon();// �����Ĺر�����  
                        JOptionPane.showMessageDialog(frame, "������������������", "����",  
                                JOptionPane.ERROR_MESSAGE);  
                        return;// �����߳�  
                    } else {// ��ͨ��Ϣ  
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
