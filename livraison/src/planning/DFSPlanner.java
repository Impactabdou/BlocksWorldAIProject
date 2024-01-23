package planning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modelling.Variable;

public class DFSPlanner implements Planner {
	public Map<Variable, Object> initState;
	public Set<Action> action;
	public Goal goal;
	private int count;
	private boolean activate;

	public DFSPlanner(Map<Variable, Object> initState, Set<Action> action, Goal goal) {
		this.initState = initState;
		this.action = action;
		this.goal = goal;
		this.activate = false;
		this.count = 0;
	}

	@Override
	public void activateNodeCount(boolean activate) {
		this.activate = activate;
	}

	public List<Action> dfs(Map<Variable, Object> state, List<Action> plan, HashSet<Map<Variable, Object>> closed) {
		if (getGoal().isSatisfiedBy(state)) {
			return plan;
		} else {
			for (Action action : action) {
				if (action.isApplicable(state)) {
					Map<Variable, Object> next = action.successor(state);
					if (!closed.contains(next)) {
						if (this.activate) {
							this.count++;
						}
						plan.add(action);
						closed.add(next);
						List<Action> subPlan = dfs(next, plan, closed);
						if (!(subPlan == null)) {
							return subPlan;
						} else {
							plan.remove(plan.size() - 1);
						}
					}
				}
			}
			return null;
		}
	}

	@Override
	public List<Action> plan() {
		HashSet<Map<Variable, Object>> closed = new HashSet<>();
		List<Action> plan = new ArrayList<>();
		return dfs(getInitialState(), plan, closed);
	}

	@Override
	public Map<Variable, Object> getInitialState() {
		return initState;
	}

	@Override
	public Set<Action> getAction() {
		return action;
	}

	@Override
	public Goal getGoal() {
		return goal;
	}

	@Override
	public int getCount() {
		return this.count;
	}
}
