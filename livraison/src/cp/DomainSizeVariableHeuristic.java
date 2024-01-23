package cp;

import java.util.Map;
import java.util.Set;
import modelling.Variable;

public class DomainSizeVariableHeuristic implements VariableHeuristic {

	private boolean preference;

	public DomainSizeVariableHeuristic(boolean preference) {
		this.preference = preference;
	}

	@Override
	public Variable best(Set<Variable> set, Map<Variable, Set<Object>> ed) {

		if (set.isEmpty()) {
			return null;
		}

		Variable variable = null;
		int count = 0;
		int max = -1;
		int min = (int) (1.0 / 0.0);

		if (this.preference) {
			for (Variable v : set) {
				count = ed.get(v).size();
				if (count > max) {
					max = count;
					variable = v;
				}
			}
			return variable;
		} else {
			for (Variable v : set) {
				count = ed.get(v).size();
				if (count < min) {
					min = count;
					variable = v;
				}
			}
			return variable;
		}
	}

}
