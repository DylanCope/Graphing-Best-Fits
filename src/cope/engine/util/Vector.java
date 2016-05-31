package cope.engine.util;

import cope.engine.util.Matrix;


public class Vector {
	
	private float values[];
	
	public Vector(float x, float y) {
		values = new float[2];
		values[0] = x;
		values[1] = y;
	}
	
	public Vector(float x, float y, float z) {
		values = new float[3];
		values[0] = x;
		values[1] = y;
		values[2] = z;
	}
	
	public Vector(float values[]) {
		this.values = values;
	}
	
	public Vector transform(Matrix transformation)
	{
		if (values.length == transformation.getWidth()) {
			Matrix v = new Matrix(1, values.length);
			for (int i = 0; i < values.length; i++)
				v.set(0, i, values[i]);
			
			v = transformation.mul(v);
			float newValues[] = new float[values.length];
			for (int i = 0; i < values.length; i++)
				newValues[i] = v.get(0, i);
			
			return new Vector(newValues);
				
		}
		else
			return null;
	}
	
	public float len(){
		float sum = 0;
		for (int i = 0; i < values.length; i++)
			sum += values[i] * values[i];
		
		return (float) Math.sqrt(sum);
	}
	
	public float[] getValues() {
		return values;
	}
	
	public float getDotProduct(Vector b){
		float dot = 0;
		for (int i = 0; i < values.length; i++)
			dot += values[i] + b.getValues()[i];
		
		return dot;
	}
	
	public Vector getNormalised(){
		float newValues[] = new float[values.length];
		float len = len();
		
		for (int i = 0; i < values.length; i++)
			newValues[i] = values[i] / len;
		
		return new Vector(newValues);
	}
	
	public Vector getPerpendicular() {
		if (values.length == 2)
			return new Vector(-values[1], -values[0]);
		else
			return null;
	}
	
	public Vector getPolar() {
		float angle = (float) getAngle();
		if (getX() < 0)
			angle += Math.PI;
		else if (getY() < 0)
			angle += 2*Math.PI;
		
		if (values.length == 2)
			return new Vector(angle, len());
		return null;
	}
	
	public Vector getCrossProduct3f(Vector b) {
		if (values.length == 3)
			return new Vector(
					values[1] * b.getValues()[2] - values[2] * b.getValues()[1],
					values[2] * b.getValues()[0] - values[0] * b.getValues()[2],
					values[0] * b.getValues()[1] - values[1] * b.getValues()[0]);
		
		else return null;
	}
	
	public float getCrossProduct2f(Vector b) {
		if (values.length == 2)
			return values[0] * b.getValues()[1] - values[1] * b.getValues()[0];
		
		else return 0;
	}
	
	public float getDYDX() {
		if (values.length == 2)
			return values[1] / values[0];
		
		else return 0;
	}
	
	public float getDYDX(Vector point) {
		if (values.length == 2)
			return (values[1] - point.getValues()[1]) / (values[0] - point.getValues()[0]);
		
		else return 0;
	}
	
	public Vector rotate(double theta) {
		/*
		double cos = Math.cos(theta);
		double sin = Math.sin(theta);
		
		return new Vector(
				(float) (values[0] * cos - values[1] * sin), 
				(float) (values[0] * sin + values[1] * cos));
		*/
		return transform(Matrix.get2DRotationalMatrix(theta));
	}
	
	public double getAngle() {
		return Math.atan(values[1] / values[0]);
	}
	
	public Vector add(Vector a){ 
		float newValues[] = new float[values.length];
		for (int i = 0; i < values.length; i++)
			newValues[i] = values[i] + a.getValues()[i];
		
		return new Vector(newValues);
	}
	
	public Vector sub(Vector a){  
		float newValues[] = new float[values.length];
		for (int i = 0; i < values.length; i++)
			newValues[i] = values[i] - a.getValues()[i];
		
		return new Vector(newValues);
	}
	
	public Vector mul(Vector a){  
		float newValues[] = new float[values.length];
		for (int i = 0; i < values.length; i++)
			newValues[i] = values[i] * a.getValues()[i];
		
		return new Vector(newValues);
	}
	
	public Vector div(Vector a){  
		float newValues[] = new float[values.length];
		for (int i = 0; i < values.length; i++)
			newValues[i] = values[i] / a.getValues()[i];
		
		return new Vector(newValues);
	}
	
	public Vector add(float a){ 
		float newValues[] = new float[values.length];
		for (int i = 0; i < values.length; i++)
			newValues[i] = values[i] + a;
		
		return new Vector(newValues);
	}
	
	public Vector mul(float a){
		float newValues[] = new float[values.length];
		for (int i = 0; i < values.length; i++)
			newValues[i] = values[i] * a;
		
		return new Vector(newValues);
	}
	
	public String toString(){
		return "("+ values[0] +", "+ values[1] + ")";
	}

	public float getX() {
		return values[0];
	}

	public void setX(float x) {
		this.values[0] = x;
	}

	public float getY() {
		return values[1];
	}

	public void set(float x, float y) {
		values[0] = x;
		values[1] = y;
	}
	
	public void set(float x, float y, float z) {
		values[0] = x;
		values[1] = y;
		values[2] = z;
	}
	
	public void set(float values[]) {
		this.values = values;
	}
	
	public void setY(float y) {
		this.values[1] = y;
	}
	
	public float getZ() {
		return values[2];
	}
	
	public void setZ(float z) {
		this.values[2] = z;
	}
}
