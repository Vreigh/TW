import java.util.Random;

public class Randomer {
	private Random r = new Random();
	private RandType rt;
	
	public Randomer(RandType rt) {
		this.rt = rt;
	}
	
	private int getNumberNormal(int M) {
		return r.nextInt(M) + 1;
	}
	
	private int getNumberSmallBetter(int M) {
		while(true) {
			int n = r.nextInt(M) + 1;
			int reject = n*100 / M; // im wieksza wzglednie liczba, tym wieksza szanca na odrzucenie
			int confirm = r.nextInt(100) + 1;
			if(confirm >= reject) return n;
		}
	}
	
	public int getNumber(int M) {
		if(rt == RandType.NORMAL) {
			return getNumberNormal(M);
		}else return getNumberSmallBetter(M);
	}
	
	
}
