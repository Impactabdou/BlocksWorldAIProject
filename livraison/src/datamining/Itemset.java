package datamining;

import java.util.HashSet;
import java.util.Set;
import modelling.BooleanVariable;

public class Itemset {
	private float frequency;
	private Set<BooleanVariable> items;

	public Itemset(Set<BooleanVariable> items, float frequency) {
		this.frequency = frequency;
		this.items = items;
	}

	public float getFrequency() {
		return frequency;
	}

	public Set<BooleanVariable> getItems() {
		if (items == null) {
			return new HashSet<BooleanVariable>();
		}
		return items;
	}

	public void setItems(Set<BooleanVariable> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return this.items.toString();
	}

}
