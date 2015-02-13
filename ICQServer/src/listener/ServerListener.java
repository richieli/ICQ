package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import thread.ServerListenThread;

import entity.Node;
import entity.UserLinkList;
import frame.Help;
import frame.MainFrame;
import frame.PortConf;

/**
 * 作者：李强
 * 发送消息监听类
 */

public class ServerListener implements ActionListener {
	
	public static int port = 8888;//服务端的侦听端口
	ServerSocket serverSocket;//服务端Socket
	UserLinkList userLinkList;//用户链表
	ServerListenThread listenThread;//服务器监听用户上下线

	MainFrame mainFrame;
	
	public ServerListener(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		//为菜单栏添加事件监听
		mainFrame.portItem.addActionListener(this);
		mainFrame.startItem.addActionListener(this);
		mainFrame.stopItem.addActionListener(this);
		mainFrame.exitItem.addActionListener(this);
		mainFrame.helpItem.addActionListener(this);
		
		//添加按钮的事件侦听
		mainFrame.portSet.addActionListener(this);
		mainFrame.startServer.addActionListener(this);
		mainFrame.stopServer.addActionListener(this);
		mainFrame.exitButton.addActionListener(this);
		
		//添加系统消息的事件侦听
		mainFrame.sysMessage.addActionListener(this);
		mainFrame.sysMessageButton.addActionListener(this);
	}

	/*
	 * 事件处理
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if (obj == mainFrame.startServer || obj == mainFrame.startItem) { //启动服务端
			startService();
		}
		else if (obj == mainFrame.stopServer || obj == mainFrame.stopItem) { //停止服务端
			int j=mainFrame.showConfirmDialog("真的停止服务吗?","停止服务");
			
			if (j == 1){
				stopService();
			}
		}
		else if (obj == mainFrame.portSet || obj == mainFrame.portItem) { //端口设置
			//调出端口设置的对话框
			PortConf portConf = new PortConf(mainFrame);
			portConf.setVisible(true);
		}
		else if (obj == mainFrame.exitButton || obj == mainFrame.exitItem) { //退出程序
			int j=mainFrame.showConfirmDialog("真的要退出吗?","退出");
			
			if (j == 1){
				stopService();
				System.exit(0);
			}
		}
		else if (obj == mainFrame.helpItem) { //菜单栏中的帮助
			//调出帮助对话框
			Help helpDialog = new Help(mainFrame);
			helpDialog.setVisible(true);
		}
		else if (obj == mainFrame.sysMessage || obj == mainFrame.sysMessageButton) { //发送系统消息
			sendSystemMessage();
		}
	}
	
	/**
	 * 启动服务端
	 */
	public void startService(){
		try{
			serverSocket = new ServerSocket(port,10);
			mainFrame.messageShow.append("服务端已经启动，在"+port+"端口侦听...\n");
			
			mainFrame.startServer.setEnabled(false);
			mainFrame.startItem.setEnabled(false);
			mainFrame.portSet.setEnabled(false);
			mainFrame.portItem.setEnabled(false);

			mainFrame.stopServer .setEnabled(true);
			mainFrame.stopItem .setEnabled(true);
			mainFrame.sysMessage.setEnabled(true);
		}
		catch (Exception e){
			//System.out.println(e);
		}
		
		
		userLinkList = new UserLinkList();
		
		
		
		listenThread = new ServerListenThread(serverSocket,mainFrame.combobox,
				mainFrame.messageShow,mainFrame.showStatus,userLinkList);
		listenThread.start();
	}
	
	/**
	 * 关闭服务端
	 */
	public void stopService(){
		try{
			//向所有人发送服务器关闭的消息
			sendStopToAll();
			listenThread.isStop = true;
			serverSocket.close();
			
			int count = userLinkList.getCount();
			
			int i =0;
			while( i < count){
				Node node = userLinkList.findUser(i);
				
				node.getInput() .close();
				node.getOutput().close();
				node.getSocket().close();
				
				i ++;
			}

			mainFrame.stopServer .setEnabled(false);
			mainFrame.stopItem .setEnabled(false);
			mainFrame.startServer.setEnabled(true);
			mainFrame.startItem.setEnabled(true);
			mainFrame.portSet.setEnabled(true);
			mainFrame.portItem.setEnabled(true);
			mainFrame.sysMessage.setEnabled(false);

			mainFrame.messageShow.append("服务端已经关闭\n");

			mainFrame.combobox.removeAllItems();
			mainFrame.combobox.addItem("所有人");
		}
		catch(Exception e){
			//System.out.println(e);
		}
	}
	
	/**
	 * 向所有人发送服务器关闭的消息
	 */
	public void sendStopToAll(){
		int count = userLinkList.getCount();
		
		int i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i ++;
				continue;
			}
			
			try{
				node.getOutput().writeObject("服务关闭");
				node.getOutput().flush();
			}
			catch (Exception e){
				//System.out.println("$$$"+e);
			}
			
			i++;
		}
	}
	
	/**
	 * 向所有人发送消息
	 */
	public void sendMsgToAll(String msg){
		int count = userLinkList.getCount();//用户总数
		
		int i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i++;
				continue;
			}
			
			try{
				node.getOutput().writeObject("系统信息");
				node.getOutput().flush();
				node.getOutput().writeObject(msg);
				node.getOutput().flush();
			}
			catch (Exception e){
				//System.out.println("@@@"+e);
			}
			
			i++;
		}

		mainFrame.sysMessage.setText("");
	}

	/**
	 * 向客户端用户发送消息
	 */
	public void sendSystemMessage(){
		String toSomebody = mainFrame.combobox.getSelectedItem().toString();
		String message = mainFrame.sysMessage.getText() + "\n";
		
		mainFrame.messageShow.append(message);
		
		//向所有人发送消息
		if(toSomebody.equalsIgnoreCase("所有人")){
			sendMsgToAll(message);
		}
		else{
			//向某个用户发送消息
			Node node = userLinkList.findUser(toSomebody);
			
			try{
				node.getOutput().writeObject("系统信息");
				node.getOutput().flush();
				node.getOutput().writeObject(message);
				node.getOutput().flush();
			}
			catch(Exception e){
				//System.out.println("!!!"+e);
			}
			mainFrame.sysMessage.setText("");//将发送消息栏的消息清空
		}
	}

}
