package cope.engine.core;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import cope.engine.input.Input;
import cope.engine.rendering.Colour;
import cope.engine.rendering.Display;
import cope.engine.util.MathUtil;
import cope.engine.util.Vector;
import cope.engine.rendering.TextObject;

public class Graphing 
{
	public static SortedSet<Vector> xOrderedPoints, angleOrderedPoints;
	public static ArrayList<Vector> enterOrderedPoints;
	public static int w = 600, h = 600;
	public static int unitLength, halfW, halfH;
	public static float[] cartesianEqn, polarEqn, roots;
	public static float[][] parametricEqn;
	public static Colour curveColour = new Colour(240, 225, 17);
	public static int showWhichCurve = 0;
	public static TextObject cartesianEqnText, parametricEqnText;
	public static int polynomialDeg = 1;
	public static boolean showCurve = true;

	public static void main(String[] args)
	{
		Display display = new Display(w, h, "Graphing");
		halfH = h / 2;
		halfW = w / 2;
		unitLength = halfW / 2;
		
		xOrderedPoints = new TreeSet<Vector>(new Comparator<Vector>() 
		{
			@Override
			public int compare(Vector v1, Vector v2) {
				return (int) (100*v1.getX() - 100*v2.getX());
			}
		});
		angleOrderedPoints = new TreeSet<Vector>(new Comparator<Vector>() 
		{
			@Override
			public int compare(Vector v1, Vector v2) {
				return (int) (100*v1.getAngle() - 100*v2.getAngle());
			}
		});
		enterOrderedPoints = new ArrayList<Vector>();
		
		cartesianEqnText = new TextObject();
		cartesianEqnText.setColour(curveColour);
		cartesianEqnText.setText("asdf");
		cartesianEqnText.setPosition(new Vector(halfW, halfH));
		
		while (true)
		{
			display.fill(0);

			drawGrid(display, unitLength / 10, Colour.DARKGREY);
			drawGrid(display, unitLength, Colour.GREY);
			drawGrid(display, halfW, Colour.WHITE);
			
			for (Vector v : xOrderedPoints) {
				Vector p = toScreenCoords(v);
				display.getFrameBuffer().drawCircle(
						(int) p.getX(), (int) p.getY(), 2, Colour.RED, true);
			}
			
			if (showWhichCurve == 0 && showCurve)
				drawCartesianEqn(display);	
			else if (showWhichCurve == 1)
				drawParametricEqn(display, -10, 10, 0.01f);
			else if (showWhichCurve == 2)
				drawPolarEqn(display, 0.01f);
			
			cartesianEqnText.render(display);
			
			drawRoots(display);
			
			display.showBufferStrategy();
			
			while (handleInput(display.getInput())) {}
		}
	}
	
	public static void polynomialOfBestFit()
	{
		cartesianEqn = MathUtil.leastSquaresMethod(polynomialDeg, enterOrderedPoints);
		ArrayList<Vector> changesInSign = 
				MathUtil.findChangesInSign(cartesianEqn, -10, 10, 0.01f);
		roots = MathUtil.newtonRhapson(cartesianEqn, 100, changesInSign);
	}
	
	public static boolean handleInput(Input in)
	{
		if (in.isLeftMousePressed())
		{
			Vector p = toGraphCoords(in.getMousePosition());
			xOrderedPoints.add(p);
			enterOrderedPoints.add(p);
			angleOrderedPoints.add(p.getPolar());
//			System.out.println(enterOrderedPoints.size());
			
//			cartesianEqn = MathUtil.getCartesianEqn(xOrderedPoints);
//			parametricEqn = MathUtil.getParametricEqn(enterOrderedPoints);
//			polarEqn = MathUtil.getCartesianEqn(angleOrderedPoints);

			polynomialOfBestFit();
//			System.out.println(MathUtil.eqnToString(cartesianEqn, "y", "x"));
			
			cartesianEqnText.setText(MathUtil.eqnToString(cartesianEqn, "y", "x"));
			
			return false;
		}
		else if (in.getKey(KeyEvent.VK_SPACE)) {
			showCurve = !showCurve;
			return false;
		}
		else if (in.getKey(KeyEvent.VK_A)) {
			polynomialDeg = 1;
			polynomialOfBestFit();
			return false;
		}
		else if (in.getKey(KeyEvent.VK_S)) {
			polynomialDeg = 2;
			polynomialOfBestFit();
			return false;
		}
		else if (in.getKey(KeyEvent.VK_D)) {
			polynomialDeg = 3;
			polynomialOfBestFit();
			return false;
		}
		else if (in.getKey(KeyEvent.VK_F)) {
			polynomialDeg = 4;
			polynomialOfBestFit();
			return false;
		}
		else if (in.getKey(KeyEvent.VK_G)) {
			polynomialDeg = 6;
			polynomialOfBestFit();
			return false;
		}
		else if (in.getKey(KeyEvent.VK_Q)) {
			showWhichCurve = 0;
			return false;
		}
		else if (in.getKey(KeyEvent.VK_W)) {
			showWhichCurve = 1;
			return false;
		}
		else if (in.getKey(KeyEvent.VK_E)) {
			showWhichCurve = 2;
			return false;
		}
		if (in.getKey(KeyEvent.VK_R)) {
			xOrderedPoints.clear();
			angleOrderedPoints.clear();
			enterOrderedPoints.clear();
			cartesianEqn = null;
			parametricEqn = null;
			polarEqn = null;
			roots = null;
			return false;
		}
		return true;
	}
	
	public static void printRoots()
	{
		ArrayList<Vector> changesInSign = MathUtil.findChangesInSign(cartesianEqn, 0, w, 1);
		float[] roots = MathUtil.newtonRhapson(cartesianEqn, 50, changesInSign);
		for (float r : roots)
			System.out.println(r);
		System.out.println();
	}
	
	public static void drawRoots(Display display)
	{
		if (roots != null)
			for (float r : roots)
			{
				Vector p = toScreenCoords(new Vector(r, 0));
				display.getFrameBuffer().drawCircle(
						(int) p.getX(), (int) p.getY(), 2, Colour.CYAN, true
				);
			}
	}
	
	public static void drawGrid(Display display, int length, Colour col)
	{
		for (int i = -halfW; i < halfW; i += length) {
			display.getFrameBuffer().drawLine(
					halfW + i, 0, halfW + i, h, col
			);
		}
		for (int i = -halfH; i < halfH; i += length) {
			display.getFrameBuffer().drawLine(
					0, halfH + i, w, halfH + i, col
			);
		}
	}
	
	public static void drawPolarEqn(Display display, float s)
	{
		if (polarEqn == null)
			return;
		
		Vector prev = null;
		for (float t = 0; t < 2*Math.PI; t += s)
		{
			float r = MathUtil.evaluateEqn(polarEqn, t);
			Vector v = toScreenCoords(new Vector(
					(float) (r*Math.cos(t)), 
					(float) (r*Math.sin(t))));
			if (prev == null)
				prev = v;
			else {
				display.getFrameBuffer().drawLine(
						(int) v.getX(), (int) v.getY(),
						(int) prev.getX(), (int) prev.getY(), 
						curveColour);
				prev = v;
			}
		}
	}
	
	public static void drawParametricEqn(Display display, float i, float f, float s)
	{
		if (parametricEqn == null)
			return;
		
		Vector prev = null;
		for (float t = i; t <= f; t += s)
		{
			float x = MathUtil.evaluateEqn(parametricEqn[0], t);
			float y = MathUtil.evaluateEqn(parametricEqn[1], t);
			Vector v = toScreenCoords(new Vector(x, y));
			if (prev == null)
				prev = v;
			else {
				display.getFrameBuffer().drawLine(
						(int) v.getX(), (int) v.getY(),
						(int) prev.getX(), (int) prev.getY(), 
						curveColour);
				prev = v;
			}
		}
	}
	
	public static void drawCartesianEqn(Display display)
	{
		if (cartesianEqn != null)
			for (int i = 0; i < w; i++)
				display.getFrameBuffer().drawLine(
						i, evalEqn(cartesianEqn, i), i+1, 
						evalEqn(cartesianEqn, i+1), 
						curveColour);
	}
	
	public static int evalEqn(float[] eqn, int i)
	{
		float x = (i - halfW) / (float) unitLength;
		float f = MathUtil.evaluateEqn(eqn, x);
		return halfH + (int) (unitLength * f);
	}
	
	public static Vector toGraphCoords(Vector v)
	{
		return new Vector(
			(v.getX() - halfW) / (float) unitLength,
			(v.getY() - halfH) / (float) unitLength
		);
	}
	
	public static Vector toScreenCoords(Vector v)
	{
		return new Vector(
				halfW + v.getX()*unitLength,
				halfH + v.getY()*unitLength
		);
	}
	
}