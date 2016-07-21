package ontologyManagement;


import java.util.Set;

import org.semanticweb.owlapi.model.OWLLogicalEntity;

import similarity.ComparableElement;
import similarity.InformationContent;


public abstract class MyOWLLogicalEntity implements ComparableElement{
	protected String uri;
	protected MyOWLOntology o;
	protected Set<OWLLink> neighbors;
	
	public void setNeighbors(Set<OWLLink> n)
	{
		neighbors = n;
	}
	
	public abstract Set<OWLLink> getNeighbors();
	
	public String getURI()
	{
		return uri;
	}
	
	public String toString()
	{
		return uri;
	}
	
	public String getName()
	{
		return uri.replaceAll(o.getOntologyPrefix(),"").replace("_",":");//("http://purl.org/obo/owl/GO#", "").replace("_", ":");
	}
	
	public boolean isOWLConcept()
	{
		return o.getOWLConcept(uri) != null;
	}
	
	public OWLConcept getOWLConcept()
	{
		return o.getOWLConcept(uri);
	}
	
	public boolean isMyOWLIndividual()
	{
		return o.getOWLIndividual(uri) != null;
	}
	
	public MyOWLIndividual getOWLIndividual()
	{
		return o.getMyOWLIndividual(uri);
	}
	
	public abstract OWLLogicalEntity getOWLLogicalEntity();
	
	public abstract double taxonomicSimilarity(MyOWLLogicalEntity c) throws Exception;
	public abstract double similarity(MyOWLLogicalEntity a) throws Exception;
	protected abstract double similarityNeighbors(MyOWLLogicalEntity c) throws Exception;
	
	public double ICOnSim(MyOWLLogicalEntity c) throws Exception
	{
		//double informC = similarityIC(c);
		double informC = similarityDCA(c);
		
		double taxSim = taxonomicSimilarity(c);
		double neighSim = 1;
		if (taxSim > 0 )
		{
				neighSim = similarityNeighbors(c);
		}
		
		return (informC * taxSim * neighSim);
	}
	
	public double similarityDCA(MyOWLLogicalEntity c)
	{
		double informC = 0;
		try {
			Set<OWLConcept> dca = o.getDCA(this, c);
			for (OWLConcept con: dca)
			{
				informC += con.getIC();
			}
			informC = informC / dca.size();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return informC;
	}
	
	public double similarityIC(MyOWLLogicalEntity c)
	{
		double informC = 0;
		try {
			MyOWLLogicalEntity lca = o.getLCS(this, c);
			InformationContent ic = InformationContent.getInstance();
			informC = ic.getIC(lca);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return informC;
	}
	
	public double OnSim(MyOWLLogicalEntity c) throws Exception
	{
		double taxSim = taxonomicSimilarity(c);
		Double sim = taxSim;
		double neighSim = 1;
		if (taxSim > 0 )
		{
				neighSim = similarityNeighbors(c);
		}
		sim = taxSim * neighSim;
		return sim;
	}
	
	public double Achim(MyOWLLogicalEntity c) throws Exception
	{
		double taxSim = taxonomicSimilarity(c);
		Double sim = taxSim;
		double neighSim = 1;
		//if (taxSim > 0 )
		//{
				neighSim = similarityNeighbors(c);
		//}
		sim = taxSim + neighSim;
		return sim;
	}
	
	public double similarity(ComparableElement a, MyOWLLogicalEntity org, MyOWLLogicalEntity des) throws Exception {
		return similarity((MyOWLLogicalEntity)a);
	}

	public abstract double getIC();
}
