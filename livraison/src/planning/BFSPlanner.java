
package planning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import modelling.Variable;

public class BFSPlanner implements Planner {
	private Map<Variable, Object> initState;
	private Set<Action> action;
	private Goal goal;
	private int count;
	private boolean activate;

	public BFSPlanner(Map<Variable, Object> initState, Set<Action> action, Goal goal) {
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

	@Override
	public List<Action> plan() {

		Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>();
		Map<Map<Variable, Object>, Action> plan = new HashMap<>();
		Set<Map<Variable, Object>> closed = new HashSet<>();
		Queue<Map<Variable, Object>> open = new LinkedList<>();
		closed.add(getInitialState());
		open.add(getInitialState());
		father.put(getInitialState(), null);
		if (getGoal().isSatisfiedBy(getInitialState())) {
			return new ArrayList<Action>();
		}
		while (!(open.isEmpty())) {
			Map<Variable, Object> instantiation = open.remove();
			closed.add(instantiation);
			for (Action action : action) {
				if (action.isApplicable(instantiation)) {
					if (this.activate) {
						this.count++;
					}
					Map<Variable, Object> next = action.successor(instantiation);
					if (!(closed.contains(next)) && !(open.contains(next))) {
						father.put(next, instantiation);
						plan.put(next, action);
						if (getGoal().isSatisfiedBy(next)) {
							return getBfsPlan(father, plan, next);
						} else {
							open.add(next);
						}
					}
				}
			}
		}
		return null;
	}

	public List<Action> getBfsPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father,
			Map<Map<Variable, Object>, Action> plan, Map<Variable, Object> goal) {
		LinkedList<Action> res = new LinkedList<Action>();
		while (father.get(goal) != null) {
			res.add(plan.get(goal));
			goal = father.get(goal);
		}
		Collections.reverse(res);
		return res;
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
