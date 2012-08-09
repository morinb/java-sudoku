package org.bm.sudoku2.model.impl;

import org.bm.sudoku2.model.IEngine;
import org.bm.sudoku2.model.IGrid;
import org.bm.sudoku2.model.RowCol;
import org.bm.sudoku2.model.Value;
import org.bm.sudoku2.model.exception.NoSolutionFoundException;

public class Engine implements IEngine {
	private long startTime;
	private long endTime;
	private long nbIterations;
	private IGrid current;

	@Override
	public IGrid solve(IGrid grid) throws NoSolutionFoundException {
		current = grid;
		startTime = System.currentTimeMillis();
		boolean modified = false;
		nbIterations = 0;
		do {
			modified = false;
			modified |= current.updatePossibleValues();
			modified |= current.shrinkPossibleValues();

			if (!modified) {
				RowCol bestBox = current.getBestBox();
				if (!bestBox.isValid()) {
					// grid is invalid, switch to next child.
					while (current.getParent().isLastChildSelected()) {
						current = current.getParent();
						if (current == null) {
							// We're on the first grid, that means this grid has
							// no solution.
							throw new NoSolutionFoundException();
						}
					}
					current = current.getParent().nextChild();
				} else {
					// We are stuck.
					// Create grids for possible values.
					current.createChild(bestBox);

					// We study the first child grid now
					current = current.nextChild();
				}
				nbIterations++;
			}

			while (!current.isValid()) {
				current = current.getParent();
				if (current == null) {
					// We're on the first grid, that means this grid has
					// no solution.
					throw new NoSolutionFoundException();
				}
				IGrid g2 = current.nextChild();
				while (g2 == null) {
					current = current.getParent();
					g2 = current.nextChild();
				}
				current = g2;
			}
		} while (!current.isFinished());
		endTime = System.currentTimeMillis();

		return current;
	}

	@Override
	public long getEndTime() {
		return endTime;
	}

	@Override
	public long getStartTime() {
		return startTime;
	}

	@Override
	public long getIterationsNumber() {
		return nbIterations;
	}

	@Override
	public IGrid parse(String input) {

		if (input.length() != 81) {
			throw new IllegalArgumentException("input String must be 81 chars long.");
		}

		IGrid g = new Grid();

		char[] datas = input.toCharArray();
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				int i = r * 9 + c;
				RowCol rc = new RowCol(r, c);

				char car = datas[i];
				if ('.' == car || '0' == car) {
					g.set(rc, Value.from(0));
				} else {
					g.set(rc, Value.from(Integer.parseInt(Character.toString(car))));
				}
			}
		}
		return g;
	}
}
