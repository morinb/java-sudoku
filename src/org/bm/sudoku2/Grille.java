package org.bm.sudoku2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grille {

	public Grille parent;
	public Grille[] enfants;
	// index de l'enfant selectionné
	public int enfantId;

	public Grille prochainEnfant() {
		if (enfants == null)
			return null;
		if (isDernierEnfantSelectionne()) {
			return null;
		}
		return enfants[enfantId++];
	}

	public boolean isDernierEnfantSelectionne() {
		if (enfants == null) {
			return true;
		}
		return enfantId == enfants.length;
	}

	public void creerEnfants(int ligne, int colonne) {
		List<Integer> pv = grille[ligne][colonne].getPossibleValues();
		enfants = new Grille[pv.size()];
		enfantId = 0;
		for (int i = 0; i < pv.size(); i++) {
			Integer g = pv.get(i);
			// Creation nouvelle grille.
			Grille nouvelleGrille = new Grille(this);
			nouvelleGrille.grille[ligne][colonne].setValue(g);
			nouvelleGrille.majPossibleValues();

			enfants[i] = nouvelleGrille;
		}
	}

	public static final Integer[] values = new Integer[] { new Integer(0), new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), };
	// Case[ligne][colonne]
	protected Case[][] grille = new Case[9][9];

	public Case[] getLigne(int l) {
		Case[] ligne = new Case[9];
		for (int c = 0; c < 9; c++) {
			ligne[c] = grille[l][c];
		}

		return ligne;
	}

	public Case[] getColonne(int c) {
		Case[] colonne = new Case[9];
		for (int l = 0; l < 9; l++) {
			colonne[l] = grille[l][c];

		}
		return colonne;
	}

	public Case[][] getBloc(int b) {
		Case[][] bloc = new Case[3][3];
		for (int l = 0; l < 9; l++) {
			for (int c = 0; c < 9; c++) {
				int i = l * 9 + c;
				if (b == (i % 9 / 3 + i / 27 * 3)) {
					bloc[l % 3][c % 3] = grille[l][c];
				}
			}
		}

		return bloc;

	}

	public void set(int l, int c, Integer value) {
		grille[l][c] = new Case(value);
	}

	private Grille(Grille grille) {
		this();
		this.parent = grille;
		for (int l = 0; l < 9; l++) {
			for (int c = 0; c < 9; c++) {
				this.grille[l][c] = new Case(grille.grille[l][c]);
			}
		}
	}

	private Grille() {
		for (int l = 0; l < 9; l++) {
			for (int c = 0; c < 9; c++) {
				grille[l][c] = null;
			}
		}
	}

	public static Grille parse(String input) {
		if (input.length() != 81) {
			throw new IllegalArgumentException("datas must be 81 chars long.");
		}

		Grille g = new Grille();

		char[] datas = input.toCharArray();
		for (int l = 0; l < 9; l++) {
			for (int c = 0; c < 9; c++) {
				int i = l * 9 + c;

				char car = datas[i];
				if ('.' == car) {
					g.set(l, c, values[0]);
				} else {
					g.set(l, c, values[Integer.parseInt(Character.toString(car))]);
				}
			}
		}

		return g;
	}

	public void setDisplayPossibleValues(boolean display) {
		for (int l = 0; l < grille.length; l++) {
			for (int c = 0; c < grille[l].length; c++) {
				grille[l][c].setDisplayPossibleValues(display);
			}
		}
	}

	public boolean isDisplayPossibleValues() {
		return grille[0][0].isDisplayPossibleValues();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		int colLength = 1;
		if (isDisplayPossibleValues()) {
			colLength = 27;
		}

		int l = 0;
		for (Case[] ligne : grille) {
			if (l % 3 == 0) {
				sb.append(afficherTirets(colLength)).append('\n');
			}
			sb.append(afficherLigne(ligne));
			sb.append("\n");
			l++;
		}
		sb.append(afficherTirets(colLength));
		return sb.toString();
	}

	/**
	 * Methode utilitaire permettant d'afficher un nombre suffisant de tiret
	 * pour separer les blocs.
	 * 
	 * @param colLength
	 * @return
	 */
	private StringBuilder afficherTirets(int colLength) {
		int tirets = 9 * (colLength) + 16;
		StringBuilder temp = new StringBuilder();
		temp.append(tirets(tirets));
		temp.setCharAt(0, '+');
		temp.setCharAt(3 * colLength + 4 + 1, '+');
		temp.setCharAt(6 * colLength + 9 + 1, '+');
		temp.setCharAt(9 * colLength + 14 + 1, '+');
		return temp;
	}

	/**
	 * Cree une String de <code>tirets</code> tirets.
	 * 
	 * @param tirets
	 * @return
	 */
	private StringBuilder tirets(int tirets) {
		StringBuilder temp = new StringBuilder();
		for (int t = 0; t < tirets; t++) {
			temp.append('-');
		}

		return temp;
	}

	/**
	 * Affiche une ligne extraite de la grille
	 * 
	 * @param ligne
	 * @return
	 */
	public String afficherLigne(Case[] ligne) {
		StringBuilder sb = new StringBuilder();
		for (int c = 0; c < ligne.length; c++) {
			Case cas = ligne[c];
			if (c % 3 == 0) {
				sb.append("| ");
			}
			sb.append(cas.toString()).append(" ");
		}

		sb.append("|");
		return sb.toString();
	}

	/**
	 * Affiche une colonne extraite de la grille
	 * 
	 * @param colonne
	 * @return
	 */
	public String afficherColonne(Case[] colonne) {
		int colLength = 1;
		if (isDisplayPossibleValues()) {
			colLength = 27;
		}

		StringBuilder sb = new StringBuilder();
		for (int l = 0; l < colonne.length; l++) {
			Case cas = colonne[l];
			if (l % 3 == 0) {
				sb.append(tirets(colLength)).append('\n');
			}
			sb.append(cas.toString()).append('\n');
		}
		sb.append('-');
		return sb.toString();
	}

	/**
	 * Affiche un bloc extrait de la grille
	 * 
	 * @param bloc
	 * @return
	 */
	public String afficherBloc(Case[][] bloc) {
		StringBuilder sb = new StringBuilder();
		for (int l = 0; l < bloc.length; l++) {
			for (int c = 0; c < bloc[l].length; c++) {
				Case cas = bloc[l][c];
				sb.append(cas.toString()).append(' ');

			}
			sb.append('\n');
		}

		return sb.toString();
	}

	/**
	 * Pour chaque case, on va reduire les valeurs possible en enlevant les
	 * valeur deja ecrite pour chaque ligne, colonne, bloc.
	 * 
	 * @return si le sudoku a subi une modification
	 */
	public boolean majPossibleValues() {
		boolean modified = false;
		// supprime des valeurs possible de chaque case d'une ligne les valeurs
		// sures.
		List<Integer> valeursSuresLigne = new ArrayList<Integer>();
		List<Integer> valeursSuresColonne = new ArrayList<Integer>();
		List<Integer> valeursSuresBloc = new ArrayList<Integer>();
		for (int i = 0; i < grille.length; i++) {
			Case[] ligne = getLigne(i);
			Case[] colonne = getColonne(i);
			Case[][] bloc = getBloc(i);

			valeursSuresLigne = calculerValeursSures(ligne);
			valeursSuresColonne = calculerValeursSures(colonne);
			valeursSuresBloc = calculerValeursSures(bloc);

			for (Case cas : ligne) {
				if (!cas.getPossibleValues().isEmpty()) {
					modified |= cas.removeFromPossibleValues(valeursSuresLigne);
				}
			}
			utiliserValeursSure();

			for (Case cas : colonne) {
				if (!cas.getPossibleValues().isEmpty()) {
					modified |= cas.removeFromPossibleValues(valeursSuresColonne);
				}
			}
			utiliserValeursSure();
			for (Case[] line : bloc) {
				for (Case cas : line) {
					if (!cas.getPossibleValues().isEmpty()) {
						modified |= cas.removeFromPossibleValues(valeursSuresBloc);
					}
				}
			}
			utiliserValeursSure();
		}

		return modified;
	}

	/**
	 * Verifie qu'une case n'a pas une seule valeur possible. si c'est le cas,
	 * la case devient une valeurs sur, et sa liste de valeur possible est
	 * vidée.
	 * 
	 * @return
	 */
	public boolean utiliserValeursSure() {
		boolean modified = false;
		for (int l = 0; l < grille.length; l++) {
			for (int c = 0; c < grille[0].length; c++) {
				grille[l][c].utiliserValeurSure();
			}
		}

		return modified;
	}

	private List<Integer> calculerValeursSures(Case[] liste) {
		// construction de la liste des valeurs sures
		List<Integer> valeursSures = new ArrayList<Integer>();
		for (Case cas : liste) {
			if (cas.getPossibleValues().isEmpty()) {
				valeursSures.add(cas.getValue());
			}
		}

		return Collections.unmodifiableList(valeursSures);
	}

	private List<Integer> calculerValeursSures(Case[][] liste) {
		return calculerValeursSures(toOneDimensionList(liste));
	}

	private Case[] toOneDimensionList(Case[][] bloc) {
		int nbLignes = bloc.length;
		int nbColonnes = bloc[0].length;
		int size = nbLignes * nbColonnes;
		Case[] oneD = new Case[size];

		int i = 0;
		for (int l = 0; l < nbLignes; l++) {
			for (int c = 0; c < nbColonnes; c++) {
				oneD[i++] = bloc[l][c];
			}
		}

		return oneD;
	}
/*
	private String listToString(List<Integer> list) {
		StringBuilder sb = new StringBuilder();
		for (Integer i : list) {
			sb.append(i).append(' ');
		}

		return sb.toString();
	}
*/
	public boolean isValid() {
		boolean valid = true;
		// verifie aussi qu'il n'y a pas 2fois le meme chiffre dans la meme
		// ligne colonne bloc.
		for (int i = 0; i < 9; i++) {
			Case[] ligne = getLigne(i);
			valid &= validate(ligne);
			if (!valid) {
				System.out.println(" dans la ligne");
				return false;
			}

			Case[] colonne = getColonne(i);
			valid &= validate(colonne);
			if (!valid) {System.out.println(" dans la colonne");
				return false;
			}

			Case[] bloc = toOneDimensionList(getBloc(i));
			valid &= validate(bloc);
			if (!valid) {System.out.println(" dans le bloc");
				return false;
			}
		}

		return valid;
	}

	private boolean validate(Case[] ligne) {
		int[] vals = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 1; i <= 9; i++) {
			Integer j = Grille.values[i];
			for (Case c : ligne) {
				if (c.getValue() == j) {
					vals[i - 1]++;
				}
			}
		}

		// verifies que les vals soient au maximum a 1
		for(int i = 0 ; i < vals.length ; i++) {
			if (vals[i] > 1) {
				System.out.print(afficherLigne(ligne)+" invalide : plusieurs "+Grille.values[i+1].toString());
				return false;
			}
		}

		return true;
	}

	private int sum(Case[] list) {
		int sum = 0;
		for (Case c : list) {
			sum += c.getValue();
		}
		return sum;
	}

	public boolean isFinished() {
		for (int i = 0; i < 9; i++) {
			Case[] ligne = getLigne(i);
			if (45 != sum(ligne)) {
				return false;
			}

			Case[] colonne = getColonne(i);
			if (45 != sum(colonne)) {
				return false;
			}

			Case[] bloc = toOneDimensionList(getBloc(i));
			if (45 != sum(bloc)) {
				return false;
			}
		}
		return true;
	}
}
