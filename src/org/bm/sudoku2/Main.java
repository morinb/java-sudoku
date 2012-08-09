package org.bm.sudoku2;

import java.util.Set;

import org.bm.sudoku2.model.IEngine;
import org.bm.sudoku2.model.IGrid;
import org.bm.sudoku2.model.UpdatedValue;
import org.bm.sudoku2.model.exception.NoSolutionFoundException;
import org.bm.sudoku2.model.impl.Engine;
import org.bm.sudoku2.model.impl.Grid;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test1();
		test2();
	}

	private static void test2() {
		String[] sudokus = new String[] { "................................................................................." };

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

	private static void test1() {
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

		// Grille g =
		// Grille.parse("8.6.7.45.7....4693..4...8.7..18.72.6.6.4.2.7.2.73.61..4.3...9..6125....4.58.4.3.2");
		long startTime = System.currentTimeMillis();
		for (String sudoku : sudokus) {
			System.out.println();
			System.out.println("-------------------------------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------------------------------");
			System.out.println();
			Grille g = Grille.parse(sudoku);

			/*
			 * .2..1...91.8..9..69..3....8......7...8.295.1...3......8....6..57..4
			 * ..2 .15...2..4. Solution :
			 * 326817459178549326954362178295631784487295613613784592842176935739458261561923847
			 */
			System.out.println(g.toString());
			int nbIteration = 0;
			long start = System.currentTimeMillis();
			boolean modified = false;
			do {

				modified = false;
				modified |= g.majPossibleValues();
				modified |= g.utiliserValeursSure();

				if (!modified) {
					// Arbre
					// trouver une case avec le moins de possibilités
					int[] lignecolonne = getLigneColonne(g);
					int ligne = lignecolonne[0];
					int colonne = lignecolonne[1];
					if (ligne == -1 && colonne == -1) {
						// Grille incorrecte, on remonte passe a l'enfant
						// suivant
						// si on etait sur le dernier enfant, on passe a
						// l'enfant
						// suivant du pere, recursivement
						while (g.parent.isDernierEnfantSelectionne()) {
							g = g.parent;
							if (g == null) {
								// on est remonté a la grille de base
								// Grille non soluble;
								// System.out.println(g.toString());
								System.out.println("Grille insoluble.");
								return;
							}
						}
						g = g.parent.prochainEnfant();
					} else {
						// Grille bloquée, on a choisi case, on cree les grilles
						// enfant pour cette case
						g.creerEnfants(ligne, colonne);

						// on place la grille de travail sur le premier enfant.
						g = g.prochainEnfant();

					}
					nbIteration++;
					System.out.println("Iteration " + nbIteration + ":\n" + g.toString());
				}
				while (!g.isValid()) {
					g = g.parent;
					if (g == null) {
						// on est remonté a la grille de base
						// Grille non soluble;
						// System.out.println(g.toString());
						System.out.println("Grille insoluble.");
						return;
					}
					Grille g2 = g.prochainEnfant();
					while (g2 == null) {
						g = g.parent;
						g2 = g.prochainEnfant();
					}
					g = g2;
				}
			} while (!g.isFinished());
			long end = System.currentTimeMillis();
			System.out.println(g.toString());
			System.out.println("Valide : " + g.isValid());
			System.out.println("Resolu en " + (end - start) + " ms et " + nbIteration + " iterations");
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Total en " + (endTime - startTime) + " ms");
	}

	private static int[] getLigneColonne(Grille g) {
		int minPos = 10;

		int ligne = -1;
		int colonne = -1;

		int countMinPos = 0;

		for (int l = 0; l < 9; l++) {
			for (int c = 0; c < 9; c++) {
				int size = g.grille[l][c].getPossibleValues().size();
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
		suite: for (int l = 0; l < 9; l++) {
			for (int c = 0; c < 9; c++) {
				int size = g.grille[l][c].getPossibleValues().size();
				if (size == minPos) {
					if ((countMinPos++) == id) {
						ligne = l;
						colonne = c;
						break suite;
					}
				}
			}
		}
		return new int[] { ligne, colonne };
	}
}
