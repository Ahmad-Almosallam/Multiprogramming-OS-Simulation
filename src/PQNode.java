

public class PQNode {

	public Process data;
	public PQNode next;
	public int burstProcessime, arrival , id;

	public PQNode(Process d, int b, int q , int idd) {
		data = d;
		burstProcessime = b;
		next = null;
		arrival = q;
		id = idd;
	}

}
