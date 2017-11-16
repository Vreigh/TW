import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

public class ColorFiller implements Runnable {
	private final BufferedImage I;
	private final int MAX_ITER;
	private final double ZOOM;
	private final int fromH;
	private final int toH;
	private final int W;
	
	public ColorFiller(BufferedImage I, int MAX_ITER, double ZOOM, int fromH, int toH, int W) {
		this.I = I;
		this.MAX_ITER = MAX_ITER;
		this.ZOOM = ZOOM;
		this.fromH = fromH;
		this.toH = toH;
		this.W = W;
	}
	
	public void run() {
		double zx, zy, cX, cY, tmp;
		
		for (int y = fromH; y < toH; y++) {
            for (int x = 0; x < W; x++) {
                zx = zy = 0;
                cX = (x - 400) / ZOOM;
                cY = (y - 300) / ZOOM;
                int iter = MAX_ITER;
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                I.setRGB(x, y, iter | (iter << 8));
            }
        }
	}

}
