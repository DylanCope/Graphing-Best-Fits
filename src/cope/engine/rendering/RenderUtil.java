package cope.engine.rendering;

import cope.engine.util.Vector;

public class RenderUtil {

	public static final int DESCENDINGX = 0;
	public static final int ASCENDINGX = 1;
	public static final int DESCENDINGY = 2;
	public static final int ASCENDINGY = 3;
	
	public static Vector[] sort(Vector[] points, int type) {
		
		if (type == DESCENDINGX) {
			
			int swaps = 1;
			
			while (swaps > 0) {
				swaps = 0;
				for (int i = 1; i < points.length; i++) {
					if (points[i].getX() > points[i - 1].getX()) {
						Vector temp = points[i - 1];
						points[i - 1] = points[i];
						points[i] = temp;
						swaps++;
					}
				}
			}
			
		} else if (type == ASCENDINGX) {
			
			int swaps = 1;
			
			while (swaps > 0) {
				swaps = 0;
				for (int i = 1; i < points.length; i++) {
					if (points[i].getX() < points[i - 1].getX()) {
						Vector temp = points[i - 1];
						points[i - 1] = points[i];
						points[i] = temp;
						swaps++;
					}
				}
			}
			
		} else if (type == DESCENDINGY) {
			
			int swaps = 1;
			
			while (swaps > 0) {
				swaps = 0;
				for (int i = 1; i < points.length; i++) {
					if (points[i].getY() > points[i - 1].getY()) {
						Vector temp = points[i - 1];
						points[i - 1] = points[i];
						points[i] = temp;
						swaps++;
					}
				}
			}
			
		} else if (type == ASCENDINGY) {
			
			int swaps = 1;
			
			while (swaps > 0) {
				swaps = 0;
				for (int i = 1; i < points.length; i++) {
					if (points[i].getY() < points[i - 1].getY()) {
						Vector temp = points[i - 1];
						points[i - 1] = points[i];
						points[i] = temp;
						swaps++;
					}
				}
			}
			
		}
		
		return points;
	}
}
