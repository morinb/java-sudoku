package org.bm.sudoku2.model;

import org.bm.sudoku2.model.exception.NoSolutionFoundException;

public interface IEngine {
	/**
	 * Solves the grid, and does not modify it.
	 * @param grid Initial problem grid.
	 * @throws NoSolutionFoundException if no solutions are found.
	 */
	IGrid solve(IGrid grid) throws NoSolutionFoundException;

	/**
	 * 
	 * @return start time of the computation.
	 */
	long getStartTime();

	/**
	 * 
	 * @return end time of the computation
	 */
	long getEndTime();

	/**
	 * 
	 * @return how many iterations it takes.
	 */
	long getIterationsNumber();
	
	/**
	 * Creates a Grid from an input String.
	 * @param input
	 * @return
	 */
	IGrid parse(String input);
		
}
