
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.FileWriter; // Import the FileWriter class

public class OS {

	// Job queue has all the jobs in the memory queue
	LinkedQueue jobs = new LinkedQueue();

	// ready queue -> PQueue
	LinkedPQ ready = new LinkedPQ();

	// wating queue
	LinkedQueue wating = new LinkedQueue();
	// finished process
	LinkedQueue fProcess = new LinkedQueue();

	Array<Process> CPU = new Array<Process>(1);

	LinkedQueue IO = new LinkedQueue();
	// system clock in miliseconds.
	int counter = 1;

	// ram size
	double RAM_size = 704; // 1024 - 320 = 704
	// the 85% of the RAM
	double ram = RAM_size * 0.85, ram_15 = RAM_size * 0.15; // ram = 598
	// 200 ms sleep
	int wakeup = 200;
	// number of process
	int nbOfAllProcess, nbOfProcess;

	public void Simulation(String filename, String resultFileName, int n) {

		write(filename, n);
		doMergeSort(jobs);
		longTerm();
		shortTerm();
		writeResult(resultFileName);

	}

	private void shortTerm() {
		Process previous = new Process(0);
		boolean first_itration = false;
		while (true) {
			// wakeup logn term
			if (wakeup == counter) {
				longTerm();
				wakeup += 200;
			}

			if (deadlock())
				kill_process();

			int access_cpu = ready.access_cpu(counter);

			if (access_cpu > 0) {

				Process p = ready.lowest_cpu_burst(access_cpu);
				if (previous.PID != p.PID && first_itration) {

					previous.nbOfTimesInCpu++;
					CPU.remove();
					if (previous.first_cpu != 0) {
						ready.update(previous.PID);
					}
				}

				performIO();
				CPU.insert(p);
				p.first_cpu--;
				p.burstTime++;
				if (p.first_cpu == 0) {
					p.l.removeFirst();
					if (isFinished(p)) {
						p.time_terminate = counter;
						p.final_state = false; // false = terminated
						p.state = pState.TERMINATED;
						ram += p.first_req_mem;

						ram_15 += p.add_mem;
						fProcess.enqueue(p);
						nbOfProcess--;
						isThereEnoughMem();
					} else {
						p.state = pState.watingIO;
						IO.add_id(p, p.PID);
					}
				} else {

					ready.add(p, p.first_cpu, p.Arrival, p.PID);
				}
				first_itration = true;
				previous = p;
			} else {

				performIO();
			}

			if (fProcess.length() == nbOfAllProcess)
				break;

			counter++;

		}

	}

	private void longTerm() {

		int total_of_mem = 0;

		while (jobs.length() != 0) {
			Process p = jobs.peek();
			if (p.first_mem <= ram) {
				total_of_mem += p.first_mem;
				p.first_req_mem = p.first_mem;
				ram = ram - p.first_mem;
				p.sizeInMem = p.first_mem;
				p.state = pState.READY;
				p.timeLoadInReadyQ = counter;
				nbOfProcess++;
				ready.add(jobs.serve(), p.first_cpu, p.Arrival, p.PID);
			} else {
				break;
			}
		}

		RAM_size -= total_of_mem;
	}

	private void performIO() {

		int index = 0, length = IO.length();
		if (length == 0)
			return;
		boolean isFinished = false;
		while (index < length) {
			Process p = IO.serve();
			p.IOTime++;
			int add_to_ready_or_no = p.l.performIO();
			if (add_to_ready_or_no == -1) {

				p.nbOfTimesIO++;
				isFinished = true;
				p.l.findFirst();
				p.first_mem = p.l.retrieve_mem();
				p.first_cpu = p.l.retrieve();
				if (p.first_mem <= ram_15) {
					p.add_mem += p.first_mem;
					ram_15 = ram_15 - p.first_mem;
					p.sizeInMem += p.first_mem;
					p.state = pState.READY;
					ready.add(p, p.first_cpu, p.Arrival, p.PID);

				} else {
					p.state = pState.watingFM;
					p.nbOfTimesWaitMem++;
					wating.enqueue(p);
				}
			}
			if (!isFinished)
				IO.add_id(p, p.PID);

			isFinished = false;
			index++;
		}
	}

	private boolean isFinished(Process p) {
		int c = 0;
		p.l.findFirst();
		c = p.l.retrieve_mem();
		return c <= -1;
	}

	// wating queue to ready if there is enough mem.
	private void isThereEnoughMem() {
		int length = wating.length(), i = 0;

		while (i < length) {

			Process p = wating.serve();
			int mem = p.first_mem;

			if (mem <= ram_15) {
				ram_15 = ram_15 - p.first_mem;
				p.add_mem += p.first_mem;
				p.sizeInMem += p.first_mem;
				p.state = pState.READY;
				ready.add(p, p.first_cpu, p.Arrival, p.PID);

			} else
				wating.enqueue(p);
			i++;
		}

	}

	private boolean deadlock() {

		boolean deadlock = false;
		int length = wating.length();
		int q = length;
		if (q == nbOfProcess)
			deadlock = true;

		return deadlock;

	}

	private Process kill_process() {
		if (wating.length() == 0)
			return null;
		int high = wating.peekOn(0).sizeInMem, length = wating.length(), index = 0;

		for (int i = 1; i < length; i++) {
			int isHigh = wating.peekOn(i).sizeInMem;
			if (high < isHigh) {
				high = isHigh;
				index = i;
			}
		}
		Process p = wating.delete_on_id(index);
		p.final_state = true;
		p.time_killed = counter;
		p.state = pState.KILLED;
		ram += p.first_req_mem;
		ram_15 += p.add_mem;
		fProcess.enqueue(p);
		nbOfProcess--;
		isThereEnoughMem();
		return p;
	}

	private void read(String name) {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(name + ".txt"));
			String line = reader.readLine();
			int j = 1;

			while (true) {
				Process p = new Process(j);
				j++;
				String[] n = line.split(" ");
				int arrival = Integer.parseInt(n[0]);
				p.Arrival = arrival;
				boolean f_mem = true, cpu_burst = true;
				boolean cpu = true, mem = false, io = false;
				for (int i = 1; i < n.length; i++) {
					int bb = Integer.parseInt(n[i]);
					if (cpu) {

						if (cpu_burst) {
							p.first_cpu = bb;
							cpu_burst = false;
						}

						p.ALLburstTime += bb;
						int mem_size = Integer.parseInt(n[i + 1]);
						p.l.insert(bb, mem_size, true, false);
						mem = true;
						cpu = false;
						io = false;
					} else if (mem) {
						if (f_mem) {
							p.first_mem = bb;
							f_mem = false;

						}
						p.all_mem += bb;
						mem = false;
						cpu = false;
						io = true;
					} else if (io) {
						p.l.insert(bb, bb, false, false);
						mem = false;
						cpu = true;
						io = false;
					} else if (bb == -1) {
						p.l.insert(bb, bb, false, true);
					}
				}
				line = reader.readLine();
				jobs.enqueue(p);
				if (line == null)
					break;
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void write(String filename, int n) {
		nbOfAllProcess = n;
		try {
			File myObj = new File(filename + ".txt");
			System.out.println(myObj.getAbsolutePath());
			if (myObj.createNewFile()) {

				FileWriter myWriter = new FileWriter(filename + ".txt");
				for (int i = 0; i < n; i++) {
					int a = getRandomDoubleBetweenRange(1, 80);
					int q = getRandomDoubleBetweenRange(3, 4);
					myWriter.write(a + " ");
					int size = 0, range_mem = 0;
					for (int j = 0; j < q; j++) {
						int cpu = getRandomDoubleBetweenRange(10, 100), mem = getRandomDoubleBetweenRange(5, 200),
								io = getRandomDoubleBetweenRange(5, 20);
						if (j != 0)
							range_mem += mem;

						myWriter.write(cpu + " ");
						myWriter.write(mem + " ");
						myWriter.write(io + " ");
						size += mem;
					}
					int cpu = getRandomDoubleBetweenRange(10, 100),
							mem = getRandomDoubleBetweenRange(5, range_mem) * -1;

					myWriter.write(cpu + " ");
					myWriter.write(mem + " ");
					myWriter.write("-1 \n");
				}
				myWriter.close();
				read(filename);
			} else {
				myObj.delete();
				write(filename, n);
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	private void writeResult(String filename) {
		int n = fProcess.length();
		try {
			File myObj = new File(filename + ".txt");
			if (myObj.createNewFile()) {
				double total_burst = 0;
				FileWriter myWriter = new FileWriter(filename + ".txt");
				for (int i = 0; i < n; i++) {
					Process p = fProcess.serve();
					String final_state = null;
					int t = 0;
					if (p.final_state) {
						final_state = "Killed";
						t = p.time_killed;
					} else {
						final_state = "Terminated";
						t = p.time_terminate;
					}
					total_burst += p.burstTime;
					myWriter.write("Process ID -> " + p.PID + " \r\n" + "Program name -> " + p.PID + " \r\n"
							+ "When it was loaded into the ready queue. -> " + p.timeLoadInReadyQ + " \r\n"
							+ "Number of times it was in the CPU. -> " + p.nbOfTimesInCpu + " \r\n"
							+ "Total time spent in the CPU -> " + p.burstTime + " \r\n"
							+ "Number of times it performed an IO. -> " + p.nbOfTimesIO + " \r\n"
							+ "Total time spent in performing IO -> " + p.IOTime + "  \r\n"
							+ "Number of times it was waiting for memory. -> " + p.nbOfTimesWaitMem + " \r\n"
							+ "Number of times its preempted -> " + p.nbOfTimesPreempted + " \r\n"
							+ "Time it terminated or was killed -> " + t + " \r\n"
							+ "Its final state: Killed or Terminated -> " + final_state + " \r\n");

				}

				double q = (total_burst / counter) * 100;

				myWriter.write("CPU Utilization -> " + q + "% \r\n");
				myWriter.close();

			} else {
				myObj.delete();
				writeResult(filename);
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private static void doMergeSort(LinkedQueue numbers) {
		int middle;
		LinkedQueue left = new LinkedQueue();
		LinkedQueue right = new LinkedQueue();

		if (numbers.length() > 1) {
			middle = numbers.length() / 2;
			// copy the left half of numbers into left.
			for (int i = 0; i < middle; i++)
				left.enqueue(numbers.peekOn(i));

			// copy the right half of numbers into right.
			for (int j = middle; j < numbers.length(); j++)
				right.enqueue(numbers.peekOn(j));

			doMergeSort(left);
			doMergeSort(right);

			merge(numbers, left, right);
		}
	}

	private static void merge(LinkedQueue numbers, LinkedQueue left, LinkedQueue right) {
		// set up a temporary arraylist to build the merge list
		LinkedQueue temp = new LinkedQueue();

		// set up index values for merging the two lists
		int numbersIndex = 0;
		int leftIndex = 0;
		int rightIndex = 0;

		while (leftIndex < left.length() && rightIndex < right.length()) {
			if (left.peekOn(leftIndex).Arrival < right.peekOn(rightIndex).Arrival) {
				numbers.set(numbersIndex, left.peekOn(leftIndex));
				leftIndex++;
			} else {
				numbers.set(numbersIndex, right.peekOn(rightIndex));
				rightIndex++;
			}
			numbersIndex++;
		}

		int tempIndex = 0;
		if (leftIndex >= left.length()) {
			temp = right;
			tempIndex = rightIndex;
		} else {
			temp = left;
			tempIndex = leftIndex;
		}

		for (int i = tempIndex; i < temp.length(); i++) {
			numbers.set(numbersIndex, temp.peekOn(i));
			numbersIndex++;
		}
	}

	private static int getRandomDoubleBetweenRange(int min, int max) {
		int x = (int) (Math.random() * ((max - min) + 1)) + min;
		return x;
	}

}
