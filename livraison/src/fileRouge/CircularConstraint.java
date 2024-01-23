package fileRouge;

import java.util.HashSet;
import java.util.Set;

import modelling.Constraint;
import modelling.UnaryConstraint;
import modelling.Variable;

public class CircularConstraint {

	/*
	 * representing the constraint with a UnaryConstraint A block can take only
	 * values which are smaller than it's number here we use the domain
	 * (-p,....,0,....n) of a chosen block to chose which value it can take
	 */

	public Set<Constraint> getConstraint(BlocksWorldConstraint blocksWorldConstraint) {
		Set<Constraint> circularConstraint = new HashSet<>();
		for (Variable block : blocksWorldConstraint.getOn()) {
			int blockValue = blocksWorldConstraint.getVariableNumber(block);
			// creating the domain that will have all the legal values
			Set<Object> domain = new HashSet<>();
			for (Variable block1 : blocksWorldConstraint.getOn()) {
				if (!block.equals(block1)) {
					int block1Value = blocksWorldConstraint.getVariableNumber(block1);
					if (block1Value < blockValue) {
						// Adding legal values
						domain.add(block1Value);
					}
				}
			}
			for (Object stack : block.getDomain()) {
				if ((Integer) stack < 0) {
					// adding all stacks
					domain.add(stack);
				}
			}
			// Creating the constraint
			UnaryConstraint constraint = new UnaryConstraint(block, domain);
			circularConstraint.add(constraint);
		}
		return circularConstraint;
	}
}
