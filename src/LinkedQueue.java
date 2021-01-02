

public class LinkedQueue {
	private int size;
	private QNode head;
	private QNode tail;

	public LinkedQueue() {
		head = tail = null;
		size = 0;

	}

	public int length() {
		return size;
	}

	public boolean full() {
		return false;
	}

	public void enqueue(Process e) {
		if (tail == null) {
			head = tail = new QNode(e);
		} else {
			tail.next = new QNode(e);
			tail = tail.next;
		}
		size++;

	}

	public void add_id(Process e, int id) {

		if (tail == null) {
			head = tail = new QNode(e);
		} else {
			if (!isProcesshere(id)) {
				QNode s = new QNode(e, id);
				tail.next = s;
				tail = tail.next;

			}
		}
		size++;
	}

	public boolean isProcesshere(int id) {
		if (tail == null)
			return true;
		QNode n = head;
		int q = n.id;
		while (n != null) {
			q = n.id;
			if (q == id) {
				return true;
			}

			n = n.next;

		}
		return false;
	}

	public Process serve() {
		Process x = head.data;
		head = head.next;
		size--;
		if (size == 0)
			tail = null;
		return x;
	}

	public Process peek() {
		Process e = head.data;
		return e;
	}

	public Process peekOn(int i) {

		QNode n = head;
		int q = 0;
		while (n != null) {
			if (q == i) {
				return n.data;
			}
			q++;
			n = n.next;
		}
		return null;
	}
	
	public void set(int i , Process p) {
		QNode n = head;
		int q = 0;
		while (n != null) {
			if (q == i) {
				break;
			}
			q++;
			n = n.next;
		}
		
		n.data = p;
	}

	public Process delete_on_id(int i) {

		QNode n = head;
		Process e = null;
		QNode q = null;

		if (i == 0) {
			Process p = serve();
			return p;
		} else if (i == length() - 1) {

			for (int j = 0; j < length(); j++) {
				if (i == j) {
					break;
				}
				q = n;
				n = n.next;
			}
			q.next = n.next;
			n.next = null;
			tail = q;
			size--;

			return n.data;
		}

		else {
			for (int j = 0; j < length(); j++) {
				if (i == j) {
					break;
				}
				q = n;
				n = n.next;
			}
			q.next = n.next;
			n.next = null;
			size--;

			if (size == 0 || i == length())
				tail = null;

			return n.data;
		}

	}

}
