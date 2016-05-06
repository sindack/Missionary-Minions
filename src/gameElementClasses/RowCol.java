package gameElementClasses;
/**
 * Simple class that represents a row and a column, with simple getters and setters for both
 * @author Osvaldo
 */

public class RowCol {
	//TODO Put your instance variables here
	
	/**
	 * The constructor that will set up the object to store a row and column
	 * 
	 * @param row
	 * @param col
	 */
	private int row; 
	private int col;
	public RowCol(int row, int col) {
		setRow(row);
		setCol(col);
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	
	@Override
	public String toString(){
		return "(" + Integer.toString(row) + ", " + Integer.toString(col) + ")";
	}
	
	//TODO put any additional methods here
}
