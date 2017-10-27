import javafx.scene.chart.XYChart;

public class DataGatherer {
	private double avarages[];
	private int tries[];
	private int M;
	
	public DataGatherer(int M) {
		this.M = M;
		avarages = new double[M];
		tries = new int[M];
		java.util.Arrays.fill(tries, 0);
	}
	
	public void push(int n, double val) {
		if(tries[n - 1] == 0) {
			tries[n - 1] = 1;
			avarages[n - 1] = val;
		}else {
			avarages[n - 1] = (avarages[n - 1] * tries[n - 1] + val) / ++tries[n - 1];
		}
	}
	
	public void fillData(XYChart.Series series) {
		for(int i=0; i<M; i++) {
			if(tries[i] != 0) series.getData().add(new XYChart.Data(i+1, avarages[i]));
		}
	}

}
