package thread;


import javax.swing.*;

import entity.Node;
import entity.UserLinkList;

import java.io.*;
import java.net.*;

/*
 * 服务器接收消息的类
 */
public class ServerReceiveThread extends Thread {
	JTextArea textarea;
	JTextField textfield;
	JComboBox combobox;
	Node client;
	UserLinkList userLinkList;//用户链表
	
	public boolean isStop;
	
	public ServerReceiveThread(JTextArea textarea,JTextField textfield,
		JComboBox combobox,Node client,UserLinkList userLinkList){

		this.textarea = textarea;
		this.textfield = textfield;
		this.client = client;
		this.userLinkList = userLinkList;
		this.combobox = combobox;
		
		isStop = false;
	}
	
	public void run(){
		//向所有人发送用户的列表
		sendUserList();
		
		while(!isStop && !client.getSocket().isClosed()){
			try{
				String type = (String)client.getInput().readObject();
				
				if(type.equalsIgnoreCase("聊天信息")){
					String toSomebody = (String)client.getInput().readObject();
					String status  = (String)client.getInput().readObject();
					String action  = (String)client.getInput().readObject();
					String message = (String)client.getInput().readObject();
					
					String msg = client.getUserName()
							+" "+ action
							+ "对 "
							+ toSomebody 
							+ " 说 : "
							+ message
							+ "\n";
					if(status.equalsIgnoreCase("悄悄话")){
						msg = " [悄悄话] " + msg;
					}
					textarea.append(msg);
					
					if(toSomebody.equalsIgnoreCase("所有人")){
						sendToAll(msg);//向所有人发送消息
					}
					else{
						try{
							client.getOutput().writeObject("聊天信息");
							client.getOutput().flush();
							client.getOutput().writeObject(msg);
							client.getOutput().flush();
						}
						catch (Exception e){
							//System.out.println("###"+e);
						}
						
						Node node = userLinkList.findUser(toSomebody);
						
						if(node != null){
							node.getOutput().writeObject("聊天信息"); 
							node.getOutput().flush();
							node.getOutput().writeObject(msg);
							node.getOutput().flush();
						}
					}
				}
				else if(type.equalsIgnoreCase("用户下线")){
					Node node = userLinkList.findUser(client.getUserName());
					userLinkList.delUser(node);
					
					String msg = "用户 " + client.getUserName() + " 下线\n";
					int count = userLinkList.getCount();

					combobox.removeAllItems();
					combobox.addItem("所有人");
					int i = 0;
					while(i < count){
						node = userLinkList.findUser(i);
						if(node == null) {
							i ++;
							continue;
						} 
			
						combobox.addItem(node.getUserName());
						i++;
					}
					combobox.setSelectedIndex(0);

					textarea.append(msg);
					textfield.setText("在线用户" + userLinkList.getCount() + "人\n");
					
					sendToAll(msg);//向所有人发送消息
					sendUserList();//重新发送用户列表,刷新
					
					break;
				}
			}
			catch (Exception e){
				//System.out.println(e);
			}
		}
	}
	
	/*
	 * 向所有人发送消息
	 */
	public void sendToAll(String msg){
		int count = userLinkList.getCount();
		
		int i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i ++;
				continue;
			}
			
			try{
				node.getOutput().writeObject("聊天信息");
				node.getOutput().flush();
				node.getOutput().writeObject(msg);
				node.getOutput().flush();
			}
			catch (Exception e){
				//System.out.println(e);
			}
			
			i++;
		}
	}
	
	/*
	 * 向所有人发送用户的列表
	 */
	public void sendUserList(){
		String userlist = "";
		int count = userLinkList.getCount();

		int i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i ++;
				continue;
			}
			
			userlist += node.getUserName();
			userlist += '\n';
			i++;
		}
		
		i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i ++;
				continue;
			} 
			
			try{
				node.getOutput().writeObject("用户列表");
				node.getOutput().flush();
				node.getOutput().writeObject(userlist);
				node.getOutput().flush();
			}
			catch (Exception e){
				//System.out.println(e);
			}
			i++;
		}
	}
}
