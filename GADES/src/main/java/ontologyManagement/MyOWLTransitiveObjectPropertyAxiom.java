package ontologyManagement;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

public class MyOWLTransitiveObjectPropertyAxiom extends MyOWLAxiom {
	private OWLRelation rel;
	public MyOWLTransitiveObjectPropertyAxiom(OWLAxiom baxiom, MyOWLOntology o)
	{
		super(baxiom, o);
		rel = o.getOWLRelation(((OWLTransitiveObjectPropertyAxiom) baxiom).getProperty().asOWLObjectProperty().toStringID());
	}
	@Override
	public double similarity(MyOWLAxiom baxiom, OWLConcept origin,
			OWLConcept destiny) throws Exception {
		
		if (baxiom.getClass() != this.getClass())
			return 0.0;
		
		MyOWLTransitiveObjectPropertyAxiom b;
		if (baxiom.getClass() != this.getClass())
			return 0.0;

		b = (MyOWLTransitiveObjectPropertyAxiom) baxiom;
		return rel.similarity(b.rel);
	}

	@Override
	public String toString() {
		return "Transitive " + rel;
	}

}
