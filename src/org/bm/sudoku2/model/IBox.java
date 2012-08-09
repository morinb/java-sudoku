package org.bm.sudoku2.model;

import java.util.Collection;
import java.util.List;

public interface IBox {
	List<Value> getPossibleValues();

	Value getValue();

	boolean removeFromPossibleValue(Collection<Value> values);

	void setValue(Value value);

	boolean shrinkPossibleValues();

}
