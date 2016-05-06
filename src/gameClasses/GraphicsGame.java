package gameClasses;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import acm.graphics.*;
import gameElementClasses.*;
import menuClasses.*;

public class GraphicsGame extends GraphicsPane{

	private final RowCol STARTING_WORLD_POSITION = new RowCol(5, 0);
	private final int NUMBER_OF_SQUARES = 15;
	
	private ArrayList<GLine> gridLines;
	private ArrayList<GLine> pathLines;
	
	private boolean isPlayerSelected;
	
	private Game game;
	
	private GLabel player;
	
	private MainApplication program;
	
	private Random rand;
	
/*******************************
 *****Functions and Methods*****
 *******************************/
	
	public GraphicsGame(MainApplication app){
		program = app;
		game = new Game(STARTING_WORLD_POSITION);
		pathLines = new ArrayList<GLine>();
		player = new GLabel("Player");
		rand = new Random();
		createGrid();
	}
	

	
	private void createGrid(){
		gridLines = new ArrayList<GLine>();
		for (int i = 1; i < 16; i++){
			gridLines.add(new GLine(0, i*(program.getHeight() / NUMBER_OF_SQUARES), program.getWidth(), i*(program.getHeight() / NUMBER_OF_SQUARES)));
		}
		for (int i = 1; i < 16; i++){
			gridLines.add(new GLine(i*(program.getWidth() / NUMBER_OF_SQUARES), 0, i*(program.getWidth() / NUMBER_OF_SQUARES), program.getHeight()));
		}
	}
	
	private void showGridLines(){
		for (int i = 0; i < gridLines.size(); i++)
		{
			program.add(gridLines.get(i));
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		GObject obj = program.getElementAt(e.getX(), e.getY());
		isPlayerSelected = false;
		if (obj == player){
			isPlayerSelected = true;
			System.out.println("True");
		}
		
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e){
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (obj == player){
			isPlayerSelected = true;
		}
		if (isPlayerSelected){
			int x = e.getY() / getGridHeight();
			int y = e.getX() / getGridWidth();
			RowCol consoleLocation = new RowCol(x, y);
			if (x >= Game.getOverworldSize().getRow() - 1 || x <= 0 || y >= Game.getOverworldSize().getCol() - 1 || y <= 0)
			{
				if (Math.abs(consoleLocation.getRow() - game.getGridData().getMostRecentInMovementQueue().getRow()) +
						Math.abs(consoleLocation.getCol() - game.getGridData().getMostRecentInMovementQueue().getCol()) >= 2){
					attemptToFixPath(consoleLocation);
					addAndDrawPathLines();
				}
				else{
					drawPathLines(consoleLocation);
				}
				addAndDrawPathLines();
				isPlayerSelected = false;
				return;
			}
			if (game.getGridData().getMovementQueueSize() < 1 || Math.abs(consoleLocation.getRow() - game.getGridData().getMostRecentInMovementQueue().getRow()) +
					Math.abs(consoleLocation.getCol() - game.getGridData().getMostRecentInMovementQueue().getCol()) < 2){
				drawPathLines(consoleLocation);
			}
			else{
				attemptToFixPath(consoleLocation);
				drawPathLines(consoleLocation);
			}

			addAndDrawPathLines();
		}
	}

	private void attemptToFixPath(RowCol dest){
		int movingX = game.getGridData().getMostRecentInMovementQueue().getRow();
		int movingY = game.getGridData().getMostRecentInMovementQueue().getCol();
		while (movingX != dest.getRow() || movingY != dest.getCol()){
			int deltaX = 0;
			int deltaY = 0;
			if (rand.nextBoolean()){
				deltaX = moveX(dest, movingX);
				if (deltaX == 0){
					deltaY = moveY(dest, movingY);
				}
			}
			else{
				deltaY = moveY(dest, movingY);
				if (deltaY == 0){
					deltaX = moveX(dest, movingX);
				}
			}
			movingX += deltaX;
			movingY += deltaY;
			game.getGridData().appendMovementQueue(new RowCol(movingX, movingY));
			if (!(movingX < Game.getOverworldSize().getRow() - 1 && movingX > 0 && movingY < Game.getOverworldSize().getCol() - 1 && movingY > 0)){
				return;
			}
		}
	}



	private int moveY(RowCol dest, int movingY) {
		return movingY == dest.getCol() ? 0 : movingY < dest.getCol() ? 1:-1;
	}



	private int moveX(RowCol dest, int movingX) {
		return movingX == dest.getRow() ? 0 : movingX < dest.getRow() ? 1:-1;
	}
	
	
	

	private void drawPathLines(RowCol consoleLocation) {
		int indexContains = game.getGridData().movementQueueContains(consoleLocation);
		if (indexContains == -1){ // Adds a location to the queue if that location isn't already there
			game.getGridData().appendMovementQueue(consoleLocation);
			
		}
		else{
			while (indexContains + 1 < game.getGridData().getMovementQueueSize()){
				game.getGridData().removeFromMovementQueue(indexContains + 1);
			}
		}
	}
	

	
	private void addAndDrawPathLines(){
		for (int i = 0; i < pathLines.size(); ++i){
			program.remove(pathLines.get(i));
		}
		pathLines.clear();
		ArrayList<RowCol> coords = game.getGridData().copyMovementQueue();
		for (int i = 0; i < coords.size() - 1; i++){
			RowCol rc1 = coords.get(i);
			RowCol rc2 = coords.get(i + 1);
			pathLines.add(new GLine(rc1.getCol() * getGridWidth() + getGridWidth() * 0.5, rc1.getRow() * getGridHeight() + getGridHeight() * 0.5, rc2.getCol() * getGridWidth() + getGridWidth() * 0.5, rc2.getRow() * getGridHeight() + getGridHeight() * 0.5));
			program.add(pathLines.get(pathLines.size() - 1));
		}
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO: REMOVE ALL GLINES HERE!!
		game.getGridData().clearMovementQueue();
		isPlayerSelected = false;
	}
	
	
	private void printGrid(){
		GridData grid = game.getGridData();
		for (int i = 0; i < grid.getNUM_COLS(); i++){
			for (int j = 0; j < grid.getNUM_ROWS(); j++){
				RowCol gridLocation = new RowCol(j, i);
				if (grid.getSpaceData(gridLocation) == "Player"){
					player.setLocation( i * getGridWidth() + 4, j * getGridHeight() + 13);
					program.add(player);
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
