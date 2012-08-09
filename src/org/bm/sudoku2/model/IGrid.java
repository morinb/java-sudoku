package org.bm.sudoku2.model;

import java.util.List;


public interface IGrid {

	/**
	 * @param r
	 *            Row number wanted.
	 * @return Gets sudoku row specified by the parameter.
	 */
	IBox[] getRow(int r);

	/**
	 * 
	 * @param c
	 *            Column number wanted.
	 * @return Gets sudoku column specified by the parameter.
	 */
	IBox[] getColumn(int c);

	/**
	 * 
	 * @param b
	 *            Block number wanted.
	 * @return Gets sudoku row specified by the parameter.
	 */
	IBox[][] getBlock(int b);

	/**
	 * Set the Box value.
	 * @param rowCol Coordinates of the box in the Grid.
	 * @param v new Value
	 */
	void set(RowCol rowCol, Value v);

	/**
	 * Check validity of the grid.
	 * @return false if something is wrong in the grid.
	 */
	boolean isValid();

	/**
	 * Grid is full, and valid.
	 * @return true if the grid is full and valid.
	 */
	boolean isFinished();

	/**
	 * Tries to eliminate some values in the possible values by comparing them to the others of the same row/column/block.
	 * @return true if Grid has been modified.
	 */
	boolean updatePossibleValues();
	
	

	/**
	 * Tries to minimize the number of possible values for a box. i.e. a box contains only one possible value
	 * @return true if Grid has been modified.
	 */
	boolean shrinkPossibleValues();

	/**
	 * 
	 * @return The coordinates of the first Box that have the minimal possible values set.
	 */
	RowCol getBestBox();

	// Reccurence part.
	IGrid getParent();
	
	/**
	 * @return next child grid
	 */
	IGrid nextChild();

	/**
	 * 
	 * @return true if this Grid has no child, or if the current grid is the last child to use.
	 */
	boolean isLastChildSelected();

	/**
	 * Create as many children as the box has possible values.
	 * @param rowCol Coordinate of a Box that have possible values.
	 */
	void createChild(RowCol rowCol);

	/**
	 * Gets the inner grid.
	 * @return
	 */
	IBox[][] getGrid();
}
