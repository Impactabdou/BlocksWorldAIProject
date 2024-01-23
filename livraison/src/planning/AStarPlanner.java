package planning;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modelling.Variable;

public class AStarPlanner implements Planner {
	private Map<Variable, Object> initState;
	private Set<Action> action;
	private Goal goal;
	private Heuristic heuristic;
	private int count;
	private boolean activate;

	public AStarPlanner(Map<Variable, Object> initState, Set<Action> action, Goal goal, Heuristic heuristic) {
		this.initState = initState;
		this.action = action;
		this.goal = goal;
		this.heuristic = heuristic;
		this.activate = false;
		this.count = 0;
	}

	@Override
	public void activateNodeCount(boolean activate) {
		this.activate = activate;
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
	public List<Action> plan() {
		Map<Map<Variable, Object>, Action> plan = new HashMap<>();
		Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>();
		Map<Map<Variable, Object>, Float> distance = new HashMap<>();
		Map<Map<Variable, Object>, Float> value = new HashMap<>();
		List<Map<Variable, Object>> open = new LinkedList<>();
		open.add(getInitialState());
		father.put(getInitialState(), null);
		distance.put(getInitialState(), (float) 0.0);
		value.put(getInitialState(), heuristic.estimate(getInitialState()));
		Map<Variable, Object> instantiation = new HashMap<>();
		while (!(open.isEmpty())) {
			instantiation = argMin(distance, open);
			if (goal.isSatisfiedBy(instantiation)) {
				return getAstarPlan(father, plan, instantiation);
			} else {
				open.remove(instantiation);
				for (Action a : action) {
					if (a.isApplicable(instantiation)) {
						Map<Variable, Object> next = a.successor(instantiation);
						if (!(distance.containsKey(next))) {
							distance.put(next, (float) (1.0 / 0.0));
						}
						if (distance.get(next) > distance.get(instantiation) + a.getCost()) {
							if (this.activate) {
								this.count++;
							}
							distance.put(next, distance.get(instantiation) + a.getCost());
							value.put(next, distance.get(next) + heuristic.estimate(next));
							father.put(next, instantiation);
							plan.put(next, a);
							open.add(next);
						}
					}
				}
			}
		}
		return null;
	}

	public List<Action> getAstarPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father,
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
