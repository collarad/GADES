package ontologyManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;

public class MyOWLSubPropertyChainOfAxiom extends MyOWLAxiom {
	OWLRelation superProp;
	List<OWLRelation> propChain;
	public MyOWLSubPropertyChainOfAxiom(OWLAxiom axiom, MyOWLOntology onto)
	{
		super(axiom, onto);
		
		propChain = new ArrayList<OWLRelation>();
		OWLSubPropertyChainOfAxiom a = (OWLSubPropertyChainOfAxiom) axiom;
		superProp = o.getOWLRelation(a.getSuperProperty().asOWLObjectProperty().toStringID());
		List<OWLObjectPropertyExpression> list = a.getPropertyChain();
		for (Iterator<OWLObjectPropertyExpression> i = list.iterator(); i.hasNext();)
		{
			OWLObjectPropertyExpression p = i.next();
			propChain.add(o.getOWLRelation(p.asOWLObjectProperty().toStringID()));
		}
		
	}
	@Override
	public double similarity(MyOWLAxiom baxiom, OWLConcept origin,
			OWLConcept destiny) throws Exception {
		
		if (baxiom.getClass() != this.getClass())
			return 0.0;
		
		MyOWLSubPropertyChainOfAxiom b = (MyOWLSubPropertyChainOfAxiom) baxiom;
		
		int min = Math.min(propChain.size(), b.propChain.size());
		int max = Math.max(propChain.size(), b.propChain.size());
		double simChain = 0;
		
		for (int i = 0; i < min; i++)
		{
			simChain += propChain.get(i).similarity(b.propChain.get(i));
		}
		simChain = simChain/max;
		
		double superSim = superProp.similarity(b.superProp);
		
		return (simChain + superSim)/2;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return propChain + " --> " + superProp;
	}

}
