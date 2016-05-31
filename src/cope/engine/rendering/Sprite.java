package cope.engine.rendering;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cope.engine.util.BoundingBox;
import cope.engine.util.Vector;


public class Sprite extends ScreenObject 
{
	
	private Bitmap bitmap;
	private Vector position;
	private int width, height;
	private double rotation;
	private BoundingBox box;
	
	public Sprite(Vector position, String fileName) throws IOException {
		width = 0; 
		height = 0;
		int[] components = null;
		
		try {
			fileName = "/" + fileName;
//			URL resource = Bitmap.class.getResource(fileName);
//			if (resource == null) {
//				throw new FileNotFoundException(fileName 
//						+ " is not an accessible resource.");
//			}
			BufferedImage image = ImageIO.read(new File(fileName));
			
			width = image.getWidth();
			height = image.getHeight();
			
			int[] pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
			components = new int[width * height * 4];
			
			for (int i = 0; i < width * height; i++) {
				components[i * 4    ] = (int) ((pixels[i] >> 16) & 0xFF);
				components[i * 4 + 1] = (int) ((pixels[i] >> 8)  & 0xFF);
				components[i * 4 + 2] = (int) ((pixels[i] >> 0)  & 0xFF);
				components[i * 4 + 3] = (int) ((pixels[i] >> 24) & 0xFF);
			}
			
			box = new BoundingBox((int) position.getX(), (int) position.getY(), width, height);
			
			
		} catch (IOException e){
			throw e;
		}
		
		bitmap = new Bitmap(width, height, components);
		this.position = position;
		
		setObject(this);
		
	}
	
	public void translate(Vector dv) {
		position = position.add(dv);
	}
	
	public void setPosition(Vector position) {
		this.position = position;
	}
	
	public void scaleToWidth(int newWidth) {
		float scale = newWidth / width;
		scale(scale);
	}
	
	public void scaleToHeight(int newHeight) {
		float scale = newHeight / height;
		scale(scale);
	}
	
	public void invert() {

		Bitmap newBitmap = new Bitmap(width, height);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				newBitmap.drawPixel(i, j, bitmap.getColourAt(i, j).invert());
		bitmap = newBitmap;
	}
	
	public void scale(float xFactor, float yFactor) {
		Bitmap newBitmap = new Bitmap((int) (width * xFactor), (int) (height * yFactor));
		
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				
				if (xFactor > 1 && yFactor > 1) {
					for (int i_ = 0; i_ < xFactor; i_++)
						for (int j_ = 0; j_ < yFactor; j_++) {
							newBitmap.drawPixel(
									(int) (i * xFactor + i_), 
									(int) (j * yFactor + j_), 
									bitmap.getColourAt(i, j));
							
						}
				}
				
			}
		
		width = (int) (width * xFactor);
		height = (int) (height * yFactor);
		bitmap = newBitmap;
		
		box = box.scale(xFactor, yFactor);
	}
	
	public void scale(float factor) {
		
		Bitmap newBitmap = new Bitmap((int) (width * factor), (int) (height * factor));
		
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				
				if (factor > 1) {
					for (int i_ = 0; i_ < factor; i_++)
						for (int j_ = 0; j_ < factor; j_++) {
							newBitmap.drawPixel(
									(int) (i * factor + i_), 
									(int) (j * factor + j_), 
									bitmap.getColourAt(i, j));
							
						}
				}
				
			}
		
		width = (int) (width * factor);
		height = (int) (height * factor);
		bitmap = newBitmap;
		
		box = box.scale(factor);
		
	}
	
	public void rotateAroundCentre(double theta) {
		
		rotation += theta;
		box = box.rotateAroundCentre(theta);
		
	}
	
	public void rotateAroundPoint(double theta, Vector point) {
		
		rotation -= theta;
		position = position.sub(point).rotate(theta).add(point);
		
	}
	
	/**
	 * @param target
	 */
	public void render(Display display) {

		display.drawBitmap(
				(int) position.getX() - width / 2, 
				(int) position.getY() - height / 2, 
				bitmap);
		
//		float halfWidth = width / 2;
//		float halfHeight = height / 2;
//		
//		Vector[] points = new Vector[] {
//				new Vector( halfWidth,  halfHeight),
//				new Vector( halfWidth, -halfHeight),
//				new Vector(-halfWidth, -halfHeight),
//				new Vector(-halfWidth,  halfHeight)
//		};
//		
//		for (int i = 0; i < points.length; i++) {
//			points[i] = points[i].rotate(rotation);
//			points[i] = points[i].add(position);
//		}
//		
//		points = RenderUtil.sort(points, RenderUtil.ASCENDINGY);
//		
//		double m = (points[1].getY() - points[0].getY()) 
//				/ (points[1].getX() - points[0].getX());
//		
//		for (int j = (int) points[0].getY(); j < points[3].getY(); j++){
//			
//			int a = 0, b = 0;
//			
//			if (j < points[1].getY()) {
//				b = (int) (points[0].getX() + (j - points[0].getY()) / m);
//				a = (int) (points[0].getX() - (j - points[0].getY()) * m);
//			}
//			else if (j < points[2].getY()) {
//				b = (int) (points[1].getX() + m * (points[1].getY() - j));
//				a = (int) (points[0].getX() + m * (points[0].getY() - j));
//			} 
//			else {
//				b = (int) (points[3].getX() + (points[3].getY() - j) * m
//				a = (int) (points[3].getX() - (points[3].getY() - j) / m);
//			}
//			
//			int max, min;
//			if (a > b) { max = a; min = b; }
//			else { max = b; min = a; }
//			
//			for (int i = min; i <= max; i++) {
//				
//				Vector v = new Vector(i, j).sub(position).rotate(-rotation);
//				
//				int sourceX = width - (int) (halfWidth - v.getX());
//				int sourceY = height - (int) (halfHeight - v.getY());
//				
//				if (bitmap.getComponentsAt(sourceX, sourceY)[0] != -1) {
//					
//					target.drawPixel(i, j, new Colour(
//							bitmap.getComponentsAt(sourceX, sourceY)[3],
//							bitmap.getComponentsAt(sourceX, sourceY)[2],
//							bitmap.getComponentsAt(sourceX, sourceY)[1],
//							bitmap.getComponentsAt(sourceX, sourceY)[0])
//						);
//					
//				}
//			}
//		}
		
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	
	public Bitmap getBitmap() { 
		return bitmap;
	}
	
	public BoundingBox getBoundingBox() {
		return box;
	}

}