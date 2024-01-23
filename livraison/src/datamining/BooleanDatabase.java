package datamining;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import modelling.BooleanVariable;

public class BooleanDatabase {
	private Set<BooleanVariable> items;
	private List<Set<BooleanVariable>> transactions;

	public BooleanDatabase(Set<BooleanVariable> items) {
		this.items = new HashSet<>(items);
		this.transactions = new ArrayList<>();
	}

	// Méthode pour ajouter une transaction a la base de données
	public void add(Set<BooleanVariable> items) {
		this.transactions.add(items);
	}

	public Set<BooleanVariable> getItems() {
		return items;
	}

	public List<Set<BooleanVariable>> getTransactions() {
		return transactions;
	}
}
