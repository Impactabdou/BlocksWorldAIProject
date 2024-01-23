package planning;

import java.util.Map;
import modelling.Variable;

public interface Heuristic {
	public float estimate(Map<Variable, Object> state);
}
