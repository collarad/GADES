package ontologyManagement;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class MyOWLSubClassOfAxiom extends MyOWLAxiom {
	OWLConcept subConcept, superConcept;
	OWLRelation rel;
	
	public MyOWLSubClassOfAxiom(OWLAxiom b , MyOWLOntology onto) throws Exception
	{
		super(b, onto);
		OWLClassExpression subA = ((OWLSubClassOfAxiom) b).getSubClass();
		OWLClassExpression superA = ((OWLSubClassOfAxiom) b).getSuperClass();
		
		subConcept = o.getOWLConcept(subA.asOWLClass().toStringID());
		switch (superA.getClassExpressionType())
		{
			case OBJECT_SOME_VALUES_FROM:
			{
				OWLObjectProperty ra = (OWLObjectProperty) ((OWLObjectSomeValuesFrom) superA).getProperty();
				OWLClassExpression fa = ((OWLObjectSomeValuesFrom) superA).getFiller();
				rel = o.getOWLRelation(ra.toStringID());
				superConcept = o.getOWLConcept(fa.asOWLClass().toStringID());
				break;
			}
			case OWL_CLASS:
			{
				superConcept = o.getOWLConcept(superA.asOWLClass().toStringID());
				rel = null;
				break;
			}
			default:
				throw new Exception("We do not know how to deal with the OWLClassExpresions \n" + superA.getClassExpressionType());
		}
	}
	
	public double similarity(MyOWLAxiom baxiom, OWLConcept origin, OWLConcept destiny) throws Exception {
		MyOWLSubClassOfAxiom b;
		if (baxiom.getClass() != this.getClass())
			return 0.0;

		b = (MyOWLSubClassOfAxiom) baxiom;
		double subSim = 0, superSim = 0, relSim = 0;
		
		if (rel == null && b.rel == null) //If both are OWLClass, relSim has no effect in the comparison
			relSim = 1;
		if (rel != null && b.rel != null) //If both are ObjectSomeValuesFrom, the relSim is calculated
			relSim = rel.similarity(b.rel);
		//If one is OWLClass and the other is ObjectSomeValuesFrom, relSim = 0
		
		if (subConcept == origin && b.subConcept == destiny || subConcept == destiny && b.subConcept == origin)
			subSim = 1;
		else
			subSim = o.taxonomicClassSimilarity(subConcept/*.getOWLClass()*/, b.subConcept/*.getOWLClass()*/);//subConcept.similarity(b.subConcept);
		if (superConcept == origin && b.superConcept == destiny || superConcept == destiny && b.superConcept == origin)
			superSim = 1;
		else
			superSim = o.taxonomicClassSimilarity(superConcept/*.getOWLClass()*/, b.superConcept/*.getOWLClass()*/);//superConcept.similarity(b.superConcept);
					
		return subSim * relSim * superSim;
		/*if (relSim >= 0)
			return subSim * relSim * superSim;
		else
			return subSim * superSim;*/
	}

	@Override
	public String toString() {
		return subConcept + " subClassOf " + rel + " some " + superConcept;
	}
}
