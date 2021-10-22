# Round-Robin: CPU Scheduling Algorithm:

## How Round Robin works:
- First In First Out (FIFO) circular queue:
  -starts from the head all the way to the tail goes back to the head and
  -checks if the process is complete if not continue going.
- One of two scenerios: 
    - 1. If CPU burst time for a process is less than 'q' time the process will release the CPU voluntarily.Then the CPU scheduler will then proceed to the next process in the ready queue
      -ex: if process is 3 sec and q=5 the process will leave after 3 sec and not stay for the whole 5 sec
    - 2. If the CPU burst time for a process is longer than 'q' time the timer will go off and will cause an interrupt to the OS. Then a context switch will be executed and the process will be put at the tail of the ready queue after the CPU scheduler will select the next process in the ready queue
CPU SCHEDULER IS PICKING THE HEAD OF THE QUEUE
### Implementation:
- The Robin object is initialized and takes two parameters, the path to the CSV file and the time
slice.
- The file is read and placed into an arraylist, the reason being an arraylist could change in size
and adjust to any # of processes.
- There is an arraylist for the pid, arrival, and burst.
- The arraylist is converted to array because it is easier to work with when it comes to sorting.
- The arrival[] array is sorted from smallest to largest. If two arrival times are the same, the burst
times are compared, the shorter one goes first.
- Their burst times are also moved according to their arrival time.
- Then the round robin scheduling begins.
- The total time is calculated and the first process is added to the queue. More specifically, their
burst times.
- After every loop the burst time is reduced and inspected to see if the process is completed or
the time quantum is completed.
- The addToQueue() method checks to see if a process has arrived and is added to the ready
queue.
- A process is removed from the queue and given to the CPU.
- Once the timer/clock reaches the total_time(20 in this case) the loop is exited and the
performance is calculated.

### Trials:
| Time quantum | CPU Utilization | Throughput | Average Waiting Time | Average Turnaround |
| ----------- | ----------------- | --------- | -------------------- | ------------------- |
| 2 | 66.66666666666667% | 4/20+10 CS |  8.25 | 13.25 | 
| 4 | 76.92307692307692% | 4/20+6 CS | 8.0 | 13.0 |
| 6 | 83.33333333333333% | 4/20+4 CS | 6.25 | 11.25 |
| 8 | 86.95652173913044% | 4/20+3 CS | 5.0 | 10.0 |
| 10 | 86.95652173913044% | 4/20+3 CS | 5.0 | 10.0 |
| 12 | 86.95652173913044% | 4/20+3 CS | 5.0 | 10.0 |
| 14 | 86.95652173913044% | 4/20+3 CS | 5.0 | 10.0 |

### Analysis:
As you can see after q=8 the results are negligible and show no signs of improvement and
performance plateaus. CPU Utilization is at its maximum at q=8 which means less overhead
and more work is being done on the processor. Less context switches are performed which is
what we want because it means less overhead. Before q=8 many context switches are done
which is not a good sign because it results in a higher average turnaround time, average waiting
time and lower CPU Utilization. We want the opposite of that for full performance from the CPU.
To conclude the best quantum time q=8. It has the best performance and least overhead.
