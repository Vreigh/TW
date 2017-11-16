import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
 
public class Drawer{
	
	// settings
    private final int MAX_ITER = 570;
    private final double ZOOM = 150;
    private BufferedImage I;
    private final int taskN = 50;
    ExecutorService executorService;
    
    private int getWidth() {
    	return 800;
    }
    
    private int getHeight() {
    	return 600;
    }
 
    public Drawer(ExecutorService executorService) {
        //setBounds(100, 100, 800, 600);
        //setResizable(false);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        this.executorService = executorService;
    }
    
    public void paint() {
    	try {        	
        	ArrayList<ColorFiller> fillers = new ArrayList<ColorFiller>();
        	ArrayList<Future> futures = new ArrayList<Future>();
        	
        	for(int i=0; i<taskN; i++) { // kazde zadanie rysuje kawalek
        		int partH = getHeight() / taskN;
        		int fromH = partH * i;
        		int toH = partH * (i+1);
        		
        		fillers.add(new ColorFiller(I, MAX_ITER, ZOOM, fromH, toH, getWidth()));
        		futures.add(executorService.submit(fillers.get(i)));
        	}
        	
			for(int i=0; i<taskN; i++) {
				futures.get(i).get();
			}
			
			//g.drawImage(I, 0, 0, this);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
