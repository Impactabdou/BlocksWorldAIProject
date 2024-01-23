package planning;

import java.util.HashMap;
import java.util.Map;

import modelling.Variable;

public class BasicAction implements Action {
	private Map<Variable, Object> precondition;
	private Map<Variable, Object> effect;
	private int cost;

	public BasicAction(Map<Variable, Object> precondition, Map<Variable, Object> effet, int cost) {
		this.precondition = new HashMap<>(precondition);
		this.effect = new HashMap<>(effet);
		this.cost = cost;
	}

	@Override
	public boolean isApplicable(Map<Variable, Object> state) {
		for (Variable s : precondition.keySet()) {
			if (!precondition.get(s).equals(state.get(s))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Map<Variable, Object> successor(Map<Variable, Object> state) {
		if (state.isEmpty()) {
			return effect;
		}
		if (effect.isEmpty()) {
			return state;
		}
		Map<Variable, Object> res = new HashMap<Variable, Object>();

		for (Map.Entry<Variable, Object> ef : state.entrySet()) {
			if (effect.containsKey(ef.getKey())) {
				res.put(ef.getKey(), effect.get(ef.getKey()));
			} else {
				res.put(ef.getKey(), state.get(ef.getKey()));
			}
		}
		for (Map.Entry<Variable, Object> ef : effect.entrySet()) {
			if (!res.containsKey(ef.getKey())) {
				res.put(ef.getKey(), effect.get(ef.getKey()));
			}
		}
		return res;
	}

	public Map<Variable, Object> getPrecondition() {
		return precondition;
	}

	public void setPrecondition(Map<Variable, Object> precondition) {
		this.precondition = precondition;
	}

	public Map<Variable, Object> getEffect() {
		return effect;
	}

	public void setEffect(Map<Variable, Object> effect) {
		this.effect = effect;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public String toString() {
		return "precondition : " + precondition.toString() + " effect : " + effect.toString();
	}

}
