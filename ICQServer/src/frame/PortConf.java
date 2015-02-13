package frame;


import java.awt.*;
import javax.swing.border.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

import listener.ServerListener;

/**
 * 生成端口设置对话框的类
 */
public class PortConf extends JDialog {
	JPanel panelPort = new JPanel();
	JButton save = new JButton();
	JButton cancel = new JButton();
	public static JLabel DLGINFO=new JLabel(
		"                              默认端口号为:8888");

	JPanel panelSave = new JPanel();
	JLabel message = new JLabel();

	public static JTextField portNumber ;

	public PortConf(JFrame frame) {
		super(frame, true);
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//设置运行位置，使对话框居中
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width - 400) / 2 + 50,
						(int) (screenSize.height - 600) / 2 + 150);
		this.setResizable(false);
	}

	private void jbInit() throws Exception {
		this.setSize(new Dimension(300, 120));
		this.setTitle("端口设置");
		message.setText("请输入侦听的端口号:");
		portNumber = new JTextField(10);
		portNumber.setText(""+ServerListener.port);
		save.setText("保存");
		cancel.setText("取消");

		panelPort.setLayout(new FlowLayout());
		panelPort.add(message);
		panelPort.add(portNumber);

		panelSave.add(new Label("              "));
		panelSave.add(save);
		panelSave.add(cancel);
		panelSave.add(new Label("              "));

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panelPort, BorderLayout.NORTH);
		contentPane.add(DLGINFO, BorderLayout.CENTER);
		contentPane.add(panelSave, BorderLayout.SOUTH);

		//保存按钮的事件处理
		save.addActionListener(
			new ActionListener() {
				public void actionPerformed (ActionEvent a) {
					int savePort;
					try{
						
						savePort=Integer.parseInt(PortConf.portNumber.getText());

						if(savePort<1 || savePort>65535){
							PortConf.DLGINFO.setText("               侦听端口必须是0-65535之间的整数!");
							PortConf.portNumber.setText("");
							return;
						}
						ServerListener.port = savePort;
						dispose();
					}
					catch(NumberFormatException e){
						PortConf.DLGINFO.setText("                错误的端口号,端口号请填写整数!");
						PortConf.portNumber.setText("");
						return;
					}
				}
			}
		);

		//关闭对话框时的操作
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					DLGINFO.setText("                              默认端口号为:8888");
				}
			}
		);

		//取消按钮的事件处理
		cancel.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					DLGINFO.setText("                              默认端口号为:8888");
					dispose();
				}
			}
		);
	}
}

