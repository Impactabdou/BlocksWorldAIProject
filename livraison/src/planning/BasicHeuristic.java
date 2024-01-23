package planning;

import java.util.Map;

import modelling.Variable;

public class BasicHeuristic implements Heuristic {
	private BasicGoal goal;

	public BasicHeuristic(BasicGoal goal) {
		this.goal = goal;
	}

	@Override
	public float estimate(Map<Variable, Object> state) {
		if (goal.getGoal().equals(state)) {
			return 0;
		}
		return 1;
	}

}
