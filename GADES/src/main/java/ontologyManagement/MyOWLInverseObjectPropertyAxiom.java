package ontologyManagement;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;

public class MyOWLInverseObjectPropertyAxiom extends MyOWLAxiom {

	private OWLRelation rel1, rel2;
	protected MyOWLInverseObjectPropertyAxiom(OWLAxiom b, MyOWLOntology onto) {
		super(b, onto);
		rel1 = o.getOWLRelation(((OWLInverseObjectPropertiesAxiom) b).getFirstProperty().asOWLObjectProperty().toStringID());
		rel2 = o.getOWLRelation(((OWLInverseObjectPropertiesAxiom) b).getSecondProperty().asOWLObjectProperty().toStringID());
	}
	
	public double similarity(MyOWLAxiom baxiom, OWLConcept origin, OWLConcept destiny) throws Exception {
			
			MyOWLInverseObjectPropertyAxiom b;
			if (baxiom.getClass() != this.getClass())
				return 0.0;
	
			b = (MyOWLInverseObjectPropertyAxiom) baxiom;
			double simFirst = rel1.similarity(b.rel1);
			double simSecond = rel2.similarity(b.rel2);
			return simFirst * simSecond;
	}
}
