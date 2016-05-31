package cope.engine.input;

import cope.engine.rendering.Display;
import cope.engine.rendering.Sprite;
import cope.engine.rendering.TextObject;
import cope.engine.util.BoundingBox;
import cope.engine.util.Vector;

public class Button {
	
	private BoundingBox box;
	private ButtonEvent event;
	private Sprite sprite;
	private TextObject text;
	private boolean isPressed = false;
	
	public Button(int x, int y, int width, int height, ButtonEvent event) {
		box = new BoundingBox(x, y, width, height);
		this.event = event;
	}
	
	public Button(Sprite sprite, ButtonEvent event) {
		this.sprite = sprite;
		this.box = sprite.getBoundingBox();
		this.event = event;
	}
	
	public Button(TextObject text, ButtonEvent event) {
		this.text = text;
		this.box = text.getBoundingBox();
		this.event = event;
	}
	
	public Button(BoundingBox box, ButtonEvent event) {
		this.box = box;
		this.event = event;
	}
	
	public void setEvent(ButtonEvent event) {
		this.event = event;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void setPosition(Vector position) {
		sprite.setPosition(position);
		box.setPosition(position);
	}
	
	public void translate(Vector dv) {
		sprite.translate(dv);
		box.translate(dv);
	}
	
	public void scale(float factor) {
		sprite.scale(factor);
		box.scale(factor);
	}
	
	public void scale(float xFactor, float yFactor) {
		sprite.scale(xFactor, yFactor);
		box.scale(xFactor, yFactor);
	}
	
	public void rotate(float theta, Vector point) {
		sprite.rotateAroundPoint(theta, point);
		box.rotateAroundPoint(point, theta);
	}
	
	public void rotate(float theta) {
		sprite.rotateAroundCentre(theta);
		box.rotateAroundCentre(theta);
	}
	
	public void update(Input input) 
	{
		
		if (input.getMouse(1) && box.isBounding(input.getMousePosition()))
			event.whilePressed();
		
		if (input.getMouse(1) && !isPressed && box.isBounding(input.getMousePosition())) {
			event.onPress();
			isPressed = true;
		}
		
		if (!input.getMouse(1) && isPressed) {
			event.onRelease();
			isPressed = false;
		}
	}
	
	public void render(Display display)
	{
		if (sprite != null)
			sprite.render(display);
		
		if (text != null)
			text.render(display);
		
	}
	
}
