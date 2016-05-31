package cope.engine.rendering;

public abstract class ScreenObject 
{
	private float depth;
	private boolean showObject = true;
	
	public float getDepth()
	{
		return depth;
	}
	
	public void setDepth(float depth)
	{
		this.depth = depth;
	}
	
	public void show() { showObject = true;  }
	public void hide() { showObject = false; }
	public boolean isRendering() { return showObject; }
	
	public void setObject(ScreenObject object)
	{
		showObject = true;
	}
	
	
	public abstract void render(Display display);
}
