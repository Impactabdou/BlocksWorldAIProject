package datamining;

import java.util.Comparator;
import java.util.Set;
import modelling.BooleanVariable;

public abstract class AbstractItemsetMiner implements ItemsetMiner {
	// Comparateur pour trier les variables booléennes
	public static final Comparator<BooleanVariable> COMPARATOR = (var1, var2) -> var1.getName()
			.compareTo(var2.getName());
	// Base de données booléenne utilisée pour l'extraction d'itemsets
	private final BooleanDatabase booleanDatabase;

	public AbstractItemsetMiner(BooleanDatabase booleanDatabase) {
		this.booleanDatabase = booleanDatabase;
	}

	public BooleanDatabase getBooleanDatabase() {
		return this.booleanDatabase;
	}

	// Méthode pour calculer la fréquence d'un ensemble d'items
	public float frequency(Set<BooleanVariable> booleanVariable) {
		float count = 0;
		// Parcourt chaque transaction dans la base de données
		for (Set<BooleanVariable> variables : this.booleanDatabase.getTransactions()) {
			// Vérifie si la transaction contient tous les items de l'ensemble
			if (variables.containsAll(booleanVariable)) {
				count++;
			}
		}

		float transactionNb = this.booleanDatabase.getTransactions().size();
		// La fréquence est le ratio entre le nombre de transactions contenant
		// l'ensemble d'items
		// et le nombre total de transactions dans la base de données
		return (transactionNb > 0) ? (count / transactionNb) : 0;
	}

}
