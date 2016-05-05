package menuClasses;
/*
 * File: GraphicsApplication
 * -------------------------
 * File for subclassing off of to generate the entire
 * GraphicsApplication.  GraphicsApplication is different
 * than GraphicsPane because the Application is responsible
 * for switching between different panes.
 * 
 * In subclassing GraphicsApplication, think of this as the starting
 * point for your entire application.  This is what
 * you can think of as being your initial graphicsprogram.
 */

import acm.program.*;
import java.awt.event.*;

public abstract class GraphicsApplication extends GraphicsProgram {
	private GraphicsPane curScreen;
	
	/* switchToScreen(newGraphicsPane)
	 * -------------------------------
	 * will simply switch from making one pane that was currently
	 * active, to making another one that is the active class.
	 */
	protected void switchToScreen(GraphicsPane newScreen) {
		if(curScreen != null) {
			curScreen.hideContents();
		}
		newScreen.showContents();
		curScreen = newScreen;
	}
	
	/*
	 * These methods just override the basic
	 * mouse listeners to pass any information that
	 * was given to the application to a particular screen.
	 */
	
	@Override
	public void mousePressed(MouseEvent e) {
		curScreen.mousePressed(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		curScreen.mouseReleased(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		curScreen.mouseClicked(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		curScreen.mouseDragged(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		curScreen.mouseMoved(e);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		curScreen.keyPressed(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		curScreen.keyReleased(e);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		curScreen.keyTyped(e);
	}
}
