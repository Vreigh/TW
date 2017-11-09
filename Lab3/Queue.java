
public interface Queue {
	public void close();
	public boolean put(int n) throws InterruptedException;
	public boolean take(int n) throws InterruptedException;
	public void createDataFile(String name);

}
