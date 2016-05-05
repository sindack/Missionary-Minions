package menuClasses;
import java.awt.event.MouseEvent;

import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;

public class SubMenu extends GraphicsPane {
	private MainApplication program; //you will use program to get access to all of the GraphicsProgram calls
	private GRect rect;
	
	public SubMenu(MainApplication app) {
		program = app;
		rect = new GRect(200, 200, 200, 200);
		rect.setFilled(true);
	}
	@Override
	public void showContents() {
		program.add(rect);
	}

	@Override
	public void hideContents() {
		program.remove(rect);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if(obj == rect) {
			program.switchToSome();
		}
	}
}
