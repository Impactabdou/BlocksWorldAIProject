package cp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import modelling.Constraint;
import modelling.Variable;

public class BacktrackSolver extends AbstractSolver {

	public BacktrackSolver(Set<Variable> variable, Set<Constraint> constraint) {
		super(variable, constraint);
	}

	// Cette méthode est appelée pour résoudre le CSP.
	@Override
	public Map<Variable, Object> solve() {
		// On initialise les arguments de la méthode BT.
		Map<Variable, Object> partialInstance = new HashMap<>();
		Set<Variable> variable = new HashSet<>(this.getVariable());
		// Commence le processus de retour en arrière à partir de l'instance partielle
		// initiale.
		return BT(partialInstance, variable);
	}

	// Implémentation de l'algorithme de retour en arrière.
	public Map<Variable, Object> BT(Map<Variable, Object> partialInstance, Set<Variable> variable) {

		// S'il n'y a plus de variables à affecter, le CSP est résolu.
		if (variable.isEmpty()) {
			return partialInstance;
		}

		// Sélectionne la prochaine variable à considérer.
		Variable var = variable.iterator().next();
		variable.remove(var);

		// Essaye d'affecter chaque valeur du domaine de la variable.
		for (Object domain : var.getDomain()) {
			// Crée une nouvelle instance partielle avec la variable affectée à une
			// valeur.
			Map<Variable, Object> next = new HashMap<>(partialInstance);
			next.put(var, domain);

			// Vérifie si la nouvelle affectation est cohérente avec les contraintes.
			if (isConsistent(next)) {
				// Si c'est cohérent, continue récursivement avec la prochaine variable.
				Map<Variable, Object> res = BT(next, variable);
				if (res != null) {
					return res; // Si une solution est trouvée, la renvoie.
				}
			}
		}
		// Si aucune affectation cohérente n'est trouvée, effectue un retour en
		// arrière.
		variable.add(var);
		return null; // Renvoie null si aucune solution partielle n'as été trouvée.
	}

}