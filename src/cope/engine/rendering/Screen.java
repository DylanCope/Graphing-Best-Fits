package cope.engine.rendering;

import java.util.ArrayList;

import cope.engine.input.Button;
import cope.engine.input.InputManager;

public class Screen {
	
	ArrayList<Sprite> sprites;
	ArrayList<Bitmap> bitmaps;
	ArrayList<Button> buttons;
	ArrayList<InputManager> inputManagers;
	ArrayList<ScreenObject> screenObjects;
	
	public Screen()
	{
		sprites = new ArrayList<Sprite>();
		bitmaps = new ArrayList<Bitmap>();
		buttons = new ArrayList<Button>();
		inputManagers = new ArrayList<InputManager>();
		screenObjects = new ArrayList<ScreenObject>();
	}
	
	public void addSprite(Sprite sprite, float depth)
	{
		sprite.setDepth(depth);
		for (int i = 0; i < sprites.size(); i++)
			if (sprites.get(i).getDepth() > depth)
				sprites.add(i, sprite);
		
		if (!sprites.contains(sprite))
			sprites.add(sprite);
	}
	
	public void addSprite(Sprite sprite) 
	{ 
		sprite.setDepth(1); 
		sprites.add(sprite);
	
	}
	public void removeSprite(Sprite sprite) { sprites.remove(sprite); }

	
	public void addObject(ScreenObject object, float depth)
	{
		object.setDepth(depth);
		for (int i = 0; i < screenObjects.size(); i++)
			if (screenObjects.get(i).getDepth() > depth)
				screenObjects.add(i, object);
		
		if (!screenObjects.contains(object))
			screenObjects.add(object);
		
	}
	
	public void addObject(ScreenObject object) { addObject(object, 1); }
	public void removeObject(ScreenObject object) { screenObjects.remove(object); }
	
	public void render(Display display)
	{
		for (int i = screenObjects.size() - 1; i >= 0; i--)
			screenObjects.get(i).render(display);
	}
	
}
