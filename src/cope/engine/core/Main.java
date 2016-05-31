package cope.engine.core;
import java.io.IOException;

import cope.engine.rendering.Colour;
import cope.engine.rendering.Display;
import cope.engine.util.Interpolator;
import cope.engine.util.Interpolator.InterpolationFormula;


public class Main 
{	
	//TODO: Screens, Transitions, Animations, Fix transformations to use matrices.
	
	public static void main(String[] args) throws IOException
	{	
		Graphing.main(args);
	}
	
	public static void test()
	{
		Display display = new Display(600, 600, "2D Engine");
		
		
//		Sprite sprite;
//		try {
//			sprite = new Sprite(new Vector(300, 300), "res/test.jpg");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		Interpolator xi = new Interpolator();

		int x = 200;
		final float t1 = 1.5f;
		final float t2 = 2f;
		final int n = 2;
		final float xf = 200;
		final float xb = 20;
		final float pi = (float) Math.PI;
		final float k2 = 1;
		
		xi.interpolate(x, x + xf, t2, new InterpolationFormula() {

			@Override
			public float velocity(float value, float endValue,
					float t, float completionTime) {
				
				if (t <= t1)
				{
					float sin = (float) Math.sin(pi * t / t1);
					return 0.5f * pi * (xb + xf) * sin / t1;
				}
				else if (t <= t2)
				{
					float theta = 1.5f * n * pi / (t2 - t1);
					float coef1 = - xb * k2 / ((t + k2) * (t + k2));
					float coef2 = - 1.5f * n * pi * xb* k2 / ((t + k2) * (t2 - t1));
					return (float) (coef1 * Math.cos(theta) + coef2 * Math.sin(theta));
				}

				return 0;
			}
			
		});
		
		while (true)
		{
			display.fill(0);
			
			x = (int) xi.step();
//			sprite.render(display);
			display.getFrameBuffer().drawCircle(x, 300, 15, Colour.RED, true);
			
			display.showBufferStrategy();
		}
	}
	
}