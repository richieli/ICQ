package entity;


import java.net.*;
import java.io.*;

/**
 * 用户链表的结点类
 */
public class Node {
	String username = null;
	Socket socket = null;
	ObjectOutputStream output = null;
	ObjectInputStream input = null;
		
	Node next = null;
	
	public Socket getSocket() {
		return this.socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public String getUserName() {
		return this.username;
	}
	public void setUserName(String name) {
		this.username = name;
	}
	
	public ObjectOutputStream getOutput() {
		return this.output;
	}
	public void setOutput(ObjectOutputStream out) {
		this.output = out;
	}
	
	public ObjectInputStream getInput() {
		return this.input;
	}
	public void setInput(ObjectInputStream in) {
		this.input = in;
	}
}