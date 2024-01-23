package datamining;

import java.util.Set;
import modelling.BooleanVariable;

public interface AssociationRuleMiner {
	BooleanVariable getDatabase();

	Set<AssociationRule> extract(float frequency, float confidence);
}
