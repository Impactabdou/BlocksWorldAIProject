package fileRouge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;

import bwmodel.BWState;
import bwmodel.BWStateBuilder;
import bwui.BWIntegerGUI;
import cp.BacktrackSolver;
import cp.DomainSizeVariableHeuristic;
import cp.HeuristicMACSolver;
import cp.MACSolver;
import cp.NbConstraintsVariableHeuristic;
import cp.RandomValueHeuristic;
import cp.ValueHeuristic;
import cp.VariableHeuristic;
import modelling.Constraint;
import modelling.Variable;

public class BlocksWorldCircularConstraintSolvers {
	public static void main(String[] args) {
		int numberOfBlocks = 6;
		int numberOfStacks = 2;
		BlocksWorldConstraint blocksWorldConstraint = new BlocksWorldConstraint(numberOfBlocks, numberOfStacks);
		// generating the circular constraint
		Set<Constraint> constraint = new HashSet<>(blocksWorldConstraint.getConstraint());
		CircularConstraint circular = new CircularConstraint();
		constraint.addAll(circular.getConstraint(blocksWorldConstraint));

		// backtracking
		BacktrackSolver backtrack = new BacktrackSolver(blocksWorldConstraint.getVariables(), constraint);

		// macsolver
		MACSolver macSolver = new MACSolver(blocksWorldConstraint.getVariables(), constraint);

		// macsolverHeuristic
		VariableHeuristic variableHeuristicNb = new NbConstraintsVariableHeuristic(constraint, true);
		VariableHeuristic variableHeuristicDomain = new DomainSizeVariableHeuristic(true);
		ValueHeuristic valueHeuristic = new RandomValueHeuristic(new Random());
		HeuristicMACSolver macSolverHeuristicNb = new HeuristicMACSolver(blocksWorldConstraint.getVariables(),
				constraint, variableHeuristicNb, valueHeuristic);
		HeuristicMACSolver macSolverHeuristicDomain = new HeuristicMACSolver(blocksWorldConstraint.getVariables(),
				constraint, variableHeuristicDomain, valueHeuristic);

		// generating the world with backtracking
		long startTime = System.nanoTime();
		Map<Variable, Object> backtrackWorld = new HashMap<>(backtrack.solve());
		long endTime = System.nanoTime();
		System.out.println("Time of backtracking : " + (endTime - startTime) + "ns");
		System.out.println(backtrackWorld);

		// generating the world with macsolver
		startTime = System.nanoTime();
		Map<Variable, Object> macSolverWorld = new HashMap<>(macSolver.solve());
		endTime = System.nanoTime();
		System.out.println("Time of macSolver : " + (endTime - startTime) + "ns");
		System.out.println(macSolverWorld);

		// generating the world with macsolver heuristic with vairaible number
		startTime = System.nanoTime();
		Map<Variable, Object> macSolverHeuristicNbWorld = new HashMap<>(macSolverHeuristicNb.solve());
		endTime = System.nanoTime();
		System.out.println("Time of macSolver with variables nb heuristic: " + (endTime - startTime) + "ns");
		System.out.println(macSolverHeuristicNbWorld);

		// generating the world with macsolver heuristic with variable domain
		startTime = System.nanoTime();
		Map<Variable, Object> macSolverHeuristicDomainWorld = new HashMap<>(macSolverHeuristicDomain.solve());
		endTime = System.nanoTime();
		System.out.println("Time of macSolver with variables domain heuristic: " + (endTime - startTime) + "ns");
		System.out.println(macSolverHeuristicDomainWorld);

		// chose which world to display
		Map<Variable, Object> worldToDisplay = new HashMap<>(backtrackWorld);

		// Building state
		BWStateBuilder<Integer> builder = BWStateBuilder.makeBuilder(numberOfBlocks);
		for (int b = 0; b < numberOfBlocks; b++) {
			Variable onB = new Variable("on" + b, blocksWorldConstraint.blockDomain(b)); // get instance of Variable for
			// choosing the world to display // "on_b"
			int under = (int) worldToDisplay.get(onB);
			if (under >= 0) { // if the value is a block (as opposed to a stack)
				builder.setOn(b, under);
			}
		}
		BWState<Integer> state = builder.getState();
		// Displaying
		BWIntegerGUI gui = new BWIntegerGUI(numberOfBlocks);
		JFrame frame = new JFrame("Blocks world representation");
		frame.add(gui.getComponent(state));
		frame.pack();
		frame.setVisible(true);
	}

}
