package fileRouge;

import java.awt.Dimension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import bwmodel.BWState;
import bwmodel.BWStateBuilder;
import bwui.BWComponent;
import bwui.BWIntegerGUI;
import modelling.BooleanVariable;
import modelling.Variable;
import planning.AStarPlanner;
import planning.Action;
import planning.BFSPlanner;
import planning.BasicGoal;
import planning.DFSPlanner;
import planning.DijkstraPlanner;
import planning.Goal;
import planning.Heuristic;

public class TestBlocksWorldActions {
	public static BWState<Integer> makeBWState(int numberOfBlocks, BlocksWorldActions bwa,
			Map<Variable, Object> instanciation) {
		// Building state
		BWStateBuilder<Integer> builder = BWStateBuilder.makeBuilder(numberOfBlocks);
		for (int b = 0; b < numberOfBlocks; b++) {
			Variable onB = new Variable("on" + b, bwa.blockDomain(b)); // get instance of Variable for "on_b"
			int under = (int) instanciation.get(onB);
			if (under >= 0) { // if the value is a block (as opposed to a stack)
				builder.setOn(b, under);
			}
		}
		BWState<Integer> state = builder.getState();
		return state;
	}

	public static void main(String[] args) {
		int numberOfBlocks = 3;
		int numberOfStacks = 3;

		BlocksWorldActions blocksWorldAcctions = new BlocksWorldActions(numberOfBlocks, numberOfStacks);

		/*
		 * initial state with 3 blocks and 3 stacks the method blockDomain is used to
		 * get the domain for the block
		 */
		Map<Variable, Object> init = new HashMap<>();
		init.put(new Variable("on0", blocksWorldAcctions.blockDomain(0)), -2);
		init.put(new Variable("on1", blocksWorldAcctions.blockDomain(1)), 0);
		init.put(new Variable("on2", blocksWorldAcctions.blockDomain(2)), 1);
		init.put(new BooleanVariable("free-2"), false);
		init.put(new BooleanVariable("free-1"), true);
		init.put(new BooleanVariable("free-3"), true);
		init.put(new BooleanVariable("fixed0"), true);
		init.put(new BooleanVariable("fixed1"), true);
		init.put(new BooleanVariable("fixed2"), false);
		/*
		 * final state
		 */
		Map<Variable, Object> finale = new HashMap<>();
		finale.put(new Variable("on0", blocksWorldAcctions.blockDomain(0)), -1);
		finale.put(new Variable("on1", blocksWorldAcctions.blockDomain(1)), -2);
		finale.put(new Variable("on2", blocksWorldAcctions.blockDomain(2)), 1);
		finale.put(new BooleanVariable("free-2"), false);
		finale.put(new BooleanVariable("free-1"), false);
		finale.put(new BooleanVariable("free-3"), true);
		finale.put(new BooleanVariable("fixed0"), false);
		finale.put(new BooleanVariable("fixed1"), true);
		finale.put(new BooleanVariable("fixed2"), false);

		Goal goal = new BasicGoal(finale);

		// DFS
		DFSPlanner dfs = new DFSPlanner(init, blocksWorldAcctions.getActions(), goal);
		dfs.activateNodeCount(true);
		long startTime = System.nanoTime();
		List<Action> dfsPlan = new ArrayList<>();
		dfsPlan = dfs.plan();
		long stopTime = System.nanoTime();
		System.out.println("Time of dfs : " + (stopTime - startTime) + "ns");
		System.out.println("dfs : " + dfs.getCount());
		if (dfsPlan == null) {
			System.out.println("No DFS Plan!!");
		}

		// BFS
		BFSPlanner bfs = new BFSPlanner(init, blocksWorldAcctions.getActions(), goal);
		bfs.activateNodeCount(true);
		long startTime1 = System.nanoTime();
		List<Action> bfsPlan = new ArrayList<>();
		bfsPlan = bfs.plan();
		long stopTime1 = System.nanoTime();
		System.out.println("Time of bfs : " + (stopTime1 - startTime1) + "ns");
		System.out.println("bfs : " + bfs.getCount());
		if (bfsPlan == null) {
			System.out.println("No BFS plan!!");
		}

		// Dijkstra
		DijkstraPlanner dij = new DijkstraPlanner(init, blocksWorldAcctions.getActions(), goal);
		dij.activateNodeCount(true);
		long startTime2 = System.nanoTime();
		List<Action> dijPlan = new ArrayList<>();
		dijPlan = dij.plan();
		long stopTime2 = System.nanoTime();
		System.out.println("Time of dij : " + (stopTime2 - startTime2) + "ns");
		System.out.println("dij : " + dij.getCount());
		if (dijPlan == null) {
			System.out.println("No BFS plan!!");
		}

		// AStar
		// First heuristic (number of miss placed blocks)
		// Second heuristic (
		Heuristic h = new MisPlacedBlocksHeuristic((BasicGoal) goal);
		Heuristic h2 = new MisPlacedBlockStackHeuristic((BasicGoal) goal);
		AStarPlanner aStarHeuristic1 = new AStarPlanner(init, blocksWorldAcctions.getActions(), goal, h);
		AStarPlanner aStarHeuristic2 = new AStarPlanner(init, blocksWorldAcctions.getActions(), goal, h2);
		aStarHeuristic1.activateNodeCount(true);
		long startTime3 = System.nanoTime();
		List<Action> aStarPlan = new ArrayList<>();
		aStarPlan = aStarHeuristic1.plan();
		long stopTime3 = System.nanoTime();
		System.out.println("Time of a* : " + (stopTime3 - startTime3) + "ns");
		System.out.println("aStar : " + aStarHeuristic1.getCount());
		if (aStarPlan == null) {
			System.out.println("No aStar plan!!");
		}
		aStarHeuristic2.activateNodeCount(true);
		long startTime4 = System.nanoTime();
		aStarPlan = aStarHeuristic2.plan();
		long stopTime4 = System.nanoTime();
		System.out.println("Time of a* : " + (stopTime4 - startTime4) + "ns");
		System.out.println("aStar : " + aStarHeuristic2.getCount());
		if (aStarPlan == null) {
			System.out.println("No aStar plan!!");
		}

		BWIntegerGUI gui = new BWIntegerGUI(numberOfBlocks);
		JFrame frame = new JFrame("Blocks World");
		BWState<Integer> bwState = TestBlocksWorldActions.makeBWState(numberOfBlocks, blocksWorldAcctions, init);
		BWComponent<Integer> component = gui.getComponent(bwState);
		frame.add(component);
		frame.setSize(new Dimension(500, 500));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		/*
		 * To chose the plan manually just chose one of the four plan list below or
		 * change it directly from the console : dfsPlan bfsPlan dijPlan aStarPlan
		 */
		Scanner sc = new Scanner(System.in);
		System.out.println("chose a plan : \n1- DFS\n2- BFS\n3-Dijkstra\n4-Astar");
		int plan = sc.nextInt();
		List<Action> chosenPlan = null;
		String message = "Plan selected!";
		if (plan == 1) {
			chosenPlan = dfsPlan;
			System.out.println(message);
		} else if (plan == 2) {
			chosenPlan = bfsPlan;
			System.out.println(message);
		} else if (plan == 3) {
			chosenPlan = dijPlan;
			System.out.println(message);
		} else if (plan == 4) {
			chosenPlan = aStarPlan;
			System.out.println(message);
		}

		if (chosenPlan != null) {
			// Playing plan
			for (Action a : chosenPlan) {
				try {
					Thread.sleep(1_000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// updating the init with it's successor
				init = a.successor(init);
				// generating the new BWState with makeBWState()
				component.setState(TestBlocksWorldActions.makeBWState(numberOfBlocks, blocksWorldAcctions, init));
			}
			System.out.println("Simulation of plan: done.");
		} else {
			System.out.println("No plan to Play!");
		}
	}
}
