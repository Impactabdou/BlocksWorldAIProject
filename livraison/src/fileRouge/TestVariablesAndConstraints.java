package fileRouge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import modelling.BooleanVariable;
import modelling.Constraint;
import modelling.Variable;

public class TestVariablesAndConstraints {
	public static void main(String[] args) {
		/*
		 * Creating an instance of BlocksWorldConstraint to get the basic constraints
		 * Creating an instance of regularConstraint to get regular constraints Creating
		 * an instance of circularConstraint to get circular constraints
		 */
		int numberOfBlocks = 4;
		int numberOfStacks = 2;
		BlocksWorldConstraint blocksWorldConstraint = new BlocksWorldConstraint(numberOfBlocks, numberOfStacks);

		Set<Constraint> allConstraint = new HashSet<>();

		Set<Constraint> basicConstraint = new HashSet<>(blocksWorldConstraint.getConstraint());
		Set<Constraint> regularConstraint = new HashSet<>(new RegularConstraint().getConstraint(blocksWorldConstraint));
		Set<Constraint> circularConstraint = new HashSet<>(
				new CircularConstraint().getConstraint(blocksWorldConstraint));

		allConstraint.addAll(basicConstraint);
		allConstraint.addAll(regularConstraint);
		allConstraint.addAll(circularConstraint);

		System.out.println(allConstraint);
		// init is a world that respects the basic/regular/circular constraint
		Map<Variable, Object> init = new HashMap<>();
		init.put(new Variable("on0", blocksWorldConstraint.blockDomain(0)), -1);
		init.put(new Variable("on1", blocksWorldConstraint.blockDomain(1)), -2);
		init.put(new Variable("on2", blocksWorldConstraint.blockDomain(2)), 0);
		init.put(new Variable("on3", blocksWorldConstraint.blockDomain(3)), 2);
		init.put(new BooleanVariable("free-1"), false);
		init.put(new BooleanVariable("free-2"), false);
		init.put(new BooleanVariable("fixed0"), true);
		init.put(new BooleanVariable("fixed1"), false);
		init.put(new BooleanVariable("fixed2"), true);
		init.put(new BooleanVariable("fixed3"), true);

		// init doesnt satisfy the regular constrains because the stack -1 doesnt have
		// the same gap
		boolean bool = true;
		for (Constraint constraint : regularConstraint) {
			if (!constraint.isSatisfiedBy(init)) {
				bool = false;
				System.out.println("incorrect regular configuration!!");
			}
		}
		bool = true;
		init.clear();

		init.put(new Variable("on0", blocksWorldConstraint.blockDomain(0)), -1);
		init.put(new Variable("on1", blocksWorldConstraint.blockDomain(1)), -2);
		init.put(new Variable("on2", blocksWorldConstraint.blockDomain(2)), 3);
		init.put(new Variable("on3", blocksWorldConstraint.blockDomain(3)), 2);
		init.put(new BooleanVariable("free-1"), false);
		init.put(new BooleanVariable("free-2"), false);
		init.put(new BooleanVariable("fixed0"), false);
		init.put(new BooleanVariable("fixed1"), false);
		init.put(new BooleanVariable("fixed2"), true);
		init.put(new BooleanVariable("fixed3"), true);

		/*
		 * init doesnt satisfy the circular constrains because on2 = 3 and on3 = 2
		 */
		for (Constraint constraint : circularConstraint) {
			if (!constraint.isSatisfiedBy(init)) {
				bool = false;
				System.out.println("incorrect circular configuration!!");
			}
		}
		if (bool) {
			System.out.println("good world !!");
		}
	}
}
