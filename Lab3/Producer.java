public class Producer implements Runnable {
	private final int myNumber;
	private final Queue queue;
	private final int M;
	private final Randomer randomer;
	private volatile boolean isRunning = true;
	
	public Producer(Queue queue, int M, int i, RandType randType) {
		this.queue = queue;
		this.M = M;
		myNumber = i;
		randomer = new Randomer(randType);
	}
	
	
	public void run() {
		while(isRunning) {
			int n = randomer.getNumber(M);
			try {
				if(!queue.put(n)) return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//System.out.println("Producer " + myNumber + " waited " + time + " to put " + n + " items.");
		}
	}
	
	public void kill() {
		isRunning = false;
	}

}
