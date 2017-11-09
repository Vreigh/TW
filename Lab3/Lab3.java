import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Lab3 {
	private static final String QUEUE_TYPE = "Fair";
	private static final int M = 100000; // wielkosc porcji
	private static final int N = 1000; // liczba watkow
	private static final RandType RAND_TYPE = RandType.SMALLMORE;
	
	private static final String HOME = "/home/filip/Desktop/TW/Lab3/Data";
	
	public static void main(String[] args) throws InterruptedException {
		
		ArrayList<Thread> producersThreads = new ArrayList<Thread>();
		ArrayList<Thread> consumersThreads = new ArrayList<Thread>();
		
		ArrayList<Producer> producers = new ArrayList<Producer>();
		ArrayList<Consumer> consumers = new ArrayList<Consumer>();
		
		Queue queue;
		if(QUEUE_TYPE.equals("Naive")) {
			queue = new NaiveQueue(M);
		}else if(QUEUE_TYPE.equals("Fair")){
			queue = new FairQueue(M);
		}else {System.out.println("Argument error"); return;}
		
		for(int i=0; i<N; i++) {
			consumers.add(new Consumer(queue, M, i, RAND_TYPE));
			producers.add(new Producer(queue, M, i, RAND_TYPE));
			
			producersThreads.add(new Thread(producers.get(i)));
			producersThreads.get(i).start();
			
			consumersThreads.add(new Thread(consumers.get(i)));
			consumersThreads.get(i).start();
		}
		
		
        TimeUnit.SECONDS.sleep(10);
        
        queue.close();
        
        for(int i=0;i<N; i++) {
			consumersThreads.get(i).join();  
			producersThreads.get(i).join();
        }
        
        System.out.println(String.format("%s/%s/N:%d_M:%d_R:%s.csv", HOME, QUEUE_TYPE, N, M, RAND_TYPE));
        queue.createDataFile(String.format("%s/%s/N:%d_M:%d_R:%s.csv", HOME, QUEUE_TYPE, N, M, RAND_TYPE));
	}
}
