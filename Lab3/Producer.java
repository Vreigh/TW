public class Producer implements Runnable {
	private final int myNumber;
	private final Queue queue;
	private final int M;
	private final Randomer randomer;
	
	public Producer(Queue queue, int M, int i, RandType randType) {
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
				time = queue.put(n);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Producer " + myNumber + " waited " + time + " to put " + n + " items.");
		}
	}

}
