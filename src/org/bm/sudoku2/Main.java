package org.bm.sudoku2;

import org.bm.sudoku2.model.IEngine;
import org.bm.sudoku2.model.IGrid;
import org.bm.sudoku2.model.exception.NoSolutionFoundException;
import org.bm.sudoku2.model.impl.Engine;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test();
	}

	private static void test() {
		String[] sudokus = new String[] { "................................................................................." };
		// "..3...1.69.8.6..25..5729.8.39.5.8...67.2.4.38...6.3.12.8.1572..21..3.4.95.6...8..",
		// "..1.....8.4...2.7...3.6...9.5...9.....4...2.....1...3.9...7.4...2.8...6.7.....5..",
		// "...2.....6..845...5...7.194..2.61...459...261...49.73.213.5...8...384..2.....7...",
		// "8.6.7.45.7....4693..4...8.7..18.72.6.6.4.2.7.2.73.61..4.3...9..6125....4.58.4.3.2",
		// "..254....3..6.98149........28395..6..7.4.6.2..4..82375........94268.5..7....136..",
		// ".....2..52.5...6...84....9.4..297..3.3.1.4.2.9..538..1.5....38...2...1.71..7.....",
		// ".54.1..6..6.8.9...9.7......1...7.6..67.....53..2.6...7......3.6...6.7.9..3..2.41.",
		// ".2..1...91.8..9..69..3....8......7...8.295.1...3......8....6..57..4..2.15...2..4.",
		// "................................................................................."
		// };
		IEngine engine = new Engine();

		for (String sudoku : sudokus) {
			IGrid g = engine.parse(sudoku);
			System.out.println(g.toString());
			IGrid solution = null;
			try {
				solution = engine.solve(g);
			} catch (NoSolutionFoundException e) {
				e.printStackTrace();
			}
			System.out.println();
			System.out.println(solution.toString());
			System.out.println("computed in " + (engine.getEndTime() - engine.getStartTime()) + " ms");
			System.out.println(" and in " + engine.getIterationsNumber() + " iterations");
			
		}
	}
}
