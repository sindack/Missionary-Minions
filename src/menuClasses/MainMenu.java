package menuClasses;
import java.awt.event.MouseEvent;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;

public class MainMenu extends GraphicsPane {
	private MainApplication program; //you will use program to get access to all of the GraphicsProgram calls
	private GOval oval;
	private GLabel label;
	
	public MainMenu(MainApplication app) {
		this.program = app;
		label = new GLabel("Hello, world!");
		label.setLocation(100, 50);
		program.add(label);
		
		
		
		
		
		oval = new GOval(100, 100, 100, 100);
		
		
	}
	
	@Override
	public void showContents() {
		program.add(oval);
		program.add(label);
	}

	@Override
	public void hideContents() {
		program.remove(oval);
		program.remove(label);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if(obj == oval) {
			program.switchToGame();
		}
	}

}
