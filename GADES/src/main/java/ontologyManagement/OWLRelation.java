package ontologyManagement;

import org.semanticweb.owlapi.model.OWLObjectProperty;

public class OWLRelation {
	private OWLObjectProperty p;
	private String uri;
	private MyOWLOntology o;
	
	public OWLRelation(OWLObjectProperty property, MyOWLOntology ont)
	{
		o = ont;
		p = property;
		uri = property.toStringID();
	}
	
	public OWLObjectProperty getOWLObjectProperty()
	{
		return p;//o.getOWLObjectProperty(uri);
	}
	
	public String toString()
	{
		return uri;
	}
	
	public double similarity(OWLRelation r)
	{
		return o.taxonomicPropertySimilarity(getOWLObjectProperty(), r.getOWLObjectProperty());
	}
}
