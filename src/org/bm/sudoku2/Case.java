package org.bm.sudoku2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Case {
	private List<Integer> possibleValues;
	private Integer value;

	public List<Integer> getPossibleValues() {
		return Collections.unmodifiableList(possibleValues);
	}

	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
		possibleValues.clear();
	}

	private boolean displayPossibleValues = false;

	private Case() {
	}
	
	public Case(Case c) {
		this();
		this.value = c.getValue();
		this.possibleValues = new ArrayList<Integer>();
		this.possibleValues.addAll(c.getPossibleValues());
	}

	public boolean removeFromPossibleValues(List<Integer> values) {
		boolean modified = possibleValues.removeAll(values);

		if (possibleValues.size() == 1) {
			value = possibleValues.get(0);
			possibleValues.clear();
		}

		return modified;
	}

	public boolean utiliserValeurSure() {
		boolean modified = false;

		if (possibleValues.size() == 1) {
			modified = true;
			value = possibleValues.get(0);
			possibleValues.clear();
		}

		return modified;

	}

	public Case(Integer value) {
		this();
		possibleValues = new ArrayList<Integer>(9);
		if (value.equals(Grille.values[0])) {
			for (int i = 1; i <= 9; i++) {
				possibleValues.add(Grille.values[i]);
			}
		}
		this.value = value;
	}

	public boolean isDisplayPossibleValues() {
		return displayPossibleValues;
	}

	public void setDisplayPossibleValues(boolean displayPossibleValues) {
		this.displayPossibleValues = displayPossibleValues;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (Grille.values[0].equals(value)) {
			sb.append('.');
		} else {
			sb.append(value.toString());
		}
		if (displayPossibleValues) {
			// (1, 2, 3, 4, 5, 6, 7, 8, 9) = 27 caracteres
			sb.append('(');
			int size = possibleValues.size();

			for (int i = 0; i < size; i++) {
				sb.append(possibleValues.get(i));
				if (i != (size - 1)) {
					sb.append(',');
				}
			}
			sb.append(')');
			while (sb.length() < 27) {
				sb.append(' ');
			}
		}

		return sb.toString();
	}

}
