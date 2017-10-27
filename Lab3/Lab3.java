import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Lab3 extends Application {
	private static final int M = 1000;
	private static final int N = 10;
	
	@Override public void start(Stage stage) throws InterruptedException {
		ArrayList<Thread> producersThreads = new ArrayList<Thread>();
		ArrayList<Thread> consumersThreads = new ArrayList<Thread>();
		
		ArrayList<Producer> producers = new ArrayList<Producer>();
		ArrayList<Consumer> consumers = new ArrayList<Consumer>();
		
		Queue queue = new Queue(M);
		for(int i=0; i<N; i++) {
			consumers.add(new Consumer(queue, M, i, RandType.NORMAL));
			producers.add(new Producer(queue, M, i, RandType.NORMAL));
			
			producersThreads.add(new Thread(producers.get(i)));
			producersThreads.get(i).start();
			
			consumersThreads.add(new Thread(consumers.get(i)));
			consumersThreads.get(i).start();
		}
		
		
        stage.setTitle("TW Lab3 Analysis");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("wielkosc porcji");
        yAxis.setLabel("Czas");
        
        final ScatterChart<Number,Number> chart = 
                new ScatterChart<Number,Number>(xAxis,yAxis);     
        chart.setTitle("Czas oczekiwania w zaleznosci od wielkosci porcji");
        
        XYChart.Series prodSeries = new XYChart.Series();
        prodSeries.setName("Producers");
        
        XYChart.Series conSeries = new XYChart.Series();
        conSeries.setName("Consumers");
        
        TimeUnit.SECONDS.sleep(5);
        
        queue.close();
        
        for(int i=0;i<N; i++) {
			consumersThreads.get(i).join();  
			producersThreads.get(i).join();
        }
        
        System.out.println("Post threads control");
        
        queue.fillData(prodSeries, conSeries);

        Scene scene  = new Scene(chart,1400,600);
        chart.getData().addAll(prodSeries, conSeries);
       
        stage.setScene(scene);
        stage.show();
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
