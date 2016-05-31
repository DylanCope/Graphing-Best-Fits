package cope.engine.util;

import cope.engine.rendering.Bitmap;
import cope.engine.rendering.Colour;

public class BoundingBox {
	
	private Vector m_position;
	private double m_rotation;
	private Vector[] m_points;
	
	public BoundingBox(int x, int y, int width, int height)
	{
		m_position = new Vector(x, y);
		m_rotation = 0;
		
		m_points = new Vector[] {
			new Vector(width / 2, height / 2), new Vector(width / 2, - height / 2),
			new Vector(- width / 2, - height / 2), new Vector(- width / 2, height / 2)	
		};
	}
	
	private BoundingBox(Vector position, Vector[] points, double rotation)
	{
		m_position = position;
		m_rotation = rotation;
		m_points = points;
	}
	
	public BoundingBox rotateAroundCentre(double theta)
	{
		double r = m_rotation + theta;
		Vector[] newPoints = new Vector[4];
		
		for (int i = 0; i < 4; i++)
			newPoints[i] = m_points[i].rotate(theta);
		
		return new BoundingBox(m_position, newPoints, r);
		
	}
	
	public BoundingBox rotateAroundPoint(Vector point, double theta)
	{
		Vector[] newPoints = rotateAroundCentre(theta).getPoints();
		
		Vector v = m_position
				.sub(point)
				.rotate(theta)
				.add(point);
		
		return new BoundingBox(v, newPoints, theta);
	}
	
	public BoundingBox scale(float factor) {
		Vector[] newPoints = new Vector[4];
		
		for (int i = 0; i < 4; i++) 
			newPoints[i] = m_points[i].mul(factor);
		
		return new BoundingBox(m_position, newPoints, m_rotation);
	}
	
	public BoundingBox scale(float xFactor, float yFactor) {
		Vector[] newPoints = new Vector[4];
		
		Vector factorVector = new Vector(xFactor, yFactor);
		
		for (int i = 0; i < 4; i++)
			newPoints[i] = m_points[i].mul(factorVector);
		
		return new BoundingBox(m_position, newPoints, m_rotation);
	}
	
	public BoundingBox translate(Vector dv) {
		Vector[] newPoints = new Vector[4];
		
		for (int i = 0; i < 4; i++) 
			newPoints[i] = m_points[i].add(dv);
		
		return new BoundingBox(m_position.add(dv), newPoints, m_rotation);
	}
	
	public void render(Bitmap target, Colour colour)
	{	
		Vector[] screenPoints = new Vector[] {
				m_points[0].add(m_position), m_points[1].add(m_position),
				m_points[2].add(m_position), m_points[3].add(m_position)
		};
		
		target.drawPolygon(screenPoints, colour);
	}
	
	public boolean isCollidingWith(BoundingBox b, Bitmap target)
	{	
		boolean isColliding = false;
		
		BoundingBox boxA = rotateAroundCentre(-m_rotation);
		BoundingBox boxB = b.rotateAroundPoint(m_position, -m_rotation);
		
		Vector p0 = boxA.getPoints()[0].add(boxA.getCentre());
		Vector p1 = boxA.getPoints()[1].add(boxA.getCentre());
		Vector p2 = boxA.getPoints()[2].add(boxA.getCentre());
		
		for(int i = 0; i < 4; i++){
			
			float x = boxB.getPoints()[i].add(boxB.getCentre()).getX();
			float y = boxB.getPoints()[i].add(boxB.getCentre()).getY();
			
			if (x >= p1.getX() && x <= p2.getX())
				if (y >= p2.getY() && y <= p0.getY())
					return true;
		}

		boxA = b.rotateAroundCentre(-b.getRotation());
		boxB = rotateAroundPoint(b.getCentre(), -b.getRotation());
		
		p0 = boxA.getPoints()[0].add(boxA.getCentre());
		p1 = boxA.getPoints()[1].add(boxA.getCentre());
		p2 = boxA.getPoints()[2].add(boxA.getCentre());
		
		for(int i = 0; i < 4; i++){
			float x = boxB.getPoints()[i].add(boxB.getCentre()).getX();
			float y = boxB.getPoints()[i].add(boxB.getCentre()).getY();
			if (x >= p2.getX() && x <= p1.getX())
				if (y >= p1.getY() && y <= p0.getY())
					return true;
		}
		
		return isColliding;
	}
	
	public boolean isBounding(Vector point) {
		BoundingBox box = rotateAroundCentre(-m_rotation);
		
		Vector p0 = box.getPoints()[0];
		Vector p1 = box.getPoints()[1];
		Vector p2 = box.getPoints()[2];
		
		Vector v = point.sub(m_position).rotate(-m_rotation);
		
		if (p2.getX() <= v.getX() && v.getX() <= p1.getX() &&
				p1.getY() <= v.getY() && v.getY() <= p0.getY())
			return true;
		
		return false;
	}
	
	public void setPosition(Vector position)
	{
		m_position = position;
	}
	
	public Vector[] getPoints() { return m_points; }
	public Vector   getCentre() { return m_position; }
	public double	  getRotation() { return m_rotation; }
	
	public String toString() {
		return "" + getPoints()[0] + ", " + getPoints()[1] + ", " + getPoints()[2] + "";
	}
}
