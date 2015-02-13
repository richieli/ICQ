package thread;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import entity.Node;
import entity.UserLinkList;

import java.io.*;
import java.net.*;

/*
 * 服务端的侦听类
 */
public class ServerListenThread extends Thread {
	ServerSocket server;
	
	JComboBox combobox;
	JTextArea textarea;
	JTextField textfield;
	UserLinkList userLinkList;//用户链表
	
	Node client;
	ServerReceiveThread recvThread;
	
	public boolean isStop;

	/*
	 * 聊天服务端的用户上线于下线侦听类
	 */
	public ServerListenThread(ServerSocket server,JComboBox combobox,
		JTextArea textarea,JTextField textfield,UserLinkList userLinkList){

		this.server = server;
		this.combobox = combobox;
		this.textarea = textarea;
		this.textfield = textfield;
		this.userLinkList = userLinkList;
		
		isStop = false;
	}
	
	public void run(){
		while(!isStop && !server.isClosed()){
			try{
				client = new Node();
				client.setSocket(server.accept());
				client.setOutput(new ObjectOutputStream(client.getSocket().getOutputStream()));
				client.getOutput().flush();
				client.setInput(new ObjectInputStream(client.getSocket().getInputStream()));
				client.setUserName((String)client.getInput().readObject());
				
				//显示提示信息
				combobox.addItem(client.getUserName());
				userLinkList.addUser(client);
				textarea.append("用户 " + client.getUserName() + " 上线" + "\n");
				textfield.setText("在线用户" + userLinkList.getCount() + "人\n");
				
				recvThread = new ServerReceiveThread(textarea,textfield,
					combobox,client,userLinkList);
				recvThread.start();
			}
			catch(Exception e){
			}
		}
	}
}

