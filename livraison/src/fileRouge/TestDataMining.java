package fileRouge;

import java.util.List;

import java.util.Random;
import java.util.Set;
import bwgenerator.BWGenerator;
import datamining.Apriori;
import datamining.AssociationRule;
import datamining.BooleanDatabase;
import datamining.BruteForceAssociationRuleMiner;
import datamining.Itemset;
import modelling.BooleanVariable;

public class TestDataMining {
	public static void main(String[] args) {
		int numberOfBlocks = 2;
		int numberOfStacks = 1;
		int numberOfInstance = 10000;
		float minFrequency = 2 / 3;
		float confidence = 95 / 100;
		// Variable extraction
		ExtractionVariable variables = new ExtractionVariable(numberOfBlocks, numberOfStacks);
		// DataBase creation with the variables generated above
		BooleanDatabase booleanDataBase = new BooleanDatabase(variables.getVariable());
		for (int i = 0; i < numberOfInstance; i++) {
			// Creation of a state of kind <List<List<Integer>> with bwgenerator where each
			// list represents a stack
			BWGenerator bwGenerator = new BWGenerator(numberOfBlocks,numberOfStacks);
			List<List<Integer>> state = bwGenerator.generate(new Random());
			// Getting the number of blocks and stacks of the generated world
			int blocks = 0;
			for (List<Integer> stack : state) {
				blocks += stack.size();
			}
			// Getting the variables of the new generated state with ExtractionVariable
			Set<BooleanVariable> instance = new ExtractionVariable(blocks, state.size()).getVariable();
			// Adding state variables to database
			booleanDataBase.add(instance);
		}
		// Extracting frequent itemset with apriori algorithm
		Apriori extractApriori = new Apriori(booleanDataBase);
		Set<Itemset> frequentItemsets = extractApriori.extract(minFrequency);
		// Extracting association rules with BruteForceAssociationRuleMiner
		BruteForceAssociationRuleMiner bruteForceAssociationRuleMiner = new BruteForceAssociationRuleMiner(
				booleanDataBase);
		Set<AssociationRule> associationRule = bruteForceAssociationRuleMiner.extract(minFrequency, confidence);
		// Printing results
		System.out.println("Frequent itemsets : ");
		for (Itemset item : frequentItemsets) {
			System.out.println("item : " + item + "\n");
		}
		
		System.out.println("Association rules : " + associationRule);
		for (AssociationRule rule : associationRule) {
			System.out.println(rule + "\n");
		}
		System.out.println("number of frequent items : "+frequentItemsets.size());
		System.out.println("number of rules : "+associationRule.size());
	}

}
