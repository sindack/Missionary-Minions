package gameClasses;
import java.util.ArrayList;

import acm.graphics.*;
import gameElementClasses.*;
import menuClasses.*;

public class GraphicsGame extends GraphicsPane{

	private final RowCol STARTING_WORLD_POSITION = new RowCol(5, 0);
	
	private ArrayList<GLine> gridLines;
	
	private Game game;
	
	private MainApplication program;
	
/*******************************
 *****Functions and Methods*****
 *******************************/
	
	public GraphicsGame(MainApplication app){
		program = app;
		
		game = new Game(STARTING_WORLD_POSITION);
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
	
	private void showGridLines(){
		for (int i = 0; i < gridLines.size(); i++)
		{
			program.add(gridLines.get(i));
		}
	}
	
	private void printGrid(){
		GridData grid = game.getGridData();
		for (int i = 0; i < grid.getNUM_COLS(); i++){
			for (int j = 0; j < grid.getNUM_ROWS(); j++){
				RowCol gridLocation = new RowCol(j, i);
				if (grid.getSpaceData(gridLocation) != "Empty"){
					program.add(new GLabel(grid.getSpaceData(gridLocation), i * getGridWidth() + 4, j * getGridHeight() + 13));
				}
				
			}
		}
		
	}
	
	private int getGridWidth(){
		return program.getWidth() / game.getGridData().getNUM_COLS();
	}
	
	private int getGridHeight(){
		return program.getHeight() / game.getGridData().getNUM_ROWS();
	}
	
	
	@Override
	public void showContents() {
		showGridLines();
		printGrid();
	}

	@Override
	public void hideContents() {
		// TODO Auto-generated method stub
		
	}

}
