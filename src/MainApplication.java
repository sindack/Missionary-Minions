import java.awt.*;

import acm.graphics.*;
import acm.program.*;

public class MainApplication extends GraphicsApplication {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	
	private SomePane somePane;
	private MenuPane menu;
	
	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	}
	
	public void run() {
		somePane = new SomePane(this);
		menu = new MenuPane(this);
		setupInteractions();
		switchToSome();
	}
	
	/* Method: setupInteractions
	 * -------------------------
	 * must be called before switching to another
	 * pane to make sure that interactivity
	 * is setup and ready to go.
	 */
	private void setupInteractions() {
		requestFocus();
		addKeyListeners();
		addMouseListeners();
	}
	
	public void switchBack() {
		System.out.println("menu");
		switchToScreen(menu);
	}
	
	public void switchToSome() {
		switchToScreen(somePane);
	}
}
