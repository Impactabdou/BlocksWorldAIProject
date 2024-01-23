package cp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import modelling.Constraint;
import modelling.Variable;

//Méme fonction que le MACSolver classique mais en prenant en compte une heuristique.

public class HeuristicMACSolver extends AbstractSolver {

	private VariableHeuristic variableHeuristic;
	private ValueHeuristic valueHeuristic;
	private ArcConsistency arcConst = new ArcConsistency(this.getConstraint());

	public HeuristicMACSolver(Set<Variable> variable, Set<Constraint> constraint, VariableHeuristic variableHeuristic,
			ValueHeuristic valueHeuristic) {
		super(variable, constraint);
		this.valueHeuristic = valueHeuristic;
		this.variableHeuristic = variableHeuristic;
	}

	@Override
	public Map<Variable, Object> solve() {

		Map<Variable, Object> partialInsta = new HashMap<>();
		Map<Variable, Set<Object>> ed = new HashMap<>();
		Set<Variable> variable = new HashSet<>(this.getVariable());
		for (Variable x : this.getVariable()) {
			ed.put(x, x.getDomain());
		}
		return mac(partialInsta, variable, ed);
	}

	public Map<Variable, Object> mac(Map<Variable, Object> partialInstatiation, Set<Variable> variable,
			Map<Variable, Set<Object>> ed) {

		if (variable.isEmpty()) {
			return partialInstatiation;
		} else {
			if (!arcConst.ac1(ed)) {
				return null;
			}
			Variable x = variableHeuristic.best(variable, ed);
			variable.remove(x);
			List<Object> list = this.valueHeuristic.ordering(x, ed.get(x));
			for (Object v : list) {
				Map<Variable, Object> next = new HashMap<>(partialInstatiation);
				next.put(x, v);
				if (this.isConsistent(next)) {
					Map<Variable, Object> r = new HashMap<>();
					r = mac(next, variable, ed);
					if (r != null) {
						return r;
					}
				}
			}
			variable.add(x);
			return null;
		}
	}

}
