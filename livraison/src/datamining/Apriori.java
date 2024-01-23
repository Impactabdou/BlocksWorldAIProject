package datamining;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import modelling.BooleanVariable;

public class Apriori extends AbstractItemsetMiner {

	public Apriori(BooleanDatabase booleanDatabase) {
		super(booleanDatabase);
	}

	// Recherche d'itemsets fréquents de taille 1
	public Set<Itemset> frequentSingletons(float frequency) {
		Set<Itemset> setItems = new HashSet<>();

		for (BooleanVariable item : this.getBooleanDatabase().getItems()) {
			int itemFrequency = 0;

			// Calcul de la fréquence de l'item dans les transactions
			for (Set<BooleanVariable> listItems : this.getBooleanDatabase().getTransactions()) {
				itemFrequency += Collections.frequency(listItems, item);
			}

			// Conversion en fréquence relative et vérification de la fréquence minimale
			float finlaItemFrequency = (float) itemFrequency / this.getBooleanDatabase().getTransactions().size();
			if (finlaItemFrequency >= frequency) {
				Set<BooleanVariable> set = new HashSet<>();
				set.add(item);
				Itemset itemset = new Itemset(set, finlaItemFrequency);
				setItems.add(itemset);
			}
		}
		return setItems;
	}

	// Cette méthode combine deux ensembles triés s'ils satisfont certaines
	// conditions
	public static SortedSet<BooleanVariable> combine(SortedSet<BooleanVariable> set1, SortedSet<BooleanVariable> set2) {

		if (set1.isEmpty() || set2.isEmpty()) {
			return null;
		}

		SortedSet<BooleanVariable> tempSet1 = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
		SortedSet<BooleanVariable> tempSet2 = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);

		tempSet1.addAll(set1);
		tempSet2.addAll(set2);

		BooleanVariable lastItem1 = set1.last();
		BooleanVariable lastItem2 = set2.last();

		boolean res1 = false;
		boolean res2 = false;
		boolean res3 = false;

		// Conditions pour la combinaison : taille, égalité, et différence
		// d'éléments
		if (set2.size() == set1.size() && set1.size() >= 1 && set2.size() >= 1) {
			res1 = true;
		}

		tempSet1.remove(lastItem1);
		tempSet2.remove(lastItem2);

		if (tempSet1.equals(tempSet2)) {
			res2 = true;
		}

		if (!lastItem1.equals(lastItem2)) {
			res3 = true;
		}

		// Si toutes les conditions sont remplies, les ensembles sont combinés
		if (res1 && res2 && res3) {
			SortedSet<BooleanVariable> set = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
			set.addAll(set1);
			set.addAll(set2);
			return set;
		}

		return null;
	}

	// Cette méthode vérifie si tous les sous-ensembles d'items sont fréquents
	public static boolean allSubsetsFrequent(Set<BooleanVariable> items, Collection<SortedSet<BooleanVariable>> set) {
		for (BooleanVariable item : items) {
			SortedSet<BooleanVariable> sous = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
			// Création de tous les sous-ensembles possibles en excluant un élément à la
			// fois
			for (BooleanVariable var : items) {
				if (!var.equals(item)) {
					sous.add(var);
				}
			}
			// Vérification si le sous-ensemble est fréquent.
			if (!set.contains(sous)) {
				return false;
			}
		}
		return true;
	}

	// Cette méthode principale extrait tous les itemsets fréquents de taille
	// supérieure à 1
	@Override
	public Set<Itemset> extract(float minFrequency) {

		Set<Itemset> res = new HashSet<>();
		Set<Itemset> start = new HashSet<>();
		start = this.frequentSingletons(minFrequency);
		res.addAll(start);
		List<SortedSet<BooleanVariable>> itemsets = new ArrayList<>();

		// Conversion des itemsets de taille 1 en ensembles triés.
		for (Itemset item : start) {
			SortedSet<BooleanVariable> set = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
			set.addAll(item.getItems());
			itemsets.add(set);
		}

		Set<Itemset> temp = new HashSet<>(res);

		while (true) {

			Set<Itemset> newItems = new HashSet<>();

			for (Itemset item1 : temp) {

				for (Itemset item2 : res) {

					SortedSet<BooleanVariable> combineset = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
					SortedSet<BooleanVariable> set1 = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
					SortedSet<BooleanVariable> set2 = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
					set1.addAll(item1.getItems());
					set2.addAll(item2.getItems());
					combineset = combine(set1, set2); // combine les deux ensembles set1 et set2

					if (combineset != null) {

						// Vérification si le nouvel itemset est fréquent, n'a pas été ajouté, et
						// respecte la fréquence minimale.
						if (allSubsetsFrequent(combineset, itemsets) && !itemsets.contains(combineset)
								&& frequency(combineset) >= minFrequency) {
							Itemset item = new Itemset(combineset, frequency(combineset));
							itemsets.add(combineset);
							newItems.add(item);
						}
					}
				}
			}

			// Si il n y'as aucun nouvelle element généré sort de la boucle
			if (newItems.isEmpty()) {
				break;
			}

			// Ajoute des nouveaux éléments dans le resultat
			res.addAll(newItems);
			temp = newItems;
		}

		return res;
	}

	@Override
	public BooleanDatabase getDatabase() {
		// TODO Auto-generated method stub
		return null;
	}
}
