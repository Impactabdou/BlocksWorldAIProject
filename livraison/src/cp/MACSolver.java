package cp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import modelling.Constraint;
import modelling.Variable;

public class MACSolver extends AbstractSolver {

	// Instance de la classe ArcConsistency afin de faire appel a la méthode ac1
	// pour filtrer les domaines
	private ArcConsistency arcConst = new ArcConsistency(this.getConstraint());

	// Constructeur de la classe MACSolver
	public MACSolver(Set<Variable> variable, Set<Constraint> constraint) {
		super(variable, constraint);
	}

	@Override
	public Map<Variable, Object> solve() {

		// initialisation des arguments de la méthode mac
		Map<Variable, Object> partialInsta = new HashMap<>(); // instanciation partielle
		Map<Variable, Set<Object>> ed = new HashMap<>(); // ensemble de domaines
		Set<Variable> variable = new HashSet<>(this.getVariable()); // ensembles de variables
		// On remplie de ensembles de domaines {variable : son domaine}
		for (Variable x : this.getVariable()) {
			ed.put(x, x.getDomain());
		}
		// On fait appel a la méthode mac avec les argument ci-dessus afin de résoudre
		// le probléme.
		return mac(partialInsta, variable, ed);
	}

	public Map<Variable, Object> mac(Map<Variable, Object> partialInstatiation, Set<Variable> variable,
			Map<Variable, Set<Object>> ed) {

		// si il n y'as aucune variable donc la solution est l'instance elle méme.
		if (variable.isEmpty()) {
			return partialInstatiation;
		} else {
			// Applique ac1 pour réduire les domaines (les filtrer).
			if (!arcConst.ac1(ed)) {
				return null; // Si ac1 échoue donc il n y'as pas de solution
			}

			// On défile une variable.
			Variable x = variable.iterator().next();
			variable.remove(x);

			// Parcours des valeurs dans le domaine de la variable sélectionnée
			for (Object v : ed.get(x)) {
				// Crée une nouvelle instanciation partielle avec la variable et sa n éme
				// valeur
				Map<Variable, Object> next = new HashMap<>(partialInstatiation);
				next.put(x, v);
				// Vérifie si l'instanciation partielle est cohérente
				if (this.isConsistent(next)) {
					// Récursivement appelle MAC pour la prochaine variable
					Map<Variable, Object> res = new HashMap<>();
					res = mac(next, variable, ed);
					if (res != null) {
						return res; // Retourne une instanciation compléte qui représente une solution pour le
									// probléme
					}
				}
			}

			// Si aucune instanciation complète n'a été trouvée, restaure la variable et
			// ne retourne rien
			variable.add(x);
			return null;
		}
	}

}
