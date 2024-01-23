package modelling;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UnaryConstraint implements Constraint {
	private Variable var;
	private Set<Object> domain;

	public UnaryConstraint(Variable var, Set<Object> domain) {
		this.var = var;
		this.domain = domain;

	}

	@Override
	public Set<Variable> getScope() {
		return new HashSet<Variable>(Arrays.asList(this.var));
	}

	@Override
	public boolean isSatisfiedBy(Map<Variable, Object> instantiation) {
		if (!instantiation.containsKey(var)) {
			throw new IllegalArgumentException("null");
		}
		// verifier si domaine est inclus dans le domaine de var
		return domain.contains(instantiation.get(var));
	}

	@Override
	public String toString() {
		return var.toString() + ":" + this.domain.toString();
	}

}
