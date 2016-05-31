package cope.engine.util;

public class Interpolator 
{
	public interface InterpolationFormula
	{
		public float velocity(float value, float endValue, 
				float elapsedTime, float completionTime);
	}
	
	private float timeAtLastCall;
	private float completionTime, elapsedTime;
	private float endValue, currentValue;
	private boolean interpolate;
	private InterpolationFormula formula;
	
	public Interpolator()
	{
		elapsedTime = 0;
		completionTime = 0;
		endValue = 0;
		interpolate = false;
		
		// By default, the formula interpolates linearly.
		formula = new InterpolationFormula()
		{
			public float velocity(float value, float endValue, 
					float elapsedTime, float completionTime)
			{
				return (endValue - value) / (completionTime - elapsedTime);
			}
		};
	}
	
	public void interpolate(float start, float end, float t)
	{
		interpolate(start, end, t, formula);
	}
	
	public void interpolate(float start, float end, float t, InterpolationFormula formula)
	{
		timeAtLastCall = System.nanoTime() / 1000000000f;
		completionTime = t;
		endValue = end;
		currentValue = start;
		this.formula = formula;
		interpolate = true;
	}
	
	
	public float step()
	{
		if (interpolate)
		{
			float t = System.nanoTime() / 1000000000f;
			float delta = t - timeAtLastCall;
			timeAtLastCall = t;
			
			float dx = delta * formula.velocity(
					currentValue, endValue, elapsedTime, completionTime);
			
			elapsedTime += delta;
			currentValue += dx;
			
			if (elapsedTime >= completionTime)
			{
				interpolate = false;
				currentValue = endValue;
			}
		}
		
		return currentValue;
	}
	
}
