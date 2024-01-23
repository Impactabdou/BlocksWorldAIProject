package planning;

import java.util.HashMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import modelling.Variable;

import planningtests.AStarPlannerTests;
import planningtests.BFSPlannerTests;
import planningtests.BasicActionTests;
import planningtests.BasicGoalTests;
import planningtests.DFSPlannerTests;
import planningtests.DijkstraPlannerTests;

public class TestPlanning {

	public static void main(String[] args) {

		// variables
		Set<Object> domainV1 = new HashSet<>();
		domainV1.add(0);
		domainV1.add(1);
		Variable v1 = new Variable("x0", domainV1);
		Variable v2 = new Variable("x1", domainV1);
		Variable v3 = new Variable("x2", domainV1);
		Variable v4 = new Variable("x3", domainV1);
		Variable v5 = new Variable("x4", domainV1);
		Variable v6 = new Variable("x5", domainV1);
		// etats
		Map<Variable, Object> state = new HashMap<>();
		Map<Variable, Object> state2 = new HashMap<>();
		Map<Variable, Object> state3 = new HashMap<>();
		Map<Variable, Object> state4 = new HashMap<>();
		Map<Variable, Object> state5 = new HashMap<>();
		Map<Variable, Object> state6 = new HashMap<>();
		state.put(v1, 1);
		state2.put(v2, 1);
		state3.put(v3, 1);
		state4.put(v4, 1);
		state5.put(v5, 1);
		state6.put(v6, 1);
		// goal
		Map<Variable, Object> finalState = new HashMap<>();
		finalState.put(v6, 1);
		BasicGoal goal = new BasicGoal(finalState);
		// action
		Action action1 = new BasicAction(state, state2, 1);
		Action action2 = new BasicAction(state, state4, 1);
		Action action3 = new BasicAction(state, state5, 1);
		Action action4 = new BasicAction(state2, state3, 1);
		Action action5 = new BasicAction(state2, state6, 1);
		Action action6 = new BasicAction(state3, state6, 1);
		Action action7 = new BasicAction(state3, state5, 1);
		Action action8 = new BasicAction(state4, state5, 1);
		Action action9 = new BasicAction(state4, state2, 1);
		Action action10 = new BasicAction(state5, state2, 1);
		Action action11 = new BasicAction(state5, state6, 1);

		Set<Action> action = new HashSet<>();
		action.add(action1);
		action.add(action2);
		action.add(action3);
		action.add(action4);
		action.add(action5);
		action.add(action6);
		action.add(action7);
		action.add(action8);
		action.add(action9);
		action.add(action10);
		action.add(action11);

		// planner
		// DFS
		DFSPlanner dfs = new DFSPlanner(state, action, goal);
		dfs.activateNodeCount(true);
		long startTime = System.nanoTime();
		dfs.plan();
		long stopTime = System.nanoTime();
		System.out.println("Time of dfs : " + (stopTime - startTime) + "ns");
		System.out.println("dfs : " + dfs.getCount());
		// BFS
		BFSPlanner bfs = new BFSPlanner(state, action, goal);
		bfs.activateNodeCount(true);
		long startTime1 = System.nanoTime();
		bfs.plan();
		long stopTime1 = System.nanoTime();
		System.out.println("Time of bfs : " + (stopTime1 - startTime1) + "ns");
		System.out.println("bfs  : " + bfs.getCount());
		// DIJ
		DijkstraPlanner dij = new DijkstraPlanner(state, action, goal);
		dij.activateNodeCount(true);
		long startTime2 = System.nanoTime();
		dij.plan();
		long stopTime2 = System.nanoTime();
		System.out.println("Time of dij : " + (stopTime2 - startTime2) + "ns");
		System.out.println("dij : " + dij.getCount());
		// A*
		Heuristic h = new BasicHeuristic(goal);
		AStarPlanner aStar = new AStarPlanner(state, action, goal, h);
		aStar.activateNodeCount(true);
		long startTime3 = System.nanoTime();
		aStar.plan();
		long stopTime3 = System.nanoTime();
		System.out.println("Time of a* : " + (stopTime3 - startTime3) + "ns");
		System.out.println("aStar : " + aStar.getCount());

		boolean ok = true;
		ok = ok && BasicActionTests.testIsApplicable();
		ok = ok && BasicActionTests.testSuccessor();
		ok = ok && BasicActionTests.testGetCost();
		ok = ok && BasicGoalTests.testIsSatisfiedBy();
		ok = ok && DFSPlannerTests.testPlan();
		ok = ok && BFSPlannerTests.testPlan();
		ok = ok && AStarPlannerTests.testPlan();
		ok = ok && DijkstraPlannerTests.testPlan();
		System.out.println(ok ? "All tests OK" : "At least one test KO");

	}

}
