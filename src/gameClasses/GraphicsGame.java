package gameClasses;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import acm.graphics.*;
import gameElementClasses.*;
import menuClasses.*;

public class GraphicsGame extends GraphicsPane{

	private final RowCol STARTING_WORLD_POSITION = new RowCol(0, 0);
	private final int NUMBER_OF_SQUARES = 15;
	
	private ArrayList<GLine> horizontalGridLines;
	private ArrayList<GLine> verticalGridLines;
	private ArrayList<GLine> pathLines;
	private ArrayList<GLabel> rockLocations;
	private ArrayList<GLabel> newRockLocations;
	
	private boolean isPlayerSelected;
	private boolean canPathBeDrawn;
	private boolean isCollidingWithRock;
	
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
		rockLocations = new ArrayList<GLabel>();
		newRockLocations = new ArrayList<GLabel>();
		isCollidingWithRock = false;
		
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
						if ((x <= Game.getOverworldSize().getRow() - 1 && x >= 0 && y <= Game.getOverworldSize().getCol() - 1
								&& y >= 0) && game.getEntityAtLocation(consoleLocation) != "Rock"){
							drawPathLines(consoleLocation);
						}
					}
					addAndDrawPathLines();
					mouseReleasedContents();
					return;
				}
				if (game.getEntityAtLocation(consoleLocation) == "Rock" || isCollidingWithRock){
					
					isCollidingWithRock = true;
					drawPathLines(consoleLocation);
					addAndDrawPathLines();
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
		if (isCollidingWithRock){
			return;
		}
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
			RowCol newLocation = new RowCol(movingX, movingY);
			if (!(movingX < Game.getOverworldSize().getRow() - 1 && movingX > 0 && movingY < Game.getOverworldSize().getCol() - 1 && movingY > 0)){
				return;
			}
			if (game.getEntityAtLocation(newLocation) != "Rock"){
				game.getGridData().appendMovementQueue(newLocation);
			}
			else{
				System.out.println("ROCK WAS IN THE WAY");
				isCollidingWithRock = true;
				return;
			}
		}
	}

	private void showRocks(){
		ArrayList<Rock> r = game.getGridData().getRocks();
		Font f = new Font("Arial-BoldMT", Font.BOLD, 40);
		for (int i = 0; i < r.size(); i++){
			RowCol rockPos = r.get(i).getLocation();
			rockLocations.add(new GLabel("O", getXFromRowCol(rockPos.getCol()) + 8 , getYFromRowCol(rockPos.getRow()) + 38));
			rockLocations.get(i).setFont(f);
			program.add(rockLocations.get(i));
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
			if (!isCollidingWithRock){
				game.getGridData().appendMovementQueue(consoleLocation);
			}
			
		}
		else{
			while (indexContains + 1 < game.getGridData().getMovementQueueSize()){
				game.getGridData().removeFromMovementQueue(indexContains + 1);
				isCollidingWithRock = false;
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
		mouseReleasedContents();
		
	}

	private void mouseReleasedContents(){
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
					game.setWorldLocation(1, 0);
					System.out.println("Moving world right...");
					moveWorld(false, 1);
					
				}
				else if (p.getRow() <= 0){
					game.setWorldLocation(-1, 0);
					System.out.println("Moving world left...");
					moveWorld(false, -1);
					
				}
				else if (p.getCol() >= Game.getOverworldSize().getCol() - 1){
					game.setWorldLocation(0, -1);
					System.out.println("Moving world down...");
					moveWorld(true, 1);
					
				}
				else if (p.getCol() <= 0){
					game.setWorldLocation(0, 1);
					System.out.println("Moving world up...");
					moveWorld(true, -1);
					
				}
				game.setPlayerLocation(new RowCol((int)(player.getLocation().getX() / getGridWidth()), (int)(player.getLocation().getY() / getGridHeight())));
				canPathBeDrawn = true;
				isCollidingWithRock = false;
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
		addRocks(isMovingVertical, multiplier);
		if (isMovingVertical){
			addHorizontalGridLines(multiplier);
		}
		else{
			addVerticalGridLines(multiplier);
		}
		showGridLines();
		for (int i = 0; i < (isMovingVertical ? (10 * (Game.getOverworldSize().getCol() - 2)) : 10 * (Game.getOverworldSize().getCol() - 2)); i++){
			player.move(isMovingVertical ? 0 : -multiplier * getGridWidth() / 10, isMovingVertical ? -multiplier * getGridHeight() / 10 : 0);
			moveRocks(isMovingVertical, -multiplier);
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
		removeExtraRocks(isMovingVertical, multiplier);
		
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
	
	public void addRocks(boolean isMovingVertical, int multiplier){
		ArrayList<Rock> r = game.getGridData().getRocks();
		Font f = new Font("Arial-BoldMT", Font.BOLD, 40);
		for (int i = 0; i < r.size(); i++){
			GLabel newRockLabel = new GLabel("O");
			RowCol rLoc = r.get(i).getLocation();
			if (isMovingVertical){
				newRockLabel.setLocation(getXFromRowCol(rLoc.getCol()) + 8, (rLoc.getRow() - multiplier * 2) * getGridHeight() + 38 + (multiplier * Game.getOverworldSize().getCol()) * getGridHeight());
			}else{
				newRockLabel.setLocation((rLoc.getCol() - multiplier * 2) * getGridWidth() + 8 + (multiplier * Game.getOverworldSize().getRow())*getGridWidth(), getYFromRowCol(rLoc.getRow()) + 38);
			}
			newRockLabel.setFont(f);
			program.add(newRockLabel);
			newRockLocations.add(newRockLabel);
		}
	}
	
	public void moveRocks(boolean isMovingVertical, int multiplier){
		if (isMovingVertical){
			for (int i = 0; i < rockLocations.size(); i++){
				rockLocations.get(i).move(0, multiplier*getGridHeight() / 10);
			}
			for (int i = 0; i < newRockLocations.size(); i++){
				newRockLocations.get(i).move(0, multiplier*getGridHeight() / 10);
			}
		}
		else{
			for (int i = 0; i < rockLocations.size(); i++){
				rockLocations.get(i).move(multiplier*getGridWidth() / 10,0);
			}
			for (int i = 0; i < newRockLocations.size(); i++){
				newRockLocations.get(i).move(multiplier * getGridWidth() / 10, 0);
			}
		}
	}
	
	public void removeExtraRocks(boolean isMovingVertical, int multiplier){
		int j = 0; int size = rockLocations.size();
		for (int i = 0; i < size && j < rockLocations.size(); i++){
			if (isMovingVertical){
				if ((multiplier == 1 && rockLocations.get(j).getLocation().getY() >= player.getLocation().getY() - getGridHeight() * 1.5) || (multiplier == -1 && rockLocations.get(j).getLocation().getY() <= player.getLocation().getY() + getGridHeight()*2.1)){
					j++;
				}
				else{
					removeRock(j);
				}
			}
			else{
				if ((multiplier == 1 && rockLocations.get(j).getLocation().getX() >= player.getLocation().getX() - getGridWidth() * 1.5) || (multiplier == -1 && rockLocations.get(j).getLocation().getX() <= player.getLocation().getX() + getGridWidth() * 1.5)){
					j++;
				}
				else{
					removeRock(j);
				}
			}
			
		}
		for (int i = 0; i < newRockLocations.size(); i++){
			rockLocations.add(newRockLocations.get(i));
		}
		newRockLocations.clear();
	}



	private void removeRock(int j) {
		program.remove(rockLocations.get(j));
		rockLocations.remove(j);
	}
	
	
	
	private void printGrid(){
		GridData grid = game.getGridData();
		showRocks();
		
		for (int i = 0; i < grid.getNUM_COLS(); i++){
			for (int j = 0; j < grid.getNUM_ROWS(); j++){
				RowCol gridLocation = new RowCol(j, i);
				if (grid.getSpaceData(gridLocation) == "Player"){
					player.setLocation( getXFromRowCol(i) + 4, getYFromRowCol(j) + 13);
					program.add(player);
				}
				
			}
		}
	}
	
	private double getXFromRowCol(int x){
		return x * getGridWidth();
	}
	
	private double getYFromRowCol(int y){
		return y * getGridHeight();
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
