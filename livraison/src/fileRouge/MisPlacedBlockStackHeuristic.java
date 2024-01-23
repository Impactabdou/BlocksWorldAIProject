package fileRouge;

import java.util.Map;

import modelling.Variable;
import planning.BasicGoal;
import planning.Heuristic;

public class MisPlacedBlockStackHeuristic implements Heuristic {
	private BasicGoal goal;

	public MisPlacedBlockStackHeuristic(BasicGoal goal) {
		this.goal = goal;
	}

	// misplaced blocks compared to stacks
	@Override
	public float estimate(Map<Variable, Object> state) {
		int numberOfMissPlacedBlocks = 0;
		for (Variable block : state.keySet()) {
			for (Variable block1 : this.goal.getGoal().keySet()) {
				if (block.equals(block1)) {
					// Checking if the block is well placed compared to the goal state
					if ((state.get(block) != goal.getGoal().get(block1)) && (state.get(block).hashCode()) < 0
							&& (goal.getGoal().get(block1).hashCode() < 0)) {
						numberOfMissPlacedBlocks++;
					}
				}
			}
		}
		return numberOfMissPlacedBlocks;
	}

}
