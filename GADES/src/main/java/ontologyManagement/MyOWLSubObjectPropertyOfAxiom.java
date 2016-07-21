package ontologyManagement;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

public class MyOWLSubObjectPropertyOfAxiom extends MyOWLAxiom {
	OWLRelation subProperty, superProperty;
	public MyOWLSubObjectPropertyOfAxiom(OWLAxiom b , MyOWLOntology onto)
	{
		super(b, onto);
		OWLObjectPropertyExpression subA = ((OWLSubObjectPropertyOfAxiom) b).getSubProperty();
		OWLObjectPropertyExpression superA = ((OWLSubObjectPropertyOfAxiom) b).getSuperProperty();
		subProperty = o.getOWLRelation(subA.asOWLObjectProperty().toStringID());
		superProperty = o.getOWLRelation(superA.asOWLObjectProperty().toStringID());
	}
	
	
	public double similarity(MyOWLAxiom baxiom, OWLConcept origin, OWLConcept destiny) throws Exception {
		
		MyOWLSubObjectPropertyOfAxiom b;
		if (baxiom.getClass() != this.getClass())
			return 0.0;

		b = (MyOWLSubObjectPropertyOfAxiom) baxiom;
		double simSub = 0;
		double simSuper = 0;
		simSub = subProperty.similarity(b.subProperty);
		simSuper = superProperty.similarity(b.superProperty);
		return simSub*simSuper; //(simSub + simSuper)/2;
	}


	@Override
	public String toString() {
		return subProperty + " subPropertyOf " + superProperty;
	}
}
