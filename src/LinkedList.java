
public class LinkedList {
	private Node head;
	private Node current;
	int burst;
	public int sizeInMem;
	int io_burst;
	int id;

	public LinkedList() {
		head = current = null;
	}

	public boolean empty() {
		return head == null;
	}

	public boolean last() {
		return current.next == null;
	}

	public boolean full() {
		return false;
	}

	public void findFirst() {
		current = head;
	}

	public void findNext() {
		current = current.next;
	}

	public int retrieve() {
		return current.data;
	}
	public int retrieve_mem() {
		return current.mem;
	}

	public void update(int val) {
		current.data = val;
	}

	public void insert(int val, int m, boolean op, boolean is) {
		Node tmp;
		if (empty()) {
			head = new Node(val, m, op, is);
			current = head;
		} else {
			tmp = current.next;
			current.next = new Node(val, m, op, is);
			current = current.next;
			current.next = tmp;
		}
	}

	public void remove() {
		if (current == head) {
			head = head.next;
		} else {
			Node tmp = head;

			while (tmp.next != current)
				tmp = tmp.next;

			tmp.next = current.next;
		}

		if (current.next == null)
			current = head;
		else
			current = current.next;
	}

	public int removeFirst() {
		Node n = head;
		if (current == head) {
			head = head.next;
			current = head;
			return n.mem;
		} else {

			head = head.next;
			return n.mem;
		}
	}

	public int performIO() {
		Node n = head;

		while (n != null) {
			if (!n.op)
				break;
			n = n.next;
		}
		
		n.data--;
		int I = IO_burst();
		if(I == 0) {
			removeIO();
			
			return -1;
		}
		return 0;
	}
	
	public int IO_burst() {
		Node n = head;

		while (n != null) {
			if (!n.op)
				break;
			n = n.next;
		}
		
		return n.data;
	}
	
	public void removeIO() {
		
		Node n = head , q = n;
		
		if(!head.op)
			head = head.next;
		
		while (n != null) {
			if (!n.op)
				break;
			q = n;
			n = n.next;
		}
		
		q.next = n.next;
		n.next = null;
		
	}
	
}