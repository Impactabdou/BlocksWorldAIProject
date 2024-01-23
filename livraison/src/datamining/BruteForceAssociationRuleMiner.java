package datamining;

import java.util.HashSet;
import java.util.Set;
import modelling.BooleanVariable;

public class BruteForceAssociationRuleMiner extends AbstractAssociationRuleMiner {

	public BruteForceAssociationRuleMiner(BooleanDatabase booleanDataBase) {
		super(booleanDataBase);
	}

	// Méthode pour générer tous les candidats pour la prémisse
	public static Set<Set<BooleanVariable>> allCandidatePremises(Set<BooleanVariable> items) {
		Set<Set<BooleanVariable>> set = new HashSet<>();
		// Vérifie si la liste d'items est vide ou ne contient pas suffisamment d'items
		if (items.isEmpty() || items.size() < 2) {
			return set;
		}

		for (BooleanVariable item : items) {
			// Crée un ensemble temporaire contenant un seul item de la liste
			Set<BooleanVariable> temp = new HashSet<>();
			temp.add(item);
			// Ajoute cet ensemble temporaire à l'ensemble des candidats
			set.add(temp);
		}

		while (true) {
			boolean stop = false;
			// Crée une copie temporaire de l'ensemble actuel de candidats
			Set<Set<BooleanVariable>> temp = new HashSet<>(set);
			for (Set<BooleanVariable> itemSet : temp) {
				for (BooleanVariable item : items) {
					if (!itemSet.contains(item)) {
						// Crée un nouvel ensemble en ajoutant un item qui n'est pas déjà présent
						Set<BooleanVariable> newSet = new HashSet<>(itemSet);
						newSet.add(item);
						// Vérifie si l'ensemble atteint la taille maximale d'items
						if (newSet.size() == items.size()) {
							stop = true;
						}
						// Ajoute le nouvel ensemble à l'ensemble des candidats
						set.add(newSet);
					}
				}
			}
			// Sort de la boucle si l'ensemble atteint la taille maximale
			if (stop) {
				break;
			}
		}

		// Retire l'ensemble original des candidats (il n'est pas utilisé comme
		// prémisse)
		set.remove(items);
		return set;
	}

	// Méthode pour extraire les règles d'association de fréquence et confiance
	// supérieures aux seuils donnés
	@Override
	public Set<AssociationRule> extract(float frequency, float confidence) {
		Set<AssociationRule> res = new HashSet<>();
		// Crée une instance d'Apriori pour extraire les itemsets fréquents
		Apriori ap = new Apriori(getBooleanDataBase());
		Set<Itemset> items = new HashSet<>(ap.extract(frequency));

		for (Itemset premise : items) {
			Set<Set<BooleanVariable>> subItem = new HashSet<>();
			// Génère tous les candidats pour la prémisse
			subItem = allCandidatePremises(premise.getItems());

			for (Set<BooleanVariable> conclusion : subItem) {
				Set<BooleanVariable> temp = new HashSet<>(premise.getItems());
				temp.removeAll(conclusion);

				// Vérifie si la règle satisfait les conditions de fréquence et de confiance
				if (frequency(premise.getItems(), items) / frequency(temp, items) >= confidence) {
					// Crée une règle d'association et l'ajoute à l'ensemble des règles
					// extraites
					AssociationRule association = new AssociationRule(temp, conclusion,
							frequency(premise.getItems(), items), confidence(temp, conclusion, items));
					res.add(association);
				}
			}
		}

		return res;
	}

	@Override
	public BooleanVariable getDatabase() {
		// TODO Auto-generated method stub
		return null;
	}

}
