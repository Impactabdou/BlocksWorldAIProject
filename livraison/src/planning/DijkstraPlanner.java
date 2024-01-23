package planning;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modelling.Variable;

public class DijkstraPlanner implements Planner {
	private Map<Variable, Object> initState;
	private Set<Action> action;
	private Goal goal;
	private int count;
	private boolean activate;

	public DijkstraPlanner(Map<Variable, Object> initState, Set<Action> action, Goal goal) {
		this.initState = initState;
		this.action = action;
		this.goal = goal;
		this.activate = false;
		this.count = 0;
	}

	public Map<Variable, Object> argMin(Map<Map<Variable, Object>, Float> distance, List<Map<Variable, Object>> open) {
		Map<Variable, Object> minNode = new HashMap<>();
		float min = (float) (1.0 / 0.0);
		for (Map<Variable, Object> d : distance.keySet()) {
			for (Map<Variable, Object> e : open) {
				if (e.equals(d)) {
					if (min > distance.get(e)) {
						min = distance.get(e);
						minNode = e;
					}
				}
			}
		}
		return minNode;
	}

	@Override
	public void activateNodeCount(boolean activate) {
		this.activate = activate;
	}

	@Override
	public List<Action> plan() {
		Map<Map<Variable, Object>, Action> plan = new HashMap<>();
		Map<Map<Variable, Object>, Float> distance = new HashMap<>();
		Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>();
		List<Map<Variable, Object>> open = new LinkedList<>();
		List<Map<Variable, Object>> goals = new LinkedList<>();

		father.put(getInitialState(), null);
		distance.put(getInitialState(), (float) 0);
		open.add(getInitialState());

		while (!(open.isEmpty())) {
			Map<Variable, Object> instantiation = argMin(distance, open);
			open.remove(instantiation);
			if (getGoal().isSatisfiedBy(instantiation)) {
				goals.add(instantiation);
			}
			for (Action action : action) {
				if (action.isApplicable(instantiation)) {
					Map<Variable, Object> next = action.successor(instantiation);
					if (!(distance.containsKey(next))) {
						if (this.activate) {
							this.count++;
						}
						distance.put(next, (float) (1.0 / 0.0));
					}
					if (distance.get(next) > distance.get(instantiation) + action.getCost()) {
						if (this.activate) {
							this.count++;
						}
						distance.put(next, distance.get(instantiation) + action.getCost());
						father.put(next, instantiation);
						plan.put(next, action);
						open.add(next);
					}
				}
			}
		}
		if (goals.isEmpty()) {
			return null;
		} else {
			return djiPlan(father, plan, goals, distance);
		}
	}

	public List<Action> djiPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father,
			Map<Map<Variable, Object>, Action> plan, List<Map<Variable, Object>> goals,
			Map<Map<Variable, Object>, Float> distance) {
		LinkedList<Action> res = new LinkedList<Action>();
		Map<Variable, Object> objective = argMin(distance, goals);
		while (father.get(objective) != null) {
			res.addFirst(plan.get(objective));
			objective = father.get(objective);
		}
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
