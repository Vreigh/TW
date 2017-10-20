import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Queue {
	private final int M;
	private int m = 0;
	private final Lock lock = new ReentrantLock();
	private final Condition lack = lock.newCondition();
	private final Condition over = lock.newCondition();
	
	public Queue(int M) {
		this.M = M;
	}
	
	private int left() {
		return 2*M - m;
	}
	
	public long put(int n) throws InterruptedException{
		long start, end;
		
		start = System.nanoTime();
		lock.lock();
		while(left() < n) {
			over.await();
		}
		end = System.nanoTime();
		
		m += n;
		lack.signal();
		lock.unlock();
		return end - start;
	}
	
	public long take(int n) throws InterruptedException{
		long start, end;
		
		start = System.nanoTime();
		lock.lock();
		while(m < n) {
			lack.await();
		}
		end = System.nanoTime();
		
		m -= n;
		over.signal();
		lock.unlock();
		return end - start;
	}
	
	
}
