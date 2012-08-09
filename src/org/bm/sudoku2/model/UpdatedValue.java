package org.bm.sudoku2.model;

public class UpdatedValue implements Comparable<UpdatedValue> {
	private final Value value;
	private final RowCol rowCol;

	public UpdatedValue(Value value, RowCol rowCol) {
		super();
		this.value = value;
		this.rowCol = rowCol;
	}

	public Value getValue() {
		return value;
	}

	public RowCol getRowCol() {
		return rowCol;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(rowCol).append(" => ").append(value);

		return sb.toString();
	}

	@Override
	public int compareTo(UpdatedValue o) {
		
		int rowColCompare = rowCol.compareTo(o.rowCol);
		
		if(rowColCompare == 0) {
			return value.compareTo(o.value);
		}
		
		return rowColCompare;
	}
}
