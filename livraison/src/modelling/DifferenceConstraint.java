package modelling;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DifferenceConstraint implements Constraint {
	private Variable v1, v2;

	public DifferenceConstraint(Variable v1, Variable v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public Set<Variable> getScope() {
		return new HashSet<Variable>(Arrays.asList(this.v1, this.v2));
	}

	@Override
	public boolean isSatisfiedBy(Map<Variable, Object> instantiation) {
		// verifier si l'instantiation contient v1 et v2
		if (!instantiation.containsKey(v1) && !instantiation.containsKey(v2)) {
			throw new IllegalArgumentException("null");
		}
		// verifier si v1=v2
		return instantiation.get(v1) != instantiation.get(v2);
	}

	public Variable getV1() {
		return v1;
	}

	public void setV1(Variable v1) {
		this.v1 = v1;
	}

	public Variable getV2() {
		return v2;
	}

	public void setV2(Variable v2) {
		this.v2 = v2;
	}

	@Override
	public String toString() {
		return this.v1 + "=/=" + this.v2;
	}

}
