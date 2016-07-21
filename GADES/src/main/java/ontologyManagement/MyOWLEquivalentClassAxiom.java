package ontologyManagement;

import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import similarity.matching.BipartiteGraphMatching;

public class MyOWLEquivalentClassAxiom extends MyOWLAxiom {

	private Set<MyOWLSubClassOfAxiom> myAxioms;
	
	public MyOWLEquivalentClassAxiom(OWLAxiom b, MyOWLOntology onto) throws Exception {
		super(b, onto);
		OWLEquivalentClassesAxiom ax = (OWLEquivalentClassesAxiom) b;
		Set<OWLSubClassOfAxiom> axioms = ax.asOWLSubClassOfAxioms();
		for (Iterator<OWLSubClassOfAxiom> i = axioms.iterator(); i.hasNext();)
		{
			OWLSubClassOfAxiom a = i.next();
			myAxioms.add(new MyOWLSubClassOfAxiom(a, onto));
			
		}
	}
	
	public double similarity(MyOWLAxiom baxiom, OWLConcept origin, OWLConcept destiny) throws Exception {
		
		MyOWLEquivalentClassAxiom b;
		if (baxiom.getClass() != this.getClass())
			return 0.0;

		b = (MyOWLEquivalentClassAxiom) baxiom;
		BipartiteGraphMatching bpm = new BipartiteGraphMatching();
		return bpm.matching(myAxioms, b.myAxioms, origin, destiny);
		
	}

}
