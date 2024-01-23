package cp;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import modelling.Constraint;
import modelling.Variable;

public abstract class AbstractSolver implements Solver {

	// initialisation de nos variables et nos contraintes.
	private Set<Variable> variable = new HashSet<>();
	private Set<Constraint> constraint = new HashSet<>();

	public AbstractSolver(Set<Variable> variable, Set<Constraint> constraint) {
		this.variable = variable;
		this.constraint = constraint;
	}

	public boolean isConsistent(Map<Variable, Object> partialInstance) { // algorithme
		// On parcours toutes nos contraintes et on vérifie si les instanciations
		// partielles donnée
		// en argument les satisfaient.
		for (Constraint c : constraint) {
			// on vérifie d'abord si les variables de notre instanciation
			// sont présentent dans la portée de chacune de nos contraintes.
			if (partialInstance.keySet().containsAll(c.getScope())) {
				// vérification de la satisfaction de la contrainte
				if (!c.isSatisfiedBy(partialInstance)) {
					return false;
				}
			}
		}
		return true;
	}

	// Setter et getter.

	public Set<Variable> getVariable() {
		return variable;
	}

	public void setVariable(Set<Variable> variable) {
		this.variable = variable;
	}

	public Set<Constraint> getConstraint() {
		return constraint;
	}

	public void setConstraint(Set<Constraint> constraint) {
		this.constraint = constraint;
	}

}
