package datamining;

import java.util.HashSet;
import java.util.Set;
import modelling.BooleanVariable;

public class AssociationRule {
	// Ensemble de variables booléennes pour la prémisse de la règle
	Set<BooleanVariable> premise;
	// Ensemble de variables booléennes pour la conclusion de la règle
	Set<BooleanVariable> conclusion;
	// Fréquence de la règle
	float frequency;
	// Confiance de la règle
	float confidence;

	public AssociationRule(Set<BooleanVariable> premise, Set<BooleanVariable> conclusion, float frequency,
			float confidence) {
		this.premise = new HashSet<>(premise);
		this.conclusion = new HashSet<>(conclusion);
		this.frequency = frequency;
		this.confidence = confidence;
	}

	public Set<BooleanVariable> getPremise() {
		return premise;
	}

	public void setPremise(Set<BooleanVariable> premise) {
		this.premise = premise;
	}

	public Set<BooleanVariable> getConclusion() {
		return conclusion;
	}

	public void setConclusion(Set<BooleanVariable> conclusion) {
		this.conclusion = conclusion;
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

	@Override
	public String toString() {
		return "promise : " + this.premise;
	}

}
