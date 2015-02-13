package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import thread.ClientReceiveThread;

import frame.ConnectConf;
import frame.Help;
import frame.MainFrame;
import frame.UserConf;

/*
 * 客户端监听类
 */

public class ClientListener implements ActionListener {

	String ip = "127.0.0.1";//连接到服务端的ip地址
	int port = 8888;//连接到服务端的端口号
	public String userName = "李强";//用户名
	public int type = 0;//0表示未连接，1表示已连接
	
	Socket socket;
	ObjectOutputStream output;//网络套接字输出流
	ObjectInputStream input;//网络套接字输入流
	
	ClientReceiveThread recvThread;
	
	MainFrame mainFrame;
	
	public ClientListener(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		//为菜单栏添加事件监听
		mainFrame.loginItem.addActionListener(this);
		mainFrame.logoffItem.addActionListener(this);
		mainFrame.exitItem.addActionListener(this);
		mainFrame.userItem.addActionListener(this);
		mainFrame.connectItem.addActionListener(this);
		mainFrame.helpItem.addActionListener(this);
		
		//添加按钮的事件侦听
		mainFrame.loginButton.addActionListener(this);
		mainFrame.logoffButton.addActionListener(this);
		mainFrame.userButton.addActionListener(this);
		mainFrame.connectButton.addActionListener(this);
		mainFrame.exitButton.addActionListener(this);
		
		//添加系统消息的事件侦听
		mainFrame.clientMessage.addActionListener(this);
		mainFrame.clientMessageButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		
		if (obj == mainFrame.userItem || obj == mainFrame.userButton) { //用户信息设置
			//调出用户信息设置对话框
			UserConf userConf = new UserConf(mainFrame,userName);
			userConf.setVisible(true);
			userName = userConf.userInputName;
		}
		else if (obj == mainFrame.connectItem || obj == mainFrame.connectButton) { //连接服务器设置
        //调出连接设置对话框
			ConnectConf conConf = new ConnectConf(mainFrame,ip,port);
			conConf.setVisible(true);
			ip = conConf.userInputIp;
			port = conConf.userInputPort;
		}
		else if (obj == mainFrame.loginItem || obj == mainFrame.loginButton) { //登录
			Connect();
		}
		else if (obj == mainFrame.logoffItem || obj == mainFrame.logoffButton) { //注销
			DisConnect();
			mainFrame.showStatus.setText("");
		}
		else if (obj == mainFrame.clientMessage || obj == mainFrame.clientMessageButton) { //发送消息
			SendMessage();
			mainFrame.clientMessage.setText("");
		}
		else if (obj == mainFrame.exitButton || obj == mainFrame.exitItem) { //退出
			int j=mainFrame.showConfirmDialog("真的要退出吗?","退出");
			
			if (j == 1){
				if(type == 1){
					DisConnect();
				}
				System.exit(0);
			}
		}
		else if (obj == mainFrame.helpItem) { //菜单栏中的帮助
			//调出帮助对话框
			Help helpDialog = new Help(mainFrame);
			helpDialog.setVisible(true);
		} 
	}
	
	public void Connect(){
		try{
			socket = new Socket(ip,port);
		}
		catch (Exception e){
			mainFrame.showConfirmDialog("不能连接到指定的服务器。\n请确认连接设置是否正确。","提示");
			return;
		}

		try{
			output = new ObjectOutputStream(socket.getOutputStream());
			output.flush();
			input  = new ObjectInputStream(socket.getInputStream() );
			
			output.writeObject(userName);
			output.flush();
			
			recvThread = new ClientReceiveThread(socket,output,input,mainFrame.combobox,mainFrame.messageShow,mainFrame.showStatus);
			recvThread.start();
			
			mainFrame.loginButton.setEnabled(false);
			mainFrame.loginItem.setEnabled(false);
			mainFrame.userButton.setEnabled(false);
			mainFrame.userItem.setEnabled(false);
			mainFrame.connectButton.setEnabled(false);
			mainFrame.connectItem.setEnabled(false);
			mainFrame.logoffButton.setEnabled(true);
			mainFrame.logoffItem.setEnabled(true);
			mainFrame.clientMessage.setEnabled(true);
			mainFrame.messageShow.append("连接服务器 "+ip+":"+port+" 成功...\n");
			type = 1;//标志位设为已连接
		}
		catch (Exception e){
			System.out.println(e);
			return;
		}
	}
	
	public void DisConnect(){
		mainFrame.loginButton.setEnabled(true);
		mainFrame.loginItem.setEnabled(true);
		mainFrame.userButton.setEnabled(true);
		mainFrame.userItem.setEnabled(true);
		mainFrame.connectButton.setEnabled(true);
		mainFrame.connectItem.setEnabled(true);
		mainFrame.logoffButton.setEnabled(false);
		mainFrame.logoffItem.setEnabled(false);
		mainFrame.clientMessage.setEnabled(false);
		
		if(socket.isClosed()){
			return ;
		}
		
		try{
			output.writeObject("用户下线");
			output.flush();
		
			input.close();
			output.close();
			socket.close();
			mainFrame.messageShow.append("已经与服务器断开连接...\n");
			type = 0;//标志位设为未连接
		}
		catch (Exception e){
			//
		}
	}
	
	public void SendMessage(){
		String toSomebody = mainFrame.combobox.getSelectedItem().toString();
		String status  = "";
		if(mainFrame.checkbox.isSelected()){
			status = "悄悄话";
		}
		
		String action = mainFrame.actionlist.getSelectedItem().toString();
		String message = mainFrame.clientMessage.getText();
		
		if(socket.isClosed()){
			return ;
		}
		
		try{
			output.writeObject("聊天信息");
			output.flush();
			output.writeObject(toSomebody);
			output.flush();
			output.writeObject(status);
			output.flush();
			output.writeObject(action);
			output.flush();
			output.writeObject(message);
			output.flush();
		}
		catch (Exception e){
			//
		}
	}


}
