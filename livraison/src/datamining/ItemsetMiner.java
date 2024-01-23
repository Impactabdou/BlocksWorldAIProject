package datamining;

import java.util.Set;

public interface ItemsetMiner {
	BooleanDatabase getDatabase();

	Set<Itemset> extract(float minFrequency);
}
