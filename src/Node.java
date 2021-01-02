
public class Node {
	public int data , mem;
	public Node next;
	public boolean op; // decide if CPU burst its true otherwise false
	public boolean isF; // decide if the process is over = -1
	
	
	public Node() {
		data = 0;
		next = null;
	}

	public Node(int val) {
		data = val;
		next = null;
	}

	public Node(int val, int m , boolean b , boolean q) {
		data = val;
		next = null;
		op = b;
		isF = q;
		mem=m;
	}

}