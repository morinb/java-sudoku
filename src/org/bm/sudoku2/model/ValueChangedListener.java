package org.bm.sudoku2.model;

import java.util.EventListener;

public interface ValueChangedListener extends EventListener {
	void valueChanged(UpdatedValue updatedValue);
}
