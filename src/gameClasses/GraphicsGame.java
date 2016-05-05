package gameClasses;
import java.util.ArrayList;

import acm.graphics.*;
import menuClasses.*;

public class GraphicsGame extends GraphicsPane{

	private MainApplication program;
	private ArrayList<GLine> gridLines;
	private GridData data;
	
	public GraphicsGame(MainApplication app){
		program = app;
		createGrid();
	}
	
	private void createGrid(){
		gridLines = new ArrayList<GLine>();
		for (int i = 1; i < 16; i++){
			gridLines.add(new GLine(0, i*(program.getHeight() / 15), program.getWidth(), i*(program.getHeight() / 15)));
		}
		for (int i = 1; i < 16; i++){
			gridLines.add(new GLine(i*(program.getWidth() / 15), 0, i*(program.getWidth() / 15), program.getHeight()));
		}
	}
	
	private void showGrid(){
		for (int i = 0; i < gridLines.size(); i++)
		{
			program.add(gridLines.get(i));
		}
	}
	
	
	@Override
	public void showContents() {
		showGrid();
		
	}

	@Override
	public void hideContents() {
		// TODO Auto-generated method stub
		
	}

}
