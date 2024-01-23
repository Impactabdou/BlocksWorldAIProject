package cp;

import java.util.List;
import java.util.Set;

import modelling.Variable;

public interface ValueHeuristic {
	List<Object> ordering(Variable variable, Set<Object> domain);

}
