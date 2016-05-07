package gameClasses;

import java.util.ArrayList;
import java.util.Random;

import gameElementClasses.*;

public class GridData {
	private final int NUM_ROWS;
	private final int NUM_COLS;
	
	private String[][] grid;
	private RowCol worldLocation;
	private int cameFromX;
	private int cameFromY;
	
	private ArrayList<RowCol> movementQueue;
	private ArrayList<Rock> rocks;

	private Random rand;
	
	
	
	public GridData(RowCol worldGrid, RowCol gridSize, int cameFromX, int cameFromY, String[][] oldGrid){ // worldGrid, gridSize
		worldLocation = worldGrid;
		movementQueue = new ArrayList<RowCol>();
		this.cameFromX = cameFromX;
		this.cameFromY = cameFromY;
		rocks = new ArrayList<Rock>();
		rand = new Random();
		grid = oldGrid;
		
		NUM_ROWS = gridSize.getRow();
		NUM_COLS = gridSize.getCol();
		if (grid == null){
			grid = new String[NUM_ROWS][NUM_ROWS];
		}
		fillGrid();
	}
	
	private void fillGrid(){
		fillGridWithRandomizedRocks();
	}
	
	private void fillGridWithEmptySlots(){
		for (int i = 0; i < NUM_ROWS; i++){
			for (int j = 0; j < NUM_COLS; j++){
				grid[i][j] = "Empty";
			}
		}
	}
	
	private void fillGridWithRandomizedRocks(){
		int x = 0;
		int iSizeFactor = 0;
		int y = 0;
		int jSizeFactor = 0;
		if (cameFromX == 1){
			x = 2;
		}
		else if (cameFromX == -1){
			jSizeFactor = 2;
		}
		if (cameFromY == 1){
			y = 2;
		}
		else if (cameFromY == -1){
			iSizeFactor = 2;
		}
		for (int i = y; i < NUM_ROWS - iSizeFactor; i++){
			for (int j = x; j < NUM_COLS - jSizeFactor; j++){
				grid[i][j] = "Empty";
				if (rand.nextInt(7) == 0){
					grid[i][j] = "Rock";
					rocks.add(new Rock(new RowCol(i, j)));
				}
			}
		}
	}
	
	public void moveGrid(int deltaX, int deltaY){
		if (deltaY != 0){
			if (deltaY == 1){
				System.out.println("Grid Moving Up");
				for (int i = 0; i < NUM_ROWS; i++){
					grid[NUM_ROWS - 2][i] = grid[0][i];
					grid[NUM_ROWS - 1][i] = grid[1][i];
				}
			}
			else{
				System.out.println("Grid Moving Down");
				for (int i = 0; i < NUM_ROWS; i++){
					grid[0][i] = grid[NUM_ROWS - 2][i];
					grid[1][i] = grid[NUM_ROWS - 1][i];
				}
			}
		}
		else{
			if (deltaX == 1){
				System.out.println("Grid Moving Right");
				for (int i = 0; i < NUM_COLS; i++){
					grid[i][0] = grid[i][NUM_COLS - 2];
					grid[i][1] = grid[i][NUM_COLS - 1];
				}
			}
			else{
				System.out.println("Grid Moving Left");
				for (int i = 0; i < NUM_COLS; i++){
					grid[i][NUM_COLS - 2] = grid[i][0];
					grid[i][NUM_COLS - 1] = grid[i][1];
				}
			}
		}
		
	}
	

	
	public ArrayList<Rock> getRocks(){
		return rocks;
	}
	
	public String getSpaceData(RowCol space){
		return grid[space.getRow()][space.getCol()];
	}
	
	public void appendMovementQueue(RowCol location){
		movementQueue.add(location);
	}
	
	public void clearMovementQueue(){
		movementQueue.clear();
	}
	
	public int getMovementQueueSize(){
		return movementQueue.size();
	}
	
	public int movementQueueContains(RowCol location){
		for (int i = 0; i < movementQueue.size(); i++){
			RowCol l = movementQueue.get(i);
			if (location.getRow() == l.getRow() && location.getCol() == l.getCol()){
				return i;
			}
		}
		return -1;
	}
	
	public ArrayList<RowCol> copyMovementQueue(){
		return movementQueue;
	}
	
	public RowCol getMostRecentInMovementQueue(){
		return movementQueue.get(movementQueue.size() - 1);
	}
	
	public void updateObjectMovement(RowCol lastLocation, RowCol newLocation, String entity){
		removeEntity(lastLocation);
		addEntity(newLocation, entity);
	}
	
	public void removeEntity(RowCol location){
		grid[location.getRow()][location.getCol()] = "Empty";
	}
	
	public void addEntity(RowCol location, String entity){
		grid[location.getRow()][location.getCol()] = entity;
	}
	
	public String[][] getGrid(){
		return grid;
	}
	
	public int getNUM_ROWS() {
		return NUM_ROWS;
	}
	
	public int getNUM_COLS() {
		return NUM_COLS;
	}

	public void removeFromMovementQueue(int i) {
		movementQueue.remove(i);
	}
}
