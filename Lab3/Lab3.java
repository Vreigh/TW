import java.util.ArrayList;

public class Lab3 {
	private static final int M = 1000;
	private static final int N = 10;
	
	public static void main(String[] args) {
		ArrayList<Thread> producers = new ArrayList<Thread>();
		ArrayList<Thread> consumers = new ArrayList<Thread>();
		
		Queue queue = new Queue(M);
		for(int i=0; i<N; i++) {
			Consumer consumer = new Consumer(queue, M, i, RandType.NORMAL);
			Producer producer = new Producer(queue, M, i, RandType.NORMAL);
			
			
			consumers.add(new Thread(consumer));
			consumers.get(i).start();
			
			producers.add(new Thread(producer));
			producers.get(i).start();
		}
	}
}
