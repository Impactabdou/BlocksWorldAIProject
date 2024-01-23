package modelling;

import java.util.Set;

public class Variable {
	private String name;
	private Set<Object> domain;

	public Variable(String name, Set<Object> domain) {
		this.name = name;
		this.domain = domain;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Variable)) {
			return false;
		}
		// utilser equals pour verifier deux String
		return this.name.equals(((Variable) obj).getName());
	}

	@Override
	public int hashCode() {
		return (getName() == null ? 0 : getName().hashCode());
	}

	public String getName() {
		return this.name;
	}

	public Set<Object> getDomain() {
		return this.domain;
	}

	@Override
	public String toString() {
		return this.getName();
	}

}
