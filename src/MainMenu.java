import java.awt.event.MouseEvent;

import acm.graphics.GObject;
import acm.graphics.GOval;

public class MainMenu extends GraphicsPane {
	private MainApplication program; //you will use program to get access to all of the GraphicsProgram calls
	private GOval oval;
	
	public MainMenu(MainApplication app) {
		this.program = app;
		oval = new GOval(100, 100, 100, 100);
	}
	
	@Override
	public void showContents() {
		program.add(oval);
	}

	@Override
	public void hideContents() {
		program.remove(oval);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if(obj == oval) {
			program.switchBack();
		}
	}

}
