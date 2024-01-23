package fileRouge;

import java.util.HashSet;
import java.util.Set;
import modelling.BooleanVariable;
import modelling.Variable;

public class BlocksWorldVariables {
	private final int numberOfBlocks;
	private final int numberOfStacks;
	private Set<BooleanVariable> freeP;
	private Set<BooleanVariable> fixedB;
	private Set<Variable> on;
	private Set<Variable> variables;

	public BlocksWorldVariables(int numberOfBlocks, int numberOfStacks) {
		// Initiation
		this.numberOfBlocks = numberOfBlocks;
		this.numberOfStacks = numberOfStacks;
		this.fixedB = new HashSet<>();
		this.freeP = new HashSet<>();
		this.on = new HashSet<>();
		this.variables = new HashSet<>();
		/*
		 * Creating the on/fixed/free Blocks are represented with positive integers each
		 * Block takes a domain from {-p,...,0,...,n}/{b} the block domain is generated
		 * with blockDomain(int out) each block has a fixed that tells if it's fixed or
		 * not each stack has a free that tells if it's free or not fixed and free are
		 * of type BooleanVariable
		 */
		for (int i = 0; i < numberOfBlocks; i++) {
			Variable block = new Variable("on" + i, this.blockDomain(i));
			BooleanVariable fixed = new BooleanVariable("fixed" + i);
			this.variables.add(block);
			this.variables.add(fixed);
			this.fixedB.add(fixed);
			this.on.add(block);
		}
		// Stacks are represented with negative integers
		for (int i = -numberOfStacks; i < 0; i++) {
			BooleanVariable free = new BooleanVariable("free" + i);
			this.variables.add(free);
			this.freeP.add(free);
		}
	}

	// Method to get a blockDomain
	public Set<Object> blockDomain(int out) {
		Set<Object> domain = new HashSet<>();
		for (int i = -this.numberOfStacks; i < this.numberOfBlocks; i++) {
			if (out != i) {
				domain.add(i);
			}
		}
		return domain;
	}

	// Methods to get a spacific on/fixed/free number
	public int getVariableNumber(Variable block) {
		return Integer.parseInt(block.getName().substring(2));
	}

	public int getBooleanVariableNumberFixed(BooleanVariable var) {
		return Integer.parseInt(var.getName().substring(5));
	}

	public int getBooleanVariableNumberFree(BooleanVariable var) {
		return Integer.parseInt(var.getName().substring(4));
	}

	// Getters/Setters
	public Set<BooleanVariable> getFreeP() {
		return freeP;
	}

	public void setFreeP(Set<BooleanVariable> freeP) {
		this.freeP = freeP;
	}

	public Set<BooleanVariable> getFixedB() {
		return fixedB;
	}

	public void setFixedB(Set<BooleanVariable> fixedB) {
		this.fixedB = fixedB;
	}

	public Set<Variable> getOn() {
		return on;
	}

	public void setOn(Set<Variable> on) {
		this.on = on;
	}

	public Set<Variable> getVariables() {
		return variables;
	}

	public void setVariables(Set<Variable> variables) {
		this.variables = variables;
	}

	public int getNumberOfBlocks() {
		return numberOfBlocks;
	}

	public int getNumberOfStacks() {
		return numberOfStacks;
	}

	@Override
	public String toString() {
		String str = "";
		for (Variable variable : this.variables) {
			str += variable.toString() + "\n";
		}
		return str;
	}

}
