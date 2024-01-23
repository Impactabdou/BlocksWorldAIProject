package cp;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import modelling.Constraint;
import modelling.Variable;

public class NbConstraintsVariableHeuristic implements VariableHeuristic {

	// Préférence pour sélectionner la variable avec le plus ou le moins de
	// contraintes
	private boolean prefrence;
	private Set<Constraint> constraint;

	public NbConstraintsVariableHeuristic(Set<Constraint> constraint, boolean prefrence) {
		this.prefrence = prefrence;
		this.constraint = new HashSet<>(constraint);
	}

	// Sélectionne la meilleure variable à assigner
	@Override
	public Variable best(Set<Variable> set, Map<Variable, Set<Object>> ed) {
		if (set.isEmpty()) {
			return null; // Aucune variable disponible
		}

		int count = 0;
		Variable variable = null;
		int max = -1;
		int min = (int) (1.0 / 0.0);

		// Préférence pour le maximum de contraintes
		if (this.prefrence) {
			for (Variable v : set) {
				count = 0;
				for (Constraint c : this.constraint) {
					count += Collections.frequency(c.getScope(), v);
				}
				if (count > max) {
					max = count;
					variable = v;
				}
			}
			return variable; // Retourne la variable avec le plus de contraintes
		} else {
			// Préférence pour le minimum de contraintes
			for (Variable v : set) {
				count = 0;
				for (Constraint c : this.constraint) {
					count = Collections.frequency(c.getScope(), v) + count;
				}
				if (count < min) {
					min = count;
					variable = v;
				}
			}
			return variable; // Retourne la variable avec le moins de contraintes
		}

	}

}
