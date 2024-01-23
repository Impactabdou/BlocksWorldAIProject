package fileRouge;

import java.util.HashSet;

import java.util.Set;
import modelling.BooleanVariable;
import modelling.Variable;

public class ExtractionVariable {
	private final BlocksWorldVariables blocksWorldVariable;

	public ExtractionVariable(int numberOfBlocks, int numberOfStacks) {
		this.blocksWorldVariable = new BlocksWorldVariables(numberOfBlocks, numberOfStacks);
	}

	public Set<BooleanVariable> getVariable() {
		Set<BooleanVariable> booleanVariable = new HashSet<BooleanVariable>();
		Set<Variable> on = this.blocksWorldVariable.getOn();
		Set<BooleanVariable> free = this.blocksWorldVariable.getFreeP();
		Set<BooleanVariable> fixed = this.blocksWorldVariable.getFixedB();
		for (Variable block : on) {
			for (Variable block1 : on) {
				if (!block.equals(block1)) {

					BooleanVariable onb = new BooleanVariable("on" + this.blocksWorldVariable.getVariableNumber(block)
							+ ":" + this.blocksWorldVariable.getVariableNumber(block1));
					booleanVariable.add(onb);// Adding onb,b'
				}
			}
			for (BooleanVariable freep : free) {
				BooleanVariable onTable = new BooleanVariable(
						"on-table" + this.blocksWorldVariable.getVariableNumber(block) + ":"
								+ this.blocksWorldVariable.getBooleanVariableNumberFree(freep));
				booleanVariable.add(onTable);// Adding freeb,p
			}
		}
		for (BooleanVariable fixedb : fixed) {
			booleanVariable.add(fixedb);// Adding fixed
		}
		for (BooleanVariable freep : free) {
			booleanVariable.add(freep);// Adding freep
		}

		return booleanVariable;
	}

}
