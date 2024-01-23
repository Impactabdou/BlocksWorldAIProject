package fileRouge;

import java.util.HashSet;
import java.util.Set;

import modelling.BooleanVariable;
import modelling.Constraint;
import modelling.Implication;
import modelling.Variable;

public class RegularConstraint {

	public Set<Constraint> getConstraint(BlocksWorldConstraint blocksWorldConstraint) {
		Set<Variable> on = blocksWorldConstraint.getOn();
		Set<BooleanVariable> free = blocksWorldConstraint.getFreeP();
		Set<Constraint> constraint = new HashSet<>();
		for (Variable block : on) {
			for (Variable block2 : on) {
				if (!block.equals(block2)) {
					int blockValue = blocksWorldConstraint.getVariableNumber(block);
					int block2Value = blocksWorldConstraint.getVariableNumber(block2);
					int ecart = block2Value - blockValue; // getting the first gap between b and b'
					Set<Object> domain1 = new HashSet<>();
					domain1.add(block2Value);// adding b' to domaine1
					for (Variable block3 : on) {
						if (!block3.equals(block) && !block3.equals(block2)) {
							int blockValue3 = blocksWorldConstraint.getVariableNumber(block3);
							// getting the second gap b' b''
							if (ecart == blockValue3 - block2Value) {
								Set<Object> domain2 = new HashSet<>();
								domain2.add(blockValue3);
								for (BooleanVariable stack : free) {
									// adding all stacks
									domain2.add(blocksWorldConstraint.getBooleanVariableNumberFree(stack));
								}
								// creating the constraint
								Implication regularConstraint = new Implication(block2, domain2, block, domain1);
								constraint.add(regularConstraint);
							}
						}
					}
				}
			}

		}
		return constraint;
	}
}
