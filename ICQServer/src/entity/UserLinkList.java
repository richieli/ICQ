package entity;


/**
 * 用户链表
 */
public class UserLinkList {
	Node root;
	Node pointer;
	int count;
	
	/**
	 * 构造用户链表
	 */
	public UserLinkList() {
		root = new Node();
		root.next = null;
		pointer = null;
		count = 0;
	}
	
	/**
	 * 添加用户
	 */
	public void addUser(Node n) {
		pointer = root;
		
		while(pointer.next != null) {
			pointer = pointer.next;
		}
		
		pointer.next = n;
		n.next = null;
		count++;
		
	}
	
	/**
	 * 删除用户
	 */
	public void delUser(Node n) {
		pointer = root;
		
		while(pointer.next != null) {
			if(pointer.next == n) {
				pointer.next = n.next;
				count --;
				
				break;
			}
			
			pointer = pointer.next;
		}
	}
	
	/**
	 * 返回用户数
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * 根据用户名查找用户
	 */
	public Node findUser(String username) {
		if(count == 0) return null;
		
		pointer = root;
		
		while(pointer.next != null) {
			pointer = pointer.next;
			
			if(pointer.username.equalsIgnoreCase(username)) {
				return pointer;
			}
		}
		
		return null;
	}
	
	/**
	 * 根据索引查找用户
	 */
	public Node findUser(int index) {
		if(count == 0) {
			return null;
		}
		
		if(index <  0) {
			return null;
		}
		
		pointer = root;
		
		int i = 0;
		while(i < index + 1) {
			if(pointer.next != null) {
				pointer = pointer.next;
			}
			else {
				return null;
			}
			
			i++;
		}
		
		return pointer;
	}
}

