package cp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import modelling.Constraint;
import modelling.Variable;

public class ArcConsistency {

	// Ensemble de contraintes
	private Set<Constraint> constraint = new HashSet<>();

	// Constructeur de la classe ArcConsistency
	public ArcConsistency(Set<Constraint> constraint) {
		// Ajoute les contraintes unaires et binaires à l'ensemble de contraintes
		for (Constraint c : constraint) {
			Set<Variable> scope = c.getScope();
			if (scope.size() == 1) {
				this.constraint.add(c);
			} else if (scope.size() == 2) {
				this.constraint.add(c);
			} else {
				// Lève une exception si la contrainte n'est ni unaire ni binaire
				throw new IllegalArgumentException("Ni unaire ni binaire");
			}
		}
	}

	// ed est un ensemble de variables avec chacune leur propore domaine.
	public boolean enforceNodeConsistency(Map<Variable, Set<Object>> ed) {

		// Parcours de chaque variable et son domaine.
		for (Map.Entry<Variable, Set<Object>> map : ed.entrySet()) {
			for (Object v : new HashSet<Object>(map.getValue())) {
				for (Constraint c : this.constraint) { // Parcour des contraintes
					// Vérifie si la contrainte est unaire et concerne la variable actuelle
					if (c.getScope().size() == 1 && c.getScope().contains(map.getKey())) {
						// Crée une instanciation partielle avec la variable et la valeur
						Map<Variable, Object> init = new HashMap<>();
						init.put(map.getKey(), v);
						if (!c.isSatisfiedBy(init)) {
							// a refaire faut pas directement copier
							// Supprime la valeur du domaine si l'instanciation partielle init ne satisfait
							// pas la contrainte unaire
							ed.get(map.getKey()).remove(v);
						}
					}
				}
			}
		}
		// Vérifie si tous les domaines de variables sont non vides
		for (Map.Entry<Variable, Set<Object>> map : ed.entrySet()) {
			if (ed.get(map.getKey()).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	// Révise les domaines de deux variables v1 et v2
	public boolean revise(Variable v1, Set<Object> d1, Variable v2, Set<Object> d2) {
		boolean del = false;
		Set<Object> temp = new HashSet<>();

		// Parcours du domaine de la première variable
		for (Object vi : d1) {
			boolean viable = false;
			// Parcours du domaine de la deuxième variable
			for (Object vj : d2) {
				boolean satisfy = true;
				for (Constraint c : constraint) {
					// Vérifie si la contrainte est binaire et concerne les deux variables v1 et v2
					if (c.getScope().size() == 2 && c.getScope().contains(v1) && c.getScope().contains(v2)) {
						// Crée une instanciation partielle avec les deux variables et les deux valeurs
						Map<Variable, Object> n = new HashMap<>();
						n.put(v1, vi);
						n.put(v2, vj);
						// Vérifie si la contrainte est satisfaite si oui on sort de la boucle
						if (!c.isSatisfiedBy(n)) {
							// Si l'instanciation ne satisfait pas la contraintes on sort de la 3eme boucle
							satisfy = false;
							break;
						}
					}
				} // On sort de la 2eme boucle
				if (satisfy) {
					viable = true;
					break;
				}
			} // On se souvien des elements du domaine qu'on supprimera plus tard
			if (!viable) {
				temp.add(vi);
				del = true;
			}
		}
		// Supprime les valeurs du domaine de la première variable
		for (Object d : temp) {
			d1.remove(d);
		}
		return del;
	}

	public boolean ac1(Map<Variable, Set<Object>> ed) {
		// On supprime les valeurs v dans les domaines qui ne vérifient pas les
		// contraintes unaires.
		if (!enforceNodeConsistency(ed)) {
			return false;
		}

		boolean change = true;
		do {
			change = false;
			for (Variable vi : ed.keySet()) {
				for (Variable vj : ed.keySet()) { // On prend un couple de variables vi et vj de sorte que vi != vj et
													// on les révise
					if (!vi.equals(vj)) {
						if (revise(vi, vi.getDomain(), vj, vj.getDomain())) {
							change = true;
						}
					}
				}
			}
		} while (change);

		// Vérifier si une des variables a un domaine vide
		for (Variable v : ed.keySet()) {
			if (v.getDomain().isEmpty()) {
				return false;
			}
		}
		// Si le filtrage est réussi on renvoie True
		return true;
	}

}
