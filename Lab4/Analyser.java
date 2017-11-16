import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
 
//newSingleThreadExecutor
//newFixedThreadPool
//newCachedThreadPool
//newWorkStealingPool
// newScheduledThreadPool
 
public class Analyser extends Application {
 
    @Override public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Liczba w¹tków w puli");
        //creating the chart
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle("Scheduled Thread Pool");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Czas w milisekundach");
        //populating the series with data
        for(int i=1; i<200; i++) {
        	Drawer drawer = new Drawer(Executors.newScheduledThreadPool(i));
        	long start = System.currentTimeMillis();
        	drawer.paint();
        	long end = System.currentTimeMillis() - start;
        	series.getData().add(new XYChart.Data(i, end));
        }
        
        
        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().addAll(series);
       
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
    	launch(args);
    }
}
