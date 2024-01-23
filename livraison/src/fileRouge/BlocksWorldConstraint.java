package fileRouge ; 
import java.util.HashSet;
import java.util.Set;
import modelling.BooleanVariable;
import modelling.Constraint;
import modelling.DifferenceConstraint;
import modelling.Implication;
import modelling.Variable;

public class BlocksWorldConstraint extends BlocksWorldVariables {
	private Set<Constraint> constraint ; 
	
	public BlocksWorldConstraint(int numberOfblocks, int numberOfStacks) {
		// Initiation of the variables
		super(numberOfblocks,numberOfStacks);
		this.constraint = new HashSet<>();
		Set<Variable> visitedBlocks = new HashSet<>();
		/*
		 * VisitedBlocks is a hashSet that keeps track of the visited blocks 
		 * It's useful in the first generation of constraints
		 * onb != onb' and onb' == onb are equivalent so we keep only onb != onb'
		 */
		for(Variable block : super.getOn()){
				visitedBlocks.add(block);
				/* 
				 * Generation of onb != onb'
				 * We use DifferenceConstraint to represent this constraint
				 */
				for(Variable block1 : super.getOn()){
					if(!block1.equals(block) && !visitedBlocks.contains(block1)){
						DifferenceConstraint diffence = new DifferenceConstraint(block, block1);//onb != onb'
						if(!this.constraint.contains(diffence)){
							this.constraint.add(diffence);
						}
					}
				}
				/*
				 * Generation of the second type of constraint onb:b' => fixedb':true
				 * to be continued ----------!!!!
				 */
				for(BooleanVariable fixed : super.getFixedB()) {
					for(Object blockValue : block.getDomain()){
						if(blockValue.hashCode() >= 0){
								Set<Object> domain1 = new HashSet<>();
								domain1.add(blockValue);
								Set<Object> domain2 = new HashSet<>();
								domain2.add(true);
								this.constraint.add(new Implication(block, domain1, fixed, domain2));
							}
					}
				}
				/*
				 * Generating the third type of constraint onb:p => freep:false
				 */
				for(BooleanVariable free : super.getFreeP()) {
					for(Object block1Value : block.getDomain()){
						if(block1Value.hashCode() < 0){
								Set<Object> domain1 = new HashSet<>();
								domain1.add(block1Value);
								Set<Object> domain2 = new HashSet<>();
								domain2.add(false);
								this.constraint.add(new Implication(block, domain1, free, domain2));
						}
					}
				}
			}
	}
	// Get Constraints
	public Set<Constraint> getConstraint() {
		return constraint ; 
	}
}
