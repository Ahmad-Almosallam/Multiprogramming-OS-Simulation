

public class Array<T> {

	private int maxsize;
	private int size;
	private int current;
	private T[] nodes;

	@SuppressWarnings("unchecked")
	public Array(int n) {
		maxsize = n;
		size = 0;
		current = -1;
		nodes = (T[]) new Object[n];

	}

	public boolean empty() {
		return size == 0;

	}

	public int size() {
		return size;
	}

	public boolean full() {
		return size == maxsize;

	}

	public T get(int i) {

		return nodes[i];
	}
	
	public void set(int i , T q) {
		nodes[i] = q;
	}

	public void remove(int i) {

		if (i == size - 1) {
			size--;
			return;
		}

		T[] node = (T[]) new Object[size];
		int j = 0, k = 0;
		while (j < size) {
			if (j == i) {
				j++;
				continue;
			}
			node[k] = nodes[j];
			k++;
			j++;
		}
		size--;
		j = 0;
		while (j < size) {
			nodes[j] = node[j];
			j++;
		}
	}

	public void findFirst() {
		current = 0;

	}

	public void findNext() {
		current++;

	}

	public boolean last() {
		return current == size - 1;

	}

	public T retrieve() {
		return nodes[current];

	}

	public void update(T e) {
		nodes[current] = e;

	}

	public void insert(T e) {

		if (size == maxsize)
			return;
		for (int i = size - 1; i > current; --i) {
			nodes[i + 1] = nodes[i];
		}
		current++;
		nodes[current] = e;
		size++;

	}

	public void remove() {
		for (int i = current + 1; i < size; i++) {
			nodes[i - 1] = nodes[i];
		}
		size--;
		if (size == 0)
			current = -1;
		else if (current == size)
			current = 0;
	}

}