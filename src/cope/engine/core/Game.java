package cope.engine.core;

import cope.engine.rendering.Bitmap;
import cope.engine.rendering.Colour;
import cope.engine.util.BoundingBox;
import cope.engine.util.Vector;

public class Game {
	
	BoundingBox box, box2, test1, test2;
	int cycles = 0;
	
	public Game()
	{
		
		test1 = new BoundingBox(35, 35, 20, 20);
		test1 = test1.rotateAroundCentre(Math.toRadians(30));
		test2 = new BoundingBox(50, 50, 20, 20);
	}
	
	public void update(float delta)
	{
		test2 = test2.rotateAroundCentre(.001f * delta);
		test1 = test1.rotateAroundPoint(new Vector(60, 60), .005f * delta);
	}
	
	public void render(Bitmap target)
	{
		Colour colour;
		if(test1.isCollidingWith(test2, target)) colour = Colour.RED;
		else colour = Colour.WHITE;
		test1.render(target, colour);
		test2.render(target, colour);
	}
	
	public void input()
	{
	}
	
	public void pause()
	{
	}
	
	public void resume()
	{
	}
	
	public void stop()
	{
	}
	
}
