package org.bm.sudoku2.model;

public class RowCol implements Comparable<RowCol> {
	private final int row;
	private final int col;

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public RowCol(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public boolean isValid() {
		return row != -1 && col != -1;
	}

	public String toString() {
		return "[" + row + "," + col + "]";
	}

	@Override
	public int compareTo(RowCol o) {
		int r = o.row;
		int c = o.col;

		if (row < r)
			return -1;

		if (row > r)
			return 1;

		if (col < c)
			return -1;

		if (col > c)
			return 1;

		return 0;
	}

}
