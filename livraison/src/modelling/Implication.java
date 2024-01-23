package modelling;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Implication implements Constraint {
	private Variable var1, var2;
	private Set<Object> domain1, domain2;

	public Implication(Variable var1, Set<Object> domain1, Variable var2, Set<Object> domain2) {
		this.domain1 = domain1;
		this.domain2 = domain2;
		this.var1 = var1;
		this.var2 = var2;
	}

	@Override
	public Set<Variable> getScope() {
		return new HashSet<Variable>(Arrays.asList(this.var1, this.var2));
	}

	@Override
	public boolean isSatisfiedBy(Map<Variable, Object> instantiation) {
		if (!instantiation.containsKey(var1) || !instantiation.containsKey(var2)) {
			throw new IllegalArgumentException("null");
		}
		// a=>b == !a || b
		return !this.domain1.contains(instantiation.get(var1)) || this.domain2.contains(instantiation.get(var2));
	}

	@Override
	public String toString() {
		return this.var1 + ":" + this.domain1 + "=>" + this.var2 + ":" + this.domain2;
	}
}
