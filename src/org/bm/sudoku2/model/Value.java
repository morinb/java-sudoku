package org.bm.sudoku2.model;

public enum Value {
	NONE(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), ;

	private Integer value;

	private Value(Integer i) {
		this.value = i;
	}

	public Integer get() {
		return value;
	}
	
	public String toString() {
		return value.toString();
	}
	
	public static Value from(int i) {
		switch(i) {
		case 1: return Value.ONE;
		case 2: return Value.TWO;
		case 3: return Value.THREE;
		case 4: return Value.FOUR;
		case 5: return Value.FIVE;
		case 6: return Value.SIX;
		case 7: return Value.SEVEN;
		case 8: return Value.EIGHT;
		case 9: return Value.NINE;
		default : return Value.NONE;
		}
	}
}
