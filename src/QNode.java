

public class QNode {
	public Process data , mem;
	int id;
	public QNode next;
	public boolean op; // decide if CPU burst its true otherwise false
	public boolean isF; // decide if the process is over = -1
	
	
	public QNode() {
		data = null;
		next = null;
	}

	public QNode(Process val) {
		data = val;
		next = null;
	}
	public QNode(Process val , int id) {
		data = val;
		this.id = id;
		next = null;
	}

	public QNode(Process val, Process m , boolean b , boolean q) {
		data = val;
		next = null;
		op = b;
		isF = q;
		mem=m;
	}
}
