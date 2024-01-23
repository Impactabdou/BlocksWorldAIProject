package cp;

import java.util.Map;
import modelling.Variable;

public interface Solver {

	Map<Variable, Object> solve();
}
