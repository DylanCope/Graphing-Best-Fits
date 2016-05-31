package cope.engine.input;

import java.util.ArrayList;

public class InputManager {
	
	private ArrayList<Button> buttons;
	private Input input;
	
	public InputManager(Input input) {
		buttons = new ArrayList<Button>();
		this.input = input;
	}
	
	public void update() {
		for (int i = 0; i < buttons.size(); i++)
			buttons.get(i).update(input);
	}
	
	public void addButton(Button button) {
		buttons.add(button);
	}
	
	public void removeButton(Button button) {
		buttons.remove(button);
	}
	
	public Input getInput() {
		return input;
	}
	
}
