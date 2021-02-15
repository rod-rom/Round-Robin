import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Robin {
    ArrayList<Integer> p = new ArrayList<>(); // pid
    ArrayList<Integer> a = new ArrayList<>(); // arrival
    ArrayList<Integer> b = new ArrayList<>(); // burst
    Queue<Integer> queue = new LinkedList<Integer>();
    String path;
    int q, timer, n, j, queue_count, i;
    int pid[], arrival[], burst[], rtime[], ct[];
    float avgtat = 0, avgwt = 0;

    public Robin(String file, int quantum) {
        this.path = file;
        this.q = quantum;
    }

    public static void main(String[] args) throws Exception {
        Robin robin = new Robin("D:\\School\\Operating Systems\\round_robin\\process.csv", 16);
        robin.readFile();
        robin.convertArrayListToArray();
        robin.sort();
        robin.start();
    }

    public void readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.path))) {
            String line = "";
            br.readLine(); // skips header
            while ((line = br.readLine()) != null) {
                String parts[] = line.split(",");
                int pid = Integer.valueOf(parts[0]);
                int arrival = Integer.valueOf(parts[1]);
                int burst = Integer.valueOf(parts[2]);
                p.add(pid);
                a.add(arrival);
                b.add(burst);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void convertArrayListToArray() {
        this.pid = this.p.stream().mapToInt(i -> i).toArray();
        this.arrival = this.a.stream().mapToInt(i -> i).toArray();
        this.burst = this.b.stream().mapToInt(i -> i).toArray();
        this.rtime = this.burst;
        this.ct = new int[p.size()];
    }

    public void sort() {
        n = p.size();
        for (int i = 0; i < n; i++) { // 0,1,2,3
            for (int j = i; j >= 1; j--) {
                if (arrival[j] < arrival[j - 1]) {
                    int temp = arrival[j - 1];
                    int temp1 = burst[j - 1];
                    arrival[j - 1] = arrival[j];
                    arrival[j] = temp;
                    burst[j - 1] = burst[j];
                    burst[j] = temp1;
                } else if (arrival[j] == arrival[j - 1]) { // if two processes have the same arrival time
                    if (burst[j] < burst[j - 1]) { // then the one with the shorter burst time goes first
                        int temp = arrival[j - 1];
                        int temp1 = burst[j - 1];
                        arrival[j - 1] = arrival[j];
                        arrival[j] = temp;
                        burst[j - 1] = burst[j];
                        burst[j] = temp1;
                    }
                }
            }
        }
    }

    public void addToQueue() {
        while ((arrival[i] <= timer) && (i < p.size())) {   //check if any process has arrived and add it to the ready queue
            queue_count++;
            i++;
            if (i <= 3) {
                queue.add(rtime[i]);
            }
        }
    }

    public void start() {
        boolean processIsBeingExecuted = false;
        int total_time = 0, temp_q = 0, cs = 0, l = 0;
        int currProcess = 0;
        int temp_turnaround[] = new int[4];
        double cpuUtil = 0.0, waiting = 0.0, turnaround = 0.0;
        String throughput;
        queue_count = 0;
        i = 0;
        for (int x : burst) { // find the total_time
            total_time += x;
        }
        timer = arrival[0];
        queue.add(rtime[i]);
        queue_count++;
        while (timer <= total_time) {
            if (processIsBeingExecuted == true || queue_count != 0) {
                if (processIsBeingExecuted == false && temp_q == 0) { // no process is being executed
                    currProcess = queue.peek();
                    queue.remove();
                    cs++; // context switch
                    processIsBeingExecuted = true;
                }
                currProcess--;
                if (currProcess == 0) { // when process is completed, remove from queue and add to completed time array
                    timer++;
                    ct[l++] = timer;
                    processIsBeingExecuted = false;
                    temp_q = 0;
                    queue_count--;
                    if (i < p.size() - 1) {
                        addToQueue();
                    }
                    continue;
                }
                temp_q++;
                if (temp_q == q) { // if process isn't complete but time slice is finished, add back to the queue
                    timer++;
                    temp_q = 0;
                    if (i < p.size() - 1) {
                        addToQueue();
                    }
                    queue.add(currProcess);
                    processIsBeingExecuted = false;
                } else {
                    timer++;
                    if (i < p.size() - 1) {
                        addToQueue();
                    }
                }
            } else {
                timer++;
                if (i < p.size() - 1) {
                    addToQueue();
                }
            }
        }
        // calculations
        for (int x = 0; x < p.size(); x++) {
            temp_turnaround[x] = ct[x] - arrival[x];
            turnaround += ct[x] - arrival[x];
        }
        for (int x = 0; x < p.size(); x++) {
            waiting += temp_turnaround[x] - burst[x];
        }
        cs--;
        cpuUtil = total_time * 100.0 / (total_time + cs);
        throughput = p.size() + "/" + total_time + "+" + cs + " CS";
        turnaround = turnaround / p.size();
        waiting = waiting / p.size();
        System.out.print("CPU Utilization: " + cpuUtil + "%" + "\tThroughput: " + throughput
                + " \tAverage Waiting Time: " + waiting + "\tAverage Turnaround Time: " + turnaround);
    }
}
