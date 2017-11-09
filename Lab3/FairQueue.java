import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairQueue implements Queue{
	
	private final int M;
	private int m = 0;
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition prod = lock.newCondition();
	private final Condition prodFirst = lock.newCondition();
	private final Condition con = lock.newCondition();
	private final Condition conFirst = lock.newCondition();
	
	private final DataGatherer consumersGatherer;
	private final DataGatherer producersGatherer;
	
	private volatile boolean isOpen = true;
	
	public FairQueue(int M) {
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
	
	private boolean unlockAll() {
		con.signal();
		prod.signal();
		prodFirst.signal();
		conFirst.signal();
		lock.unlock();
		return false;
	}
	
	public boolean put(int n) throws InterruptedException{
		long start, end;
		
		start = System.nanoTime();
		lock.lock();
		while(lock.hasWaiters(prodFirst)) {
			prod.await();
		}
		// jestem juz 1 w kolejce producentów
		while(leftSpace() < n) {
			if(!isOpen) {
				return unlockAll();
			}
			prodFirst.await();
		}
		end = System.nanoTime();
		
		producersGatherer.push(n, end - start);
		m += n;
		
		conFirst.signal();
		prod.signal();
		lock.unlock();
		return true;
	}
	
	public boolean take(int n) throws InterruptedException{
		long start, end;
		
		start = System.nanoTime();
		lock.lock();
		while(lock.hasWaiters(conFirst)) {
			con.await();
		}
		while(m < n) {
			if(!isOpen) {
				return unlockAll();
			}
			conFirst.await();
		}
		end = System.nanoTime();
		
		consumersGatherer.push(n, end - start);
		m -= n;
		
		prodFirst.signal();
		con.signal();
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
