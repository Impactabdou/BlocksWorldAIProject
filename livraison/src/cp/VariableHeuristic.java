package cp;

import java.util.Map;
import java.util.Set;

import modelling.Variable;

public interface VariableHeuristic {
	Variable best(Set<Variable> set, Map<Variable, Set<Object>> ed);
}
