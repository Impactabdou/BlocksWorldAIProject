package fileRouge;

import java.util.HashMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import modelling.BooleanVariable;
import modelling.Variable;
import planning.Action;
import planning.BasicAction;

public class BlocksWorldActions extends BlocksWorldVariables {
	private Set<Action> actions;

	public BlocksWorldActions(int numberOfBlocks, int numberOfStacks) {
		super(numberOfBlocks, numberOfStacks);
		this.actions = new HashSet<>();
		/*
		 * Type 1 of actions : move b from b' to b'' precondition : onb = b' , fixedb =
		 * false , fixedb'' = false effect : onb = b'' , fixedb' = false , fixedb'' =
		 * true
		 */
		for (Variable block : super.getOn()) {
			for (Variable block1 : super.getOn()) {
				if (!block.equals(block1)) {
					Map<Variable, Object> precondition = new HashMap<>();
					precondition.put(block, super.getVariableNumber(block1));// onb = b'
					for (BooleanVariable fixed : super.getFixedB()) {
						if (super.getBooleanVariableNumberFixed(fixed) == super.getVariableNumber(block)) {
							precondition.put(fixed, false);// fixedb : false
						}
					}
					for (Variable block2 : super.getOn()) {
						if (!block2.equals(block) && !block2.equals(block1)) {
							Map<Variable, Object> effect = new HashMap<>();
							effect.put(block, super.getVariableNumber(block2));// onb = b''----1
							for (BooleanVariable fixed : super.getFixedB()) {
								if (super.getBooleanVariableNumberFixed(fixed) == super.getVariableNumber(block2)) {
									precondition.put(fixed, false);// fixedb'' : false
									effect.put(fixed, true);// fixedb'':true
								}
								if (super.getBooleanVariableNumberFixed(fixed) == super.getVariableNumber(block1)) {
									effect.put(fixed, false);// fixedb':false
								}
							}
							// Creation of the action
							Action action = new BasicAction(precondition, effect, 1);
							this.actions.add(action);
						}
					}
				}
			}
		}
		/*
		 * Type 2 of actions : moves b from b' to p precondition : onb = b' , fixedb =
		 * false , freep = true 
		 * effect : onb = p , freep = false , fixedb' = false
		 */
		for (Variable block : super.getOn()) {
			for (Variable block1 : super.getOn()) {
				if (!block.equals(block1)) {
					Map<Variable, Object> precondition = new HashMap<>();
					precondition.put(block, super.getVariableNumber(block1));// onb = b'
					for (BooleanVariable fixed : super.getFixedB()) {
						if (super.getBooleanVariableNumberFixed(fixed) == super.getVariableNumber(block)) {
							precondition.put(fixed, false);// fixedb : false
						}
					}
					for (BooleanVariable free : super.getFreeP()) {
						precondition.put(free, true);// freep : true
						Map<Variable, Object> effect = new HashMap<>();
						effect.put(block, super.getBooleanVariableNumberFree(free));// onb = p
						effect.put(free, false);// freep = false
						for (BooleanVariable fixed : super.getFixedB()) {
							if (super.getBooleanVariableNumberFixed(fixed) == super.getVariableNumber(block1)) {
								effect.put(fixed, false);// fixedb'=false
							}
						}
						// Creation de l'action
						Action action = new BasicAction(precondition, effect, 1);
						this.actions.add(action);
						precondition.remove(free);// removing the last visited free
					}
				}
			}
		}

		/*
		 * Type 3 of actions : move b from p to b' precondition : onb = p , fixedb =
		 * false , fixedb' = fasle 
		 * effect : onb = b' , fixedb' = true , freep = true
		 */
		for (Variable block : super.getOn()) {
			for (BooleanVariable free : super.getFreeP()) {
				Map<Variable, Object> precondition = new HashMap<>();
				precondition.put(block, super.getBooleanVariableNumberFree(free));// onb = p
				for (BooleanVariable fixed : super.getFixedB()) {
					if (super.getBooleanVariableNumberFixed(fixed) == super.getVariableNumber(block)) {
						precondition.put(fixed, false); // fixedb = false
					}
				}
				BooleanVariable toRemove = null;
				for (Variable block1 : super.getOn()) {
					if (!block1.equals(block)) {
						Map<Variable, Object> effect = new HashMap<>();
						effect.put(free, true);// freep : true
						effect.put(block, super.getVariableNumber(block1));// onb = b'
						for (BooleanVariable fixed : super.getFixedB()) {
							if (super.getBooleanVariableNumberFixed(fixed) == super.getVariableNumber(block1)) {
								effect.put(fixed, true);// fixedb' = true
								precondition.put(fixed, false);// fixedb' = false
								toRemove = fixed;
							}
						}
						// Creating action
						Action action3 = new BasicAction(precondition, effect, 1);
						this.actions.add(action3);
						if (toRemove != null) {
							precondition.remove(toRemove);
						}
					}
				}
			}
		}

		/*
		 * Type 4 of actions : move b from p to p' precondition : onb = p , freep' =
		 * true , fixedb = false effect : onb = p' , freep = true , freep' = false
		 */
		for (Variable block : super.getOn()) {
			for (BooleanVariable free : super.getFreeP()) {
				Map<Variable, Object> precondition = new HashMap<>();
				precondition.put(block, super.getBooleanVariableNumberFree(free));// onb = p
				for (BooleanVariable fixed : super.getFixedB()) {
					if (super.getBooleanVariableNumberFixed(fixed) == super.getVariableNumber(block)) {
						precondition.put(fixed, false);// fixedb:false
					}
				}
				for (BooleanVariable free1 : super.getFreeP()) {
					if (!free1.equals(free)) {
						Map<Variable, Object> effect = new HashMap<>();
						effect.put(block, super.getBooleanVariableNumberFree(free1));// onb = free'
						effect.put(free, true);// freep = true
						effect.put(free1, false);// freep' = false
						precondition.put(free1, true);// freep' : true (precondition)
						// creating action
						Action action4 = new BasicAction(precondition, effect, 1);
						this.actions.add(action4);
						precondition.remove(free1);
					}
				}
			}
		}
	}

	// Get Actions
	public Set<Action> getActions() {
		return this.actions;
	}
}
