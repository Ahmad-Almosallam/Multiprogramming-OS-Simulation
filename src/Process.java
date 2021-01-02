

public class Process {

	int PID;
	int burstTime; // Total time spent in the CPU
	int IOTime; // Total time spent in performing IO
	int waitTimeIO; // ?
	int nbOfTimesInCpu; // Number of times it was in the CPU (class)
	int nbOfTimesIO; // Number of times it performed an IO (class)
	int nbOfTimesWaitMem; // Number of times it was waiting for memory
	int nbOfTimesPreempted; // Number of times its preempted (stopped execution because another process
							// replaced it)
	int time_terminate;
	int time_killed;
	int timeLoadInReadyQ; // When it was loaded into the ready queue.
	int Arrival;
	boolean final_state; // True = killed fslse otherwise 
	int sizeInMem;
	int first_mem; // first required memory
	int first_cpu; // first required memory
	double utilization;
	int ALLburstTime;
	int all_mem;
	int add_mem; // additional memory
	int first_req_mem; // first req memory

	pState state = pState.in_job_queue;

	LinkedList l = new LinkedList();

	public Process(int id) {
		PID = id;
	}

}
