package org.bm.sudoku2.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bm.sudoku2.model.IBox;
import org.bm.sudoku2.model.RowCol;
import org.bm.sudoku2.model.Value;

public class Box implements IBox {

	private List<Value> possibleValues;
	private Value value;

	@Override
	public List<Value> getPossibleValues() {
		return Collections.unmodifiableList(possibleValues);
	}

	@Override
	public Value getValue() {
		return value;
	}

	@Override
	public void setValue(Value value) {
		this.value = value;
		possibleValues.clear();
	}

	@Override
	public boolean removeFromPossibleValue(Collection<Value> values) {
		boolean modified = possibleValues.removeAll(values);

		if (possibleValues.size() == 1) {
			setValue(possibleValues.get(0));
		}
		return modified;
	}

	@Override
	public boolean shrinkPossibleValues() {
		boolean modified = false;

		if (possibleValues.size() == 1) {
			modified = true;
			setValue(possibleValues.get(0));
		}

		return modified;
	}

	private Box() {}

	public Box(Value value, RowCol rc) {
		this();

		possibleValues = new ArrayList<Value>(9);
		if (value.equals(Value.NONE)) {
			for (int i = 1; i <= 9; i++) {
				possibleValues.add(Value.from(i));
			}
		} else {
			setValue(value);
		}
		
		this.value = value;
	}

	public Box(IBox box) {
		this();
		this.value = box.getValue();
		this.possibleValues = new ArrayList<Value>(9);
		this.possibleValues.addAll(box.getPossibleValues());
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		if (Value.NONE.equals(value)) {
			for (Value v : possibleValues) {
				sb.append(v.get()).append(',');
			}
		} else {
			sb.append(value.get());
		}

		sb.append(']');
		return sb.toString();
	}
}
