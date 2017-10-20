public class Consumer implements Runnable {
	private final int myNumber;
	private final Queue queue;
	private final int M;
	private final Randomer randomer;
	
	public Consumer(Queue queue, int M, int i, RandType randType) {
		this.queue = queue;
		this.M = M;
		myNumber = i;
		randomer = new Randomer(randType);
	}
	
	
	public void run() {
		while(true) {
			int n = randomer.getNumber(M);
			long time = -1;
			try {
				time = queue.take(n);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Consumer " + myNumber + " waited " + time + " to take " + n + " items.");
		}
	}

}
