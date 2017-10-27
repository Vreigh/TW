public class Consumer implements Runnable {
	private final int myNumber;
	private final Queue queue;
	private final int M;
	private final Randomer randomer;
	private volatile boolean isRunning = true;
	
	public Consumer(Queue queue, int M, int i, RandType randType) {
		this.queue = queue;
		this.M = M;
		myNumber = i;
		randomer = new Randomer(randType);
	}
	
	
	public void run() {
		while(isRunning) {
			int n = randomer.getNumber(M);
			try {
				if(!queue.take(n)) return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//System.out.println("Consumer " + myNumber + " waited " + time + " to take " + n + " items.");
		}
	}
	
	public void kill() {
		isRunning = false;
	}

}
