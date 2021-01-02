
import java.util.Comparator;

public class LinkedPQ {
	private int size;
	private PQNode head;

	/* tail is of no use here. */
	public LinkedPQ() {
		head = null;
		size = 0;
	}

	public int length() {
		return size;
	}

	public boolean full() {
		return false;
	}

	public void add(Process data, int burst, int arr, int id) {

		PQNode tmp = new PQNode(data, burst, arr, id);
		if ((size == 0) || (arr < head.arrival)) {
			tmp.next = head;
			head = tmp;
		} else {
			PQNode p = head;
			PQNode q = null;
			while ((p != null) && (arr >= p.arrival)) {
				q = p;
				p = p.next;
			}
			tmp.next = p;
			q.next = tmp;
		}
		size++;
	}

	public PQNode serve() {
		PQNode node = head;
		head = head.next;
		size--;
		return node;
	}

	public Process serve2() {
		PQNode node = head;
		head = head.next;
		size--;
		return node.data;
	}

	public Process lowest_cpu_burst(int index) {

		if (index == 1) {
			PQNode low = head;
			delete_on_id(head.id);

			return low.data;
		}

		PQNode low = head;
		PQNode n = head;

		for (int i = 0; i < index; i++) {
			if (low.burstProcessime > n.burstProcessime)
				low = n;
			n = n.next;
		}

		delete_on_id(low.id);

		return low.data;

	}

	public int access_cpu(int sys_clock) {

		int c = 0;

		PQNode n = head;

		for (int i = 0; i < length(); i++) {
			if (n.arrival <= sys_clock)
				c++;
			n = n.next;
		}

		return c;
	}
	
	public Process peekOn(int i) {

		PQNode n = head;
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

	public boolean update(int id) {
		if (length() == 0)
			return false;
		PQNode n = head, w = head;
		;
		int q = n.id;
		while (n != null) {
			q = n.id;
			if (q == id) {
				break;
			}
			w = n;
			n = n.next;
			
		}
		if (n != null)
			n.data.nbOfTimesPreempted++;
		return true;
	}

	public Process delete_on_id(int id) {

		PQNode n = head;

		PQNode q = null;

		if (head.id == id) {

			q = serve();
			return q.data;

		} else {
			for (int i = 0; i < length(); i++) {

				if (n.id == id) {

					break;
				}
				q = n;
				n = n.next;
			}
			q.next = n.next;
			n.next = null;
			size--;
			return n.data;
		}

	}

}
