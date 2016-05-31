package cope.engine.rendering;

import cope.engine.util.Vector;

public class Bitmap extends ScreenObject 
{
	
	private final int m_width;
	private final int m_height;
	private final int m_components[];
	
	public Bitmap(int width, int height)
	{
		m_width = width;
		m_height = height;
		m_components = new int[width * height * 4];
		
		setObject(this);
	}
	
	public Bitmap(int height, int width, int components[]) {
		m_width = width;
		m_height = height;
		m_components = components;
		
		setObject(this);
	}
	
	public void clear(int shade)
	{
		for (int i = 0; i < m_width; i++)
			for (int j = 0; j < m_height; j++) {
				int index = (i + j * m_width) * 4;
				m_components[index    ] = shade;
				m_components[index + 1] = shade;
				m_components[index + 2] = shade;
				m_components[index + 3] = 255;
			}
	}
	
	public void render(Display display) 
	{
		display.drawBitmap(0, 0, this);
	}
	
	public void drawPixel(int x, int y, Colour colour)
	{
		int index = (x + y * m_width) * 4;
		
		if(index + 3 < m_components.length && index >= 0){
			if(x <= m_width && x >= 0 
					&& y <= m_height && y >= 0){
				m_components[index    ] = colour.getR();
				m_components[index + 1] = colour.getG();
				m_components[index + 2] = colour.getB();
				m_components[index + 3] = colour.getA();
			}
		}
			
	}
	
	public int[] getComponentsAt(int x, int y) 
	{
		int i = (x + y * m_width) * 4;
		
		if (i + 3 < m_components.length && i > 0)
			return new int[] {m_components[i], m_components[i + 1],
				m_components[i + 2], m_components[i + 3]};
		
		return new int[] {-1};
	}
	
	public Colour getColourAt(int x, int y) 
	{
		int i = (x + y * m_width) * 4;
		return new Colour(m_components[i], m_components[i + 1],
				m_components[i + 2], m_components[i + 3]);
	}
	
	public int[] getComponents() 
	{
		return m_components;
	}
	
	public void drawBitmap(int x, int y, Bitmap map) 
	{
		for (int i = x; i < x + map.getWidth(); i++)
			for (int j = y; j < y + map.getHeight(); j++)
				drawPixel(i, j, map.getColourAt(i - x, j - y));
	}
	
	public void drawLine(int x1, int y1, int x2, int y2, 
			Colour colour)
	{
		
		int dx = x1 - x2;
		int dy = y1 - y2;
		
		int steps;
		float xIncrement, yIncrement, x = x1, y = y1;
		
		if(Math.abs(dx) > Math.abs(dy)){
			steps = Math.abs(dx);
		}else{
			steps = Math.abs(dy);
		}
		xIncrement = dx / (float) steps;
		yIncrement = dy / (float) steps;
		
		drawPixel((int)Math.round(x), (int)Math.round(y), colour);
		
		for(int k = 0; k < steps; k++){
			x -= xIncrement;
			y -= yIncrement;
			drawPixel((int)Math.round(x), (int)Math.round(y), colour);
		}
		
	}
	
	public void drawArc(int x1, int y1, int x2, int y2,
			float skew, float height, Colour colour)
	{
		
	}
	
	public void drawCircle(int x, int y, int r, Colour col, boolean fill)
	{
		for(int i = -r; i <= r; i++)
			for(int j = -r; j <= r; j++){
				
				double d = Math.sqrt(i*i + j*j);
				
				if(fill){
					if(Math.round(d) <= r) { 
						drawPixel(x + i, y + j, col); }
				} else {
					if((int)Math.round(d) == r) { 
						drawPixel(x + i, y + j, col); }
				}
			}
	}
	
	public void drawRectangle(int x, int y, int width, int height, Colour col, boolean fill)
	{
		if (!fill) {
			drawLine(x, y, x + width, y, col);
			drawLine(x, y, x, y + height, col);
			drawLine(x + width, y, x + width, y + height, col);
			drawLine(x, y + height, x + width, y + height, col);
		} else {
			for(int i = x; i < x + width; i++)
				for(int j = y; j < y + height; j++)
					drawPixel(i, j, col);
		}
	}
	
	public void drawPolygon(Vector[] points, Colour col){
		for (int i = 0; i < points.length - 1; i++){
			drawLine((int)points[i].getX(), 
					(int) points[i].getY(),
					(int) points[i + 1].getX(), 
					(int) points[i + 1].getY(), 
					col);
		}
		drawLine((int)points[0].getX(), 
				(int)points[0].getY(),
				(int) points[points.length - 1].getX(), 
				(int) points[points.length - 1].getY(), 
				col);
		
	}
	
	public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3,
			Colour col, boolean fill)
	{
		drawLine(x1, y1, x2, y2, col);
		drawLine(x1, y1, x3, y3, col);
		drawLine(x2, y2, x3, y3, col);
		
		if (fill) {
			
			Vector[] points = new Vector[] {
				new Vector(x1, y1),
				new Vector(x2, y2),
				new Vector(x3, y3)
			};
			
			points = RenderUtil.sort(points, RenderUtil.DESCENDINGY);
			
			int yMax = (int) points[0].getY(); 
			int xMax = (int) points[0].getX();
			int yMid = (int) points[1].getY();
			int xMid = (int) points[1].getX();
			int yMin = (int) points[2].getY();
			int xMin = (int) points[2].getX();
			
			float mMaxToMin = 0;
			float mMaxToMid = 0;
			float mMidToMin = 0;
			
			if(xMax - xMin != 0){ mMaxToMin = (yMax - yMin) / (float) (xMax - xMin); }
			if(xMax - xMid != 0){ mMaxToMid = (yMax - yMid) / (float) (xMax - xMid); }
			if(xMid - xMin != 0){ mMidToMin = (yMid - yMin) / (float) (xMid - xMin); }
			
			for(int i = yMin; i <= yMax; i++){
				
				if (i >= yMid) {
					int a = (int) (Math.ceil(xMax + (i - yMax) / mMaxToMid));
					int b = (int) (Math.ceil(xMax + (i - yMax) / mMaxToMin));
					drawLine(a, i, b, i, col);
				}
				else if (i <= yMid) {
					int a = (int) (xMid + (i - yMid) / mMidToMin);
					int b = (int) (xMax + (i - yMax) / mMaxToMin);
					drawLine(a, i, b, i, col);
				}
			}
					
		}
	}
	
	public void copyToByteArray(byte[] m_displayComponents)
	{
		for(int i = 0; i < m_width * m_height; i++){
			m_displayComponents[i*4    ] = (byte) m_components[i * 4 + 3];
			m_displayComponents[i*4 + 1] = (byte) m_components[i * 4 + 2];
			m_displayComponents[i*4 + 2] = (byte) m_components[i * 4 + 1];
			m_displayComponents[i*4 + 3] = (byte) m_components[i * 4    ];
		}
		
}

	public int getWidth() 
	{
		return m_width;
	}

	public int getHeight() 
	{
		return m_height;
	}
	

}

