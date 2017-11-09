import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class NaiveQueue implements Queue {
	private final int M;
	private int m = 0;
	private final Lock lock = new ReentrantLock();
	private final Condition lack = lock.newCondition();
	private final Condition over = lock.newCondition();
	
	private final DataGatherer consumersGatherer;
	private final DataGatherer producersGatherer;
	
	private volatile boolean isOpen = true;
	
	public NaiveQueue(int M) {
		this.M = M;
		consumersGatherer = new DataGatherer(M);
		producersGatherer = new DataGatherer(M);
	}
	
	private int leftSpace() {
		return 2*M - m;
	}
	
	public void close() {
		isOpen = false;
	}
	
	public boolean put(int n) throws InterruptedException{
		long start, end;
		
		start = System.nanoTime();
		lock.lock();
		while(leftSpace() < n) {
			if(!isOpen) {
				lack.signal();
				over.signal();
				lock.unlock();
				return false;
			}
			over.await();
		}
		end = System.nanoTime();
		
		producersGatherer.push(n, end - start);
		m += n;
		//System.out.println("dzialam");
		
		lack.signal();
		lock.unlock();
		return true;
	}
	
	public boolean take(int n) throws InterruptedException{
		long start, end;
		
		start = System.nanoTime();
		lock.lock();
		while(m < n) {
			if(!isOpen) {
				lack.signal();
				over.signal();
				lock.unlock();
				
				return false;
			}
			lack.await();
		}
		end = System.nanoTime();
		
		consumersGatherer.push(n, end - start);
		m -= n;
		//System.out.println("dzialam");
		
		over.signal();
		lock.unlock();
		return true;
	}
	

	public void createDataFile(String name) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(name));
			String title = "\"Size\",\"Avg\",\"Type\"\n";
			writer.write(title);
			consumersGatherer.fillData(writer, "\"C\"");
			producersGatherer.fillData(writer, "\"P\"");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
