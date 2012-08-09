package org.bm.sudoku2.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bm.sudoku2.model.IBox;
import org.bm.sudoku2.model.IGrid;
import org.bm.sudoku2.model.RowCol;
import org.bm.sudoku2.model.UpdatedValue;
import org.bm.sudoku2.model.Value;
import org.bm.sudoku2.model.ValueChangedListener;

public class Grid implements IGrid, ValueChangedListener {
	private IGrid parent;
	private IGrid[] children;
	private int childId;

	private IBox[][] grid = new Box[9][9];

	@Override
	public IBox[] getRow(int r) {
		IBox[] row = new Box[9];
		for (int c = 0; c < 9; c++) {
			row[c] = grid[r][c];
		}
		return row;
	}

	@Override
	public IBox[] getColumn(int c) {
		IBox[] col = new Box[9];
		for (int r = 0; r < 9; r++) {
			col[r] = grid[r][c];
		}
		return col;
	}

	@Override
	public IBox[][] getBlock(int b) {
		IBox[][] block = new Box[3][3];
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				int i = r * 9 + c;
				if (b == (i % 9 / 3 + i / 27 * 3)) {
					block[r % 3][c % 3] = grid[r][c];
				}
			}
		}
		return block;
	}

	@Override
	public void set(RowCol rowCol, Value v) {
		Box b = new Box(v, rowCol);
		grid[rowCol.getRow()][rowCol.getCol()] = b;
	}

	@Override
	public boolean isValid() {
		boolean valid = true;

		for (int i = 0; i < 9; i++) {
			IBox[] row = getRow(i);
			valid &= validate(row);
			if (!valid) {
				return false;
			}

			IBox[] col = getColumn(i);
			valid &= validate(col);
			if (!valid) {
				return false;
			}

			IBox[] block = toOneDimensionList(getBlock(i));
			valid &= validate(block);
			if (!valid) {
				return false;
			}
		}
		return valid;
	}

	@Override
	public boolean isFinished() {
		for (int i = 0; i < 9; i++) {
			IBox[] row = getRow(i);
			if (45 != sum(row)) {
				return false;
			}

			IBox[] colonne = getColumn(i);
			if (45 != sum(colonne)) {
				return false;
			}

			IBox[] bloc = toOneDimensionList(getBlock(i));
			if (45 != sum(bloc)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean updatePossibleValues() {
		boolean modified = false;

		// uv.clear();

		List<Value> rowValues = new ArrayList<Value>();
		List<Value> colValues = new ArrayList<Value>();
		List<Value> blockValues = new ArrayList<Value>();

		for (int i = 0; i < grid.length; i++) {
			IBox[] row = getRow(i);
			IBox[] col = getColumn(i);
			IBox[][] block = getBlock(i);

			rowValues = computeValues(row);
			colValues = computeValues(col);
			blockValues = computeValues(block);

			for (int c = 0; c < row.length; c++) {
				IBox box = row[c];
				if (!box.getPossibleValues().isEmpty()) {
					modified |= box.removeFromPossibleValue(rowValues);
				}
			}

			for (int r = 0; r < col.length; r++) {
				IBox box = col[r];
				if (!box.getPossibleValues().isEmpty()) {
					modified |= box.removeFromPossibleValue(colValues);
				}
			}

			for (int r = 0; r < block.length; r++) {
				IBox[] rowBlock = block[r];

				for (int c = 0; c < rowBlock.length; c++) {
					IBox box = block[r][c];
					if (!box.getPossibleValues().isEmpty()) {
						modified |= box.removeFromPossibleValue(blockValues);
					}
				}
			}
			modified |= shrinkPossibleValues();
		}

		return modified;
	}

	@Override
	public boolean shrinkPossibleValues() {
		boolean modified = false;
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {
				modified |= grid[r][c].shrinkPossibleValues();
			}
		}

		return modified;
	}

	@Override
	public RowCol getBestBox() {
		int minPos = 10;

		int row = -1;
		int col = -1;

		int countMinPos = 0;

		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				int size = grid[r][c].getPossibleValues().size();
				if (size != 0) {
					if (size == minPos) {
						countMinPos++;
					} else if (size < minPos) {
						countMinPos = 0;
						minPos = size;
					}
				}
			}
		}

		int id = (int) (Math.random() * countMinPos);
		countMinPos = 0;

		suite: for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				int size = grid[r][c].getPossibleValues().size();
				if (size == minPos) {
					if ((countMinPos++) == id) {
						row = r;
						col = c;
						break suite;
					}
				}
			}
		}
		return new RowCol(row, col);
	}

	@Override
	public IGrid getParent() {
		return parent;
	}

	@Override
	public IGrid nextChild() {
		if (children == null) {
			return null;
		}
		if (isLastChildSelected()) {
			return null;
		}

		return children[childId++];
	}

	@Override
	public boolean isLastChildSelected() {
		if (children == null) {
			return true;
		}
		return childId == children.length;
	}

	@Override
	public void createChild(RowCol rowCol) {
		List<Value> pv = grid[rowCol.getRow()][rowCol.getCol()].getPossibleValues();
		children = new Grid[pv.size()];
		childId = 0;

		for (int i = 0; i < pv.size(); i++) {
			Value v = pv.get(i);

			IGrid newGrid = new Grid(this);
			newGrid.getGrid()[rowCol.getRow()][rowCol.getCol()].setValue(v);
			newGrid.updatePossibleValues();

			children[i] = newGrid;
		}
	}

	@Override
	public IBox[][] getGrid() {
		return grid;
	}

	public Grid() {
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				grid[r][c] = null;
			}
		}
	}

	public Grid(IGrid grid) {
		this();
		this.parent = grid;
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				Box b = new Box(grid.getGrid()[r][c]);
				this.grid[r][c] = b;
			}

		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				Value v = grid[r][c].getValue();
				if (Value.NONE.equals(v)) {
					sb.append('.');
				} else {
					sb.append(v.get().toString());
				}
			}
		}

		return sb.toString();
	}

	private IBox[] toOneDimensionList(IBox[][] block) {
		int nbLignes = block.length;
		int nbColonnes = block[0].length;
		int size = nbLignes * nbColonnes;
		IBox[] oneD = new Box[size];

		int i = 0;
		for (int l = 0; l < nbLignes; l++) {
			for (int c = 0; c < nbColonnes; c++) {
				oneD[i++] = block[l][c];
			}
		}

		return oneD;
	}

	private boolean validate(IBox[] box) {
		int[] vals = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, };
		for (int i = 1; i <= 9; i++) {
			Value j = Value.from(i);
			for (IBox c : box) {
				if (c.getValue() == j) {
					vals[i - 1]++;
				}
			}
		}

		// verifies que les vals soient au maximum a 1
		for (int i = 0; i < vals.length; i++) {
			if (vals[i] > 1) {
				return false;
			}
		}

		return true;
	}

	private int sum(IBox[] list) {
		int sum = 0;
		for (IBox c : list) {
			sum += c.getValue().get();
		}
		return sum;
	}

	private List<Value> computeValues(IBox[][] block) {
		return computeValues(toOneDimensionList(block));
	}

	private List<Value> computeValues(IBox[] list) {

		List<Value> values = new ArrayList<Value>();
		for (IBox box : list) {
			if (box.getPossibleValues().isEmpty()) {
				values.add(box.getValue());
			}
		}

		return Collections.unmodifiableList(values);
	}

	@Override
	public void valueChanged(UpdatedValue updatedValue) {
		System.out.println(this);
	}

	
}
