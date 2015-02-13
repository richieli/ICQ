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
import javax.swing.JCheckBox;
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

import listener.ClientListener;

/**
 * 作者：李强
 * 客户端主窗口
 */

public class MainFrame extends JFrame {

	ClientListener clientListener;
	
	public JComboBox combobox;//选择发送消息的接受者
	public JTextArea messageShow;//客户端的信息显示
	JScrollPane messageScrollPane;//信息显示的滚动条

	JLabel express,sendToLabel,messageLabel ;

	public JTextField clientMessage;//客户端消息的发送
	public JCheckBox checkbox;//悄悄话
	public JComboBox actionlist;//表情选择
	public JButton clientMessageButton;//发送消息
	public JTextField showStatus;//显示用户连接状态
	
	//建立菜单栏
	JMenuBar jMenuBar = new JMenuBar(); 
	//建立菜单组
	JMenu operateMenu = new JMenu ("操作"); 
	//建立菜单项
	public JMenuItem loginItem = new JMenuItem ("用户登录");
	public JMenuItem logoffItem = new JMenuItem ("用户注销");
	public JMenuItem exitItem=new JMenuItem ("退出");

	JMenu conMenu=new JMenu ("设置");
	public JMenuItem userItem=new JMenuItem ("用户设置");
	public JMenuItem connectItem=new JMenuItem ("连接设置");
	
	
	JMenu helpMenu=new JMenu ("帮助");
	public JMenuItem helpItem=new JMenuItem ("帮助");

	//建立工具栏
	JToolBar toolBar = new JToolBar();
	//建立工具栏中的按钮组件
	public JButton loginButton;//用户登录
	public JButton logoffButton;//用户注销
	public JButton userButton;//用户信息的设置
	public JButton connectButton;//连接设置
	public JButton exitButton;//退出按钮

	//框架的大小
	Dimension faceSize = new Dimension(400, 600);

	JPanel downPanel ;
	GridBagLayout girdBag;
	GridBagConstraints girdBagCon;
		
	public MainFrame() {
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
		this.setTitle(clientListener.userName); //设置标题
	}
	
	/**
	 * 程序初始化函数
	 */
	public void init() {

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//添加菜单栏
		operateMenu.add (loginItem);
		operateMenu.add (logoffItem);
		operateMenu.add (exitItem);
		jMenuBar.add (operateMenu); 
		conMenu.add (userItem);
		conMenu.add (connectItem);
		jMenuBar.add (conMenu);
		helpMenu.add (helpItem);
		jMenuBar.add (helpMenu); 
		setJMenuBar (jMenuBar);

		//初始化按钮
		loginButton = new JButton("登录");
		logoffButton = new JButton("注销");
		userButton  = new JButton("用户设置" );
		connectButton  = new JButton("连接设置" );
		exitButton = new JButton("退出" );
		//当鼠标放上显示信息
		loginButton.setToolTipText("连接到指定的服务器");
		logoffButton.setToolTipText("与服务器断开连接");
		userButton.setToolTipText("设置用户信息");
		connectButton.setToolTipText("设置所要连接到的服务器信息");
		//将按钮添加到工具栏
		toolBar.add(userButton);
		toolBar.add(connectButton);
		toolBar.addSeparator();//添加分隔栏
		toolBar.add(loginButton);
		toolBar.add(logoffButton);
		toolBar.addSeparator();//添加分隔栏
		toolBar.add(exitButton);
		contentPane.add(toolBar,BorderLayout.NORTH);

		checkbox = new JCheckBox("悄悄话");
		checkbox.setSelected(false);

		actionlist = new JComboBox();
		actionlist.addItem("微笑地");
		actionlist.addItem("高兴地");
		actionlist.addItem("轻轻地");
		actionlist.addItem("生气地");
		actionlist.addItem("小心地");
		actionlist.addItem("静静地");
		actionlist.setSelectedIndex(0);

		//初始时
		loginButton.setEnabled(true);
		logoffButton.setEnabled(false);
		
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
		
		clientMessage = new JTextField(23);
		clientMessage.setEnabled(false);
		clientMessageButton = new JButton();
		clientMessageButton.setText("发送");

		sendToLabel = new JLabel("发送至:");
		express = new JLabel("         表情:   ");
		messageLabel = new JLabel("发送消息:");
		downPanel = new JPanel();
		girdBag = new GridBagLayout();
		downPanel.setLayout(girdBag);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 0;
		girdBagCon.gridwidth = 5;
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
		//girdBagCon.ipadx = 5;
		//girdBagCon.ipady = 5;
		girdBag.setConstraints(sendToLabel,girdBagCon);
		downPanel.add(sendToLabel);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx =1;
		girdBagCon.gridy = 2;
		girdBagCon.anchor = GridBagConstraints.LINE_START;
		girdBag.setConstraints(combobox,girdBagCon);
		downPanel.add(combobox);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx =2;
		girdBagCon.gridy = 2;
		girdBagCon.anchor = GridBagConstraints.LINE_END;
		girdBag.setConstraints(express,girdBagCon);
		downPanel.add(express);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 3;
		girdBagCon.gridy = 2;
		girdBagCon.anchor = GridBagConstraints.LINE_START;
		//girdBagCon.insets = new Insets(1,0,0,0);
		//girdBagCon.ipadx = 5;
		//girdBagCon.ipady = 5;
		girdBag.setConstraints(actionlist,girdBagCon);
		downPanel.add(actionlist);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 4;
		girdBagCon.gridy = 2;
		girdBagCon.insets = new Insets(1,0,0,0);
		//girdBagCon.ipadx = 5;
		//girdBagCon.ipady = 5;
		girdBag.setConstraints(checkbox,girdBagCon);
		downPanel.add(checkbox);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(messageLabel,girdBagCon);
		downPanel.add(messageLabel);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 1;
		girdBagCon.gridy = 3;
		girdBagCon.gridwidth = 3;
		girdBagCon.gridheight = 1;
		girdBag.setConstraints(clientMessage,girdBagCon);
		downPanel.add(clientMessage);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 4;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(clientMessageButton,girdBagCon);
		downPanel.add(clientMessageButton);

		showStatus = new JTextField(35);
		showStatus.setEditable(false);
		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 5;
		girdBagCon.gridwidth = 5;
		girdBag.setConstraints(showStatus,girdBagCon);
		downPanel.add(showStatus);

		contentPane.add(messageScrollPane,BorderLayout.CENTER);
		contentPane.add(downPanel,BorderLayout.SOUTH);
		
		clientListener = new ClientListener(this);
		
		//关闭程序时的操作
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					if(clientListener.type == 1){
						clientListener.DisConnect();
					}
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
