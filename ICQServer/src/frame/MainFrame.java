package frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import listener.ServerListener;

/**
 * 作者：李强
 * 服务器主窗口
 */

public class MainFrame extends JFrame {
	
	ServerListener sendInfo;
	
	public JComboBox combobox;//选择发送消息的接受者,列表显示
	public JTextArea messageShow;//服务端的信息显示
	JScrollPane messageScrollPane;//信息显示的滚动条
	public JTextField showStatus;//显示用户连接状态，在线人数
	JLabel sendToLabel,messageLabel;
	public JTextField sysMessage;//服务端消息的发送，写信息
	public JButton sysMessageButton;//服务端消息的发送按钮

	//建立菜单栏
	JMenuBar jMenuBar = new JMenuBar(); 
	//建立菜单组
	JMenu serviceMenu = new JMenu ("服务"); 
	//建立菜单项
	public JMenuItem portItem = new JMenuItem ("端口设置");
	public JMenuItem startItem = new JMenuItem ("启动服务");
	public JMenuItem stopItem=new JMenuItem ("停止服务");
	public JMenuItem exitItem=new JMenuItem ("退出");
	
	JMenu helpMenu=new JMenu ("帮助");
	public JMenuItem helpItem=new JMenuItem ("帮助");

	//建立工具栏
	JToolBar toolBar = new JToolBar();

	//建立工具栏中的按钮组件
	public JButton portSet;//启动服务端侦听
	public JButton startServer;//启动服务端侦听
	public JButton stopServer;//关闭服务端侦听
	public JButton exitButton;//退出按钮
	
	//框架的大小
	Dimension faceSize = new Dimension(400, 600);

	JPanel downPanel ;
	GridBagLayout girdBag;
	GridBagConstraints girdBagCon;
	
	/**
	 * 构造函数
	 */
	public MainFrame(){
		init();//初始化程序

		//添加框架的关闭事件处理
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		//设置框架的大小
		this.setSize(faceSize);
		this.setVisible(true);
		//设置运行时窗口的位置
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width - faceSize.getWidth()) / 2,
						  (int) (screenSize.height - faceSize.getHeight()) / 2);
		this.setResizable(false);

		this.setTitle("聊天室服务端"); //设置标题
		
	}
	
	/**
	 * 程序初始化函数
	 */
	public void init(){

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//添加菜单栏
		serviceMenu.add (portItem);
		serviceMenu.add (startItem);
		serviceMenu.add (stopItem);
		serviceMenu.add (exitItem);
		jMenuBar.add (serviceMenu); 
		helpMenu.add (helpItem);
		jMenuBar.add (helpMenu); 
		setJMenuBar (jMenuBar);

		//初始化按钮
		portSet = new JButton("端口设置");
		startServer = new JButton("启动服务");
		stopServer = new JButton("停止服务" );
		exitButton = new JButton("退出" );
		//将按钮添加到工具栏
		toolBar.add(portSet);
		toolBar.addSeparator();//添加分隔栏
		toolBar.add(startServer);
		toolBar.add(stopServer);
		toolBar.addSeparator();//添加分隔栏
		toolBar.add(exitButton);
		contentPane.add(toolBar,BorderLayout.NORTH);

		//初始时，令停止服务按钮不可用
		stopServer.setEnabled(false);
		stopItem .setEnabled(false);
		
		combobox = new JComboBox();
		combobox.insertItemAt("所有人",0);
		combobox.setSelectedIndex(0);
		
		messageShow = new JTextArea();
		messageShow.setEditable(false);
		//添加滚动条
		messageScrollPane = new JScrollPane(messageShow,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messageScrollPane.setPreferredSize(new Dimension(400,400));
		messageScrollPane.revalidate();
		
		showStatus = new JTextField(35);
		showStatus.setEditable(false);
		
		sysMessage = new JTextField(24);
		sysMessage.setEnabled(false);
		sysMessageButton = new JButton();
		sysMessageButton.setText("发送");

		sendToLabel = new JLabel("发送至:");
		messageLabel = new JLabel("发送消息:");
		downPanel = new JPanel();
		girdBag = new GridBagLayout();
		downPanel.setLayout(girdBag);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 0;
		girdBagCon.gridwidth = 3;
		girdBagCon.gridheight = 2;
		girdBagCon.ipadx = 5;
		girdBagCon.ipady = 5;
		JLabel none = new JLabel("    ");
		girdBag.setConstraints(none,girdBagCon);
		downPanel.add(none);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 2;
		girdBagCon.insets = new Insets(1,0,0,0);
		girdBagCon.ipadx = 5;
		girdBagCon.ipady = 5;
		girdBag.setConstraints(sendToLabel,girdBagCon);
		downPanel.add(sendToLabel);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx =1;
		girdBagCon.gridy = 2;
		girdBagCon.anchor = GridBagConstraints.LINE_START;
		girdBag.setConstraints(combobox,girdBagCon);
		downPanel.add(combobox);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(messageLabel,girdBagCon);
		downPanel.add(messageLabel);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 1;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(sysMessage,girdBagCon);
		downPanel.add(sysMessage);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 2;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(sysMessageButton,girdBagCon);
		downPanel.add(sysMessageButton);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 4;
		girdBagCon.gridwidth = 3;
		girdBag.setConstraints(showStatus,girdBagCon);
		downPanel.add(showStatus);

		contentPane.add(messageScrollPane,BorderLayout.CENTER);
		contentPane.add(downPanel,BorderLayout.SOUTH);
		
		sendInfo = new ServerListener(this);
		
		//关闭程序时的操作
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					sendInfo.stopService();
					System.exit(0);
				}
			}
		);
	}

	public int showConfirmDialog(String title, String msg) {
		int j=JOptionPane.showConfirmDialog(
				this,title,msg,
				JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE);
			
		if (j == JOptionPane.YES_OPTION){
			return 1;
		}
		return 0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainFrame mainFrame = new MainFrame();
	}

}
