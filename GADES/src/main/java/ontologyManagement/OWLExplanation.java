package ontologyManagement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

import similarity.ComparableElement;
import similarity.matching.BipartiteGraphMatching;

public class OWLExplanation implements ComparableElement{

	//The aim of this Map is to avoid creating new OWLAxioms when they were already created
	private static Map<Integer, MyOWLAxiom> usedAxioms;
	static
	{
		usedAxioms = new HashMap<Integer, MyOWLAxiom>();
	}
	
	private Set<MyOWLAxiom> sequence;
	
	public OWLExplanation(Set<OWLAxiom> explanation, MyOWLOntology onto) throws Exception
	{
		sequence = new HashSet<MyOWLAxiom>();
		for (Iterator<OWLAxiom> i = explanation.iterator(); i.hasNext();)
		{
			OWLAxiom axiom = i.next();
			if (axiom.getAxiomType() != AxiomType.EQUIVALENT_CLASSES)
			{
				MyOWLAxiom insertedAxiom = usedAxioms.get(axiom.hashCode());
					
				if (insertedAxiom == null)
				{
						insertedAxiom = new MyOWLAxiom(axiom, onto);
						usedAxioms.put(axiom.hashCode(), insertedAxiom);
				}
				sequence.add(insertedAxiom);
			}
		}
	}
	
	public double similarity(OWLExplanation b, OWLConcept org, OWLConcept des)
	{
		BipartiteGraphMatching bpm = new BipartiteGraphMatching();
		try {
			return bpm.matching(sequence, b.sequence, org, des);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;
	}

	public Set<MyOWLAxiom> getSequence() {
		return sequence;
	}

	public void setSequence(Set<MyOWLAxiom> sequence) {
		this.sequence = sequence;
	}
	
	public String toString()
	{
		return sequence.toString();
	}

	public double similarity(ComparableElement a, MyOWLLogicalEntity org, MyOWLLogicalEntity des) throws Exception {
		if (!(a instanceof OWLExplanation))
			throw new Exception("Invalid comparison");
		return similarity((OWLExplanation)a, (OWLConcept)org, (OWLConcept)des);
	}
	
}
