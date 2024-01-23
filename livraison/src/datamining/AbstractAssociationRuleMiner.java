package datamining;

import java.util.HashSet;
import java.util.Set;
import modelling.BooleanVariable;

public abstract class AbstractAssociationRuleMiner implements AssociationRuleMiner {
	// Base de données booléenne utilisée pour l'extraction des règles
	// d'association
	private BooleanDatabase booleanDataBase;

	// Constructeur de la classe abstraite
	public AbstractAssociationRuleMiner(BooleanDatabase booleanDataBase) {
		this.booleanDataBase = booleanDataBase;
	}

	// Accesseur pour obtenir la base de données booléenne
	public BooleanDatabase getBooleanDataBase() {
		return booleanDataBase;
	}

	public void setBooleanDataBase(BooleanDatabase booleanDB) {
		this.booleanDataBase = booleanDB;
	}

	// Méthode pour calculer la fréquence d'un ensemble d'items dans un ensemble
	// d'itemsets fréquents
	public static float frequency(Set<BooleanVariable> items, Set<Itemset> frequent) {
		for (Itemset set : frequent) {
			if (set.getItems().equals(items)) {
				return set.getFrequency();
			}
		}
		return 0;
	}

	// Méthode pour calculer la confiance d'une règle d'association
	public static float confidence(Set<BooleanVariable> premise, Set<BooleanVariable> conclusion, Set<Itemset> items) {
		Set<BooleanVariable> variables = new HashSet<>();
		variables.addAll(conclusion);
		variables.addAll(premise);
		// La confiance est calculée en utilisant la fréquence des items impliqués
		// dans la règle
		return frequency(variables, items) / frequency(premise, items);
	}
}
