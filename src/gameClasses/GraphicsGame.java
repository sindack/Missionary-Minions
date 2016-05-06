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
	
	private ArrayList<GLine> horizontalGridLines;
	private ArrayList<GLine> verticalGridLines;
	private ArrayList<GLine> pathLines;
	
	private boolean isPlayerSelected;
	private boolean canPathBeDrawn;
	
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
		canPathBeDrawn = true;
		isPlayerSelected = false;
		createGrid();
	}
	

	
	private void createGrid(){
		horizontalGridLines = new ArrayList<GLine>();
		verticalGridLines = new ArrayList<GLine>();
		addHorizontalGridLines(0);
		addVerticalGridLines(0);
	}



	private void addVerticalGridLines(int multiplier) {
		for (int i = Game.getOverworldSize().getRow() * multiplier + 1; i < multiplier * Game.getOverworldSize().getRow() + 16; i++){
			verticalGridLines.add(new GLine(i*(program.getWidth() / NUMBER_OF_SQUARES), 0, i*(program.getWidth() / NUMBER_OF_SQUARES), program.getHeight()));
		}
	}

	private void addHorizontalGridLines(int multiplier) {
		for (int i = Game.getOverworldSize().getCol() * multiplier + 1; i < multiplier * Game.getOverworldSize().getCol() + 16; i++){
			horizontalGridLines.add(new GLine(0, i*(program.getHeight() / NUMBER_OF_SQUARES), program.getWidth(), i*(program.getHeight() / NUMBER_OF_SQUARES)));
		}
	}
	
	private void showGridLines(){
		for (int i = 0; i < horizontalGridLines.size(); i++){
			program.add(horizontalGridLines.get(i));
		}
		for (int i = 0; i < verticalGridLines.size(); i++){
			program.add(verticalGridLines.get(i));
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		if (canPathBeDrawn){
			GObject obj = program.getElementAt(e.getX(), e.getY());
			isPlayerSelected = false;
			if (obj == player){
				isPlayerSelected = true;
				System.out.println("True");
			}
		}
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e){
		if (canPathBeDrawn) {
			GObject obj = program.getElementAt(e.getX(), e.getY());
			if (obj == player) {
				isPlayerSelected = true;
			}
			if (isPlayerSelected) {
				int x = (int) (e.getY() / getGridHeight());
				int y = (int) (e.getX() / getGridWidth());
				RowCol consoleLocation = new RowCol(x, y);
				if (x >= Game.getOverworldSize().getRow() - 1 || x <= 0 || y >= Game.getOverworldSize().getCol() - 1
						|| y <= 0) {
					if (Math.abs(consoleLocation.getRow() - game.getGridData().getMostRecentInMovementQueue().getRow())
							+ Math.abs(consoleLocation.getCol()
									- game.getGridData().getMostRecentInMovementQueue().getCol()) >= 2) {
						attemptToFixPath(consoleLocation);
					} else {
						drawPathLines(consoleLocation);
					}
					addAndDrawPathLines();
					canPathBeDrawn = false;
					return;
				}
				if (game.getGridData()
						.getMovementQueueSize() < 1 || Math
								.abs(consoleLocation.getRow()
										- game.getGridData().getMostRecentInMovementQueue().getRow())
								+ Math.abs(consoleLocation.getCol()
										- game.getGridData().getMostRecentInMovementQueue().getCol()) < 2) {
					drawPathLines(consoleLocation);
				} else {
					attemptToFixPath(consoleLocation);
					drawPathLines(consoleLocation);
				}

				addAndDrawPathLines();
			} 
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
		if (isPlayerSelected){
			canPathBeDrawn = false;
			isPlayerSelected = false;
			removePathLinesAndMovePlayer();
			game.getGridData().clearMovementQueue();	
		}
	}



	private void removePathLinesAndMovePlayer() {
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run(){
				t.cancel();
				for (int i = 0; i < pathLines.size(); i++){
					GLine line = pathLines.get(i);
					double deltaX = line.getStartPoint().getX() - line.getEndPoint().getX();
					double deltaY = line.getStartPoint().getY() - line.getEndPoint().getY();
					program.remove(line);
					movePlayer(deltaX, deltaY);
					game.setPlayerLocation(new RowCol((int)(line.getEndPoint().getX() / getGridWidth()), (int)(line.getEndPoint().getY() / getGridHeight())));
				}
				
				RowCol p = game.getPlayer().getLocation();
				if (p.getRow() >= Game.getOverworldSize().getRow() - 1){
					System.out.println("Moving world right...");
					
					moveWorld(false, 1);
				}
				else if (p.getRow() <= 0){
					System.out.println("Moving world left...");
					moveWorld(false, -1);
				}
				else if (p.getCol() >= Game.getOverworldSize().getCol() - 1){
					System.out.println("Moving world down...");
					moveWorld(true, 1);
				}
				else if (p.getCol() <= 0){
					System.out.println("Moving world up...");
					moveWorld(true, -1);
				}
				game.setPlayerLocation(new RowCol((int)(player.getLocation().getX() / getGridWidth()), (int)(player.getLocation().getY() / getGridHeight())));
				canPathBeDrawn = true;
			}
		}, 0);
	}
	
	
	
	

	private void movePlayer(double deltaX, double deltaY){
		for (int i = 0; i < 10; i++){
			player.move(-deltaX / 10, -deltaY / 10);
			program.pause(10);
		}
	}
	
	private void moveWorld(boolean isMovingVertical, int multiplier){
		if (isMovingVertical){
			addHorizontalGridLines(multiplier);
		}
		else{
			addVerticalGridLines(multiplier);
		}
		showGridLines();
		for (int i = 0; i < (isMovingVertical ? (10 * (Game.getOverworldSize().getCol() - 2)) : 10 * (Game.getOverworldSize().getCol() - 2)); i++){
			player.move(isMovingVertical ? 0 : -multiplier * getGridWidth() / 10, isMovingVertical ? -multiplier * getGridHeight() / 10 : 0);
			if (isMovingVertical){
				moveHorizontalLines(-multiplier);
			}
			else{
				moveVerticalLines(-multiplier);
			}
			program.pause(4);
		}
		if (isMovingVertical){
			removeExtraHorizontalLines();
		}
		else{
			removeExtraVerticalLines();
		}
		
	}
	
	private void removeExtraVerticalLines(){
		while(verticalGridLines.size() > 0){
			program.remove(verticalGridLines.get(0));
			verticalGridLines.remove(0);
		}
		addVerticalGridLines(0);
		showGridLines();
		
	}
	
	private void removeExtraHorizontalLines(){
		while(horizontalGridLines.size() > 0){
			program.remove(horizontalGridLines.get(0));
			horizontalGridLines.remove(0);
		}
		addHorizontalGridLines(0);
		showGridLines();
	}
	
	private void moveHorizontalLines(int multiplier){
		for (int i = 0; i < horizontalGridLines.size(); i++){
			horizontalGridLines.get(i).move(0, multiplier * getGridHeight() / 10);
		}
	}
	
	private void moveVerticalLines(int multiplier){
		for (int i = 0; i < verticalGridLines.size(); i++){
			verticalGridLines.get(i).move(multiplier*getGridWidth() / 10, 0);
		}
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
	
	private double getGridWidth(){
		return program.getWidth() / game.getGridData().getNUM_COLS();
	}
	
	private double getGridHeight(){
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
