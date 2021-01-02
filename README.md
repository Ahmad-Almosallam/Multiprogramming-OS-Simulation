# Multiprogramming-OS-Simulation


## Aim of the project
This project simulates the behavior of the multiprogramming operating system and use
CPU scheduler, and CPU Execution. At the end of the simulation, it’s expected to output
some statistics regarding the behavior of the system.
### Hardware
The computer hardware is assumed to have:
1. A RAM of size 1GB, where 320MB is used to store the OS.
2. A single core CPU that executes one instruction each unit of time.
3. An I/O device for input and output operations.
4. An internal clock that allows to measure time in milliseconds.


## Output from the simulation
A text file containing statistics about all processes and their final status TERMINATED
or KILLED. Statistics about a process should contain:

a. Process ID

b. When it was loaded into the ready queue.

c. Number of times it was in the CPU.

d. Total time spent in the CPU

e. Number of times it performed an IO.

f. Total time spent in performing IO

g. Number of times it was waiting for memory.

h. Number of times its preempted (stopped execution because another process
replaced it)

i. Time it terminated or was killed

j. Its final state: Killed or Terminated

k. CPU Utilization
