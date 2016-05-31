package cope.engine.util;

import java.util.ArrayList;
import java.util.Collection;

public class MathUtil 
{
	
	/** Calculate an array of vectors, where each pair of numbers represents a range of the
	 * independent variable where there is change in sign of the dependent variable. */
	public static ArrayList<Vector> findChangesInSign(float[] eqn, float start, float end, float step)
	{
		ArrayList<Vector> changes = new ArrayList<Vector>();
		
		for (float i = start; i < end; i += step) {
			float y1 = evaluateEqn(eqn, i);
			float y2 = evaluateEqn(eqn, i - step);
			if ((y1 <= 0 && y2 > 0) || (y2 <= 0 && y1 > 0)) {
				changes.add(new Vector(i, i - step));
			}
		}
		return changes;
	}
	
	/** Find the real roots of an equation using the Newton-Rhapson method. */
	public static float[] newtonRhapson(float[] eqn, int iterations, ArrayList<Vector> changesInSign)
	{
		float roots[] = new float[changesInSign.size()];
		float x;
		float[] diffEqn = differentiateEqn(eqn);
		for (int i = 0; i < roots.length; i++) {
			x = changesInSign.get(i).getX();
			for (int j = 0; j < iterations; j++) {
				if (evaluateEqn(diffEqn, x) != 0)
					x = x - evaluateEqn(eqn, x) / evaluateEqn(diffEqn, x);
			}
			roots[i] = x;
		}
		
		return roots;
	}

	/** Differentiate an array of polynomial coefficients. */
	public static float[] differentiateEqn(float[] eqn)
	{
		float newEqn[] = new float[eqn.length - 1];
		
		for (int i = 0; i < newEqn.length; i++) {
			/* Implementing d(cx^n)/dx = cnx^(n-1) 
			 * the index of the coef indicates the power x.
			 * */
			newEqn[i] = (i + 1) * eqn[i + 1];
		}
		
		return newEqn;
	}
	
	/** Integrate an array of polynomial coefficients. 
	 * (The first element in the return array is the constant of integration
	 * which is defaulted to 0 and needs to be defined later). */
	public static float[] integrateEqn(float[] eqn)
	{
		float newEqn[] = new float[eqn.length + 1];
		newEqn[0] = 0;
		for (int i = 1; i < newEqn.length; i++) {
			/* Implementing S(cx^n)dx = cx^(n+1)/(n+1) + c 
			 * the index of the eqn indicates the power x.
			 * */
			newEqn[i] = eqn[i - 1] / (float) i;
		}
		
		return newEqn;
	}
	
	/** Find the constant of integration assuming the inputed array
	 * of coefficients has the first value set to 0. */
	public static float findC(float[] eqn, float x0, float y0)
	{
		return y0 - evaluateEqn(eqn, x0);
	}
	
	public static float evaluateEqn(float[] eqn, float x)
	{
		float y = 0;
		
		for (int i = 0; i < eqn.length; i++)
			y += eqn[i] * Math.pow(x, i);
			
		return y;
	}
	
	public static float[] addEqns(float[] eqn1, float[] eqn2)
	{
		//Making eqn1 have the longer length.
		if (eqn1.length < eqn2.length) {
			float temp[] = eqn2;
			eqn2 = eqn1;
			eqn1 = temp;
		}
		
		float[] newEqn = new float[eqn1.length];
		 
		for (int i = 0; i < eqn1.length; i++) {
			if (i < eqn2.length)
				newEqn[i] = eqn1[i] + eqn2[i];
			else
				newEqn[i] = eqn1[i];
		}
		
		return newEqn;
	}
	
	/** Returns an array of coefficients representing a Cartesian equation going
	 * through the inputed points. */
	public static float[] getCartesianEqn(Collection<Vector> points)
	{
		int size = points.size();
		float[] eqn = new float[size];
		
		if (size >= 2) 
		{
			float mValues[][] = new float[size][size];
			
			for (int i = 0; i < size; i++) {
				int j = 0;
				for (Vector p : points) {
					mValues[i][j] = (float) Math.pow(p.getX(), i);
					j++;
				}
			}
			
			Matrix M = new Matrix(size, size, mValues);
			
			float yValues[][] = new float[1][size];
			int i = 0;
			for (Vector p : points) {
				yValues[0][i] = p.getY();
				i++;
			}
			
			Matrix Y = new Matrix(1, size, yValues);
			
			Matrix C = M.getInverse().mul(Y);
			eqn = C.getValues()[0];
			
		}
		
		return eqn;
		
	}
	
	/** Returns an array of two arrays of coefficients representing 
	 * a Parametric equation going through the inputed points. */
	public static float[][] getParametricEqn(ArrayList<Vector> points)
	{
		int size = points.size();
		float[][] eqn = new float[2][size];
		
		if (size >= 2) {
			float mValues[][] = new float[size][size];
			
			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++) {
					mValues[i][j] = (float) Math.pow(j, i);
				}
			
			Matrix M = new Matrix(size, size, mValues);
			
			float yValues[][] = new float[1][size];
			for (int i = 0; i < size; i++)
				yValues[0][i] = points.get(i).getY();
			
			Matrix Y = new Matrix(1, size, yValues);
			
			float xValues[][] = new float[1][size];
			for (int i = 0; i < size; i++)
				xValues[0][i] = points.get(i).getX();
			
			Matrix X = new Matrix(1, size, xValues);
			
			Matrix inverse = M.getInverse();
			Matrix cY = inverse.mul(Y);
			eqn[1] = cY.getValues()[0];
			
			Matrix cX = inverse.mul(X);
			eqn[0] = cX.getValues()[0];
		}
		
		return eqn;
	}

	public static float[] leastSquaresMethod(int p, Collection<Vector> dataPoints)
	{
		int n = dataPoints.size();
		
		float[][] valsM = new float[p+1][n];
		float[][] valsY = new float[1][n];
		
		int i = 0;
		for (Vector v : dataPoints) {
			valsY[0][i] = v.getY();
			for (int j = 0; j <= p; j++)
				valsM[j][i] = (float) Math.pow(v.getX(), j);
			i++;
		}
		
		Matrix X = new Matrix(p+1, n, valsM);
		Matrix y = new Matrix(1, n, valsY);
		
		Matrix XT = X.getTranspose();
		
		return 	(XT.mul(X).getInverse()).mul(XT).mul(y).getValues()[0];
	}

	public static String eqnToString(float[] eqn, String dependent, String independant) {
		String str = dependent + " = ";
		String sign;
		for (int i = eqn.length - 1; i >= 0; i--) {
			if (eqn[i] < 0)
				sign = " - ";
			else if (i == eqn.length - 1)
				sign = "";
			else
				sign = " + ";
			
			if (i == 0) {
				independant = "";
			}
			
			if (i <= 1)
				str += sign + Math.abs(eqn[i]) + independant;
			else
				str += sign + Math.abs(eqn[i]) + independant + "^" + i;
		}

		return str;
	}
}
