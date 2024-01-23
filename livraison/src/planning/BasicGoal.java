package planning;

import java.util.HashMap;
import java.util.Map;
import modelling.Variable;

public class BasicGoal implements Goal {
	private Map<Variable, Object> goal;

	public BasicGoal(Map<Variable, Object> goal) {
		this.goal = goal;
	}

	@Override
	public boolean isSatisfiedBy(Map<Variable, Object> etat) {
		Map<Variable, Object> temp = new HashMap<>();
		etat.forEach((key, value) -> {
			if (goal.containsKey(key)) {
				temp.put(key, value);
			}
		});
		return goal.equals(temp);
	}

	public Map<Variable, Object> getGoal() {
		return goal;
	}

	public void setGoal(Map<Variable, Object> goal) {
		this.goal = goal;
	}

	@Override
	public String toString() {
		return " goal : " + goal.toString();
	}

}
